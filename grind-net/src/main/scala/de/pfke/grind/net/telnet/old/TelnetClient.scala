package de.pfke.grind.net.telnet.old

import java.net.InetAddress
import java.nio.charset.{Charset, StandardCharsets}

import akka.actor.{Actor, ActorRef, ActorSystem, PoisonPill}
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}

object TelnetClient {
  /**
    * Create an actor which is a telnet client..
    */
  def apply (
    descr: TelnetClientDescr
  ) (
    implicit
    actorSystem: ActorSystem
  ): TelnetClient = new TelnetClient(descr = descr)

  /**
    * Test ob mit den agegebenen Zugangsdaten eine Telnet-Verbindung aufgebaut werden kann.
    */
  def apply (
    host: InetAddress,
    port: Int = TelnetClientActorOld.DEFAULT_PORT,
    cred: TelnetCredentials,
    timeout: FiniteDuration
  ) (
    implicit
    actorSystem: ActorSystem = ActorSystem("telnetclient")
  ): TelnetClient = apply(descr = TelnetClientDescr(host = host, port = port, cred = cred, timeout = timeout))

  /**
    * Test ob mit den agegebenen Zugangsdaten eine Telnet-Verbindung aufgebaut werden kann.
    */
  def testCredentials (
    descr: TelnetClientDescr
  ) (
    implicit
    actorSystem: ActorSystem
  ): Boolean = {
    implicit val timeout = descr.timeout

    val p = Promise[Boolean]()

    def populateFailure(event: ErrorEvent): Unit = {
      event.err match {
        case t: TelnetException => p.failure(t)
      }}

    def populateSuccess(event: Rx): Unit = {
      event match {
        case Rx(in: String, _) if TelnetClientActorOld.isAnPrompt(in) => p.success(true)
        case Rx(in: String, _) => println(s">> $in")
        case in: RunlevelEvent => println(s"ee $in")
        case t => println(s"so: $t")
      }}

    Future {
      val telnetClient = TelnetClient(descr = descr)

      telnetClient
        .registerObserver(Subscriber[ErrorEvent]({n: ErrorEvent => populateFailure(n) }))
      telnetClient
        .registerObserver(Subscriber[Rx]({n: Rx => populateSuccess(n) }))

      telnetClient.start()
      telnetClient.stop()
      println()
    }

    p.future
      .tryAwait
      .toOption
      .nonEmpty
  }
}

class TelnetClient (
  descr: TelnetClientDescr
) (
  implicit
  actorSystem: ActorSystem,
  charset: Charset = StandardCharsets.ISO_8859_1
)
  extends RxBus[GrindEvent]
    with ErrorEventReporter
    with TrafficEventReporter {
  implicit val timeout: Timeout = Timeout(descr.timeout * 2)

  // fields
  private val _telnetControllerActor = ActorHeHyphenMan[TelnetClientControllerActor]()

  // ctor
  _telnetControllerActor.tell(this, null)

  /**
    * Connect to Telnet-Svr
    */
  def start (): Unit = {
    _telnetControllerActor
      .ask(ConnectReq(descr))
      .mapTo[ConnectRsp]
      .tryAwait match {
      case Success(t) => 
      case Failure(e) => throw e
    }
  }

  /**
    * Stop this shit
    */
  def stop(): Unit = {
    _telnetControllerActor ! PoisonPill
  }

  /**
    * Format string to put to a telnet conn.
    */
  def write(
    data: String,
    log: Boolean = true
  ): Unit = true//_telnetControllerActor.tell(WriteReq(data, log = log), null)

  /**
    * Format string + '\n' to put to a telnet conn.
    */
  def writeln(
    data: String,
    log: Boolean = true
  ): Unit = write(data = s"$data\n", log = log)
}

private [telnet] class TelnetClientControllerActor
  extends Actor
    with RxBus[GrindEvent]
    with TrafficEventReporter
    with ErrorEventReporter
    with RunLevelEventReporter
{
  implicit private val actorSystem = context.system

  // fields
  private val _telnetActor = TelnetClientActor()
  private var _connSender: Option[ActorRef] = None

  // ctor
  _telnetActor ! ListenerReq(self)

  override def receive: Receive = {
    case m: ConnectReq => _telnetActor ! m; _connSender = Some(sender())
    case m: ConnectRsp => _connSender match {
      case Some(x) => x ! m
      case None =>
    }

//    case m: WriteReq => _telnetActor ! m
    case t: RxBus[_] => registerObserver(t)

    case TelnetClientActor.Rx(data, time) => reportTrafficRx(data, time)
    case TelnetClientActor.Tx(data, time) => reportTrafficTx(data, time)
    case TelnetClientActor.Error(msg) => reportCritical(msg)

    case TelnetClientActor.RunLevel("NotConnected") => reportRunLevelAfterShutdown()
    case TelnetClientActor.RunLevel("WaitForLogin") => reportRunLevelInit()
    case TelnetClientActor.RunLevel("WaitForPrompt") => reportRunLevelInit()
    case TelnetClientActor.RunLevel("Idle") => reportRunLevelRunning()
    case TelnetClientActor.RunLevel("Failure") => reportRunLevelFailure()

    case PoisonPill => _telnetActor.tell(PoisonPill, null)

    case t => println(s"unhandled msg $t")
  }
}