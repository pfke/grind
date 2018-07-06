package de.pfke.grind.net.telnet.old

import java.io.IOException
import java.net.{InetAddress, InetSocketAddress}
import java.nio.charset.StandardCharsets
import java.time.Instant

import akka.actor.{ActorRef, ActorSystem, FSM, PoisonPill}
import akka.util.ByteString
import org.apache.commons.net.{telnet => at}

import scala.collection.mutable
import scala.concurrent.duration._
import scala.language.implicitConversions
import scala.util.{Failure, Success, Try}

/**
  * Usage:
  *
  */
//object TelnetClientActorOld {
//  private[TelnetClientActorOld] val LOGIN_FAILED_REGEX = """Login failed.*""".r
//  private[TelnetClientActorOld] val PROMPT_FOR_USER_REGEX = """\s*Fritz!Box user:\s*""".r
//  private[TelnetClientActorOld] val PROMPT_FOR_PWD_REGEX = """(?s).*password:\s*""".r
//  private[TelnetClientActorOld] val PROMPT_REGEX = """.*#\s*""".r
//
//  val DEFAULT_PORT = 23
//  case class TelnetHandle (descr: TelnetClientDescr, actor: ActorRef)
//
//  sealed trait TelnetEvent extends GrindEvent
//
//  // message classes
//  sealed trait TelnetMsg
//  sealed trait TelnetReq extends TelnetMsg
//  sealed trait TelnetRsp extends TelnetMsg
//
//  case class Connect(descr: TelnetClientDescr)          extends TelnetReq
//
//  /**
//    *
//    * @param receiver
//    */
//  case class ReportToMe(receiver: ActorRef)           extends TelnetReq
//  case class Write(data: String, log: Boolean = true) extends TelnetReq
//
//
//  case class Error(ex: TelnetException)               extends TelnetRsp
//  case class Read(data: String)                       extends TelnetRsp
//
////  case class Error(ex: TelnetException) extends TelnetRsp
////  case class Tx(data: String)           extends TelnetRsp
////  case class Rx(data: String)           extends TelnetRsp
//
//  /**
//    * FSM States
//    */
//  private[telnet] sealed trait State
//  private[telnet] case object WaitForPrompt extends State
//  private[telnet] case object WaitForLogin  extends State
//  private[telnet] case object Idle          extends State
//  private[telnet] case object Failure       extends State
//  private[telnet] case object Stopped       extends State
//
//  /**
//    * FSM Event Data
//    */
//  private[telnet] sealed trait Data
//  private[telnet] case object NoData extends Data
//  private[telnet] case class ErrorData(exerr: TelnetException) extends Data
//
//  /**
//    * Create an actor which is a telnet client..
//    */
//  def apply (
//    descr: TelnetClientDescr
//  ) (
//    implicit
//    actorSystem: ActorSystem
//  ): TelnetHandle = {
//    TelnetHandle (
//      descr = descr,
//      actor = ActorHeHyphenMan[TelnetClientActor](
//        descr.address,
//        descr.cred.user.getOrElse(""),
//        descr.cred.password.getOrElse(""),
//        descr.cred.pauseAfterWrite,
//        descr.timeout
//      )
//    )
//  }
//
//  /**
//    * Test ob mit den agegebenen Zugangsdaten eine Telnet-Verbindung aufgebaut werden kann.
//    */
//  def apply (
//    host: InetAddress,
//    port: Int = DEFAULT_PORT,
//    cred: TelnetCredentials,
//    timeout: FiniteDuration
//  ) (
//    implicit
//    actorSystem: ActorSystem
//  ): TelnetHandle = apply(descr = TelnetClientDescr(address = new InetSocketAddress(host, port), cred = cred, timeout = timeout))
//
//  /**
//    * Gibt true zurueck, wenn die Zeile einen Prompt anzeigt
//    */
//  def isAnPrompt (
//    in: String
//  ): Boolean = {
//    in match {
//      case TelnetClientActorOld.PROMPT_REGEX() => true
//      case _ => false
//    }
//  }
//}
//
//class TelnetClientActorOld (
//  address: InetSocketAddress,
//  user: String,
//  password: String,
//  pauseAfterWrite: Duration,
//  timeout: FiniteDuration
//)
//  extends FSM[State, Data]
//    with RxBus[GrindEvent]
//    with ErrorEventReporter
//    with TrafficEventReporter
//{
//  // types
//  case class InternalError(ex: TelnetException)
//  case class InternalRx(data: String)
//
//  // fields
//  private val _telnet = new at.TelnetClient()
//  private val _reader = new Reader()
//  private val _writeQueue = new mutable.Queue[(String, Boolean)]()
//  private var _writtenTimeStamp = Instant.now()
//
//  /**
//    * Write string into queue or into telnet writer.
//    */
//  private def write (
//    data: String,
//    writeIntoQueue: Boolean = false,
//    log: Boolean = true,
//    nextState: Option[TelnetClientActorOld.State] = None
//  ): State = {
//    if (writeIntoQueue) {
//      _writeQueue += (data -> log)
//    } else {
//      writeTo(data, pauseAfterWrite, log)
//    }
//
//    nextState match {
//      case Some(x) => goto(x)
//      case None => stay()
//    }
//  }
//
//  private def writeTo (
//    data: String,
//    pauseAfterWrite: Duration,
//    log: Boolean
//  ): Unit = {
//    def waitToSend (): Unit = {
//      val r1 = Instant.now().toEpochMilli
//      val r2 = pauseAfterWrite.toMillis
//      val r3 = _writtenTimeStamp.toEpochMilli
//      if ((r3 + r2) < r1) {
//        Thread.sleep(r1 - r3 - r2)
//      }
//    }
//
//    // wait last send 'pauseAfterWrite'
//    waitToSend()
//
//    try {
//      _telnet.getOutputStream.write(data.getBytes(StandardCharsets.ISO_8859_1))
//      _telnet.getOutputStream.flush()
//
//      _writtenTimeStamp = Instant.now()
//
//      reportTx(data, log)
//    } catch {
//      case e: IOException => self ! InternalError(new TelnetException(s"tx: error while writing: ${e.getMessage}"))
//    }
//  }
//
//  /**
//    * <code>reportToThisActor</code> wird über den Fehler informiert
//    */
//  private def reportError (
//    data: Data
//  ) = {
//    data match {
//      case ErrorData(ex) => reportCritical(ex)
//      case t             => reportCritical(new TelnetException(msg = s"got an unknown error: $t"))
//    }
//  }
//
//  /**
//    * <code>reportToThisActor</code> wird über den Transfer informiert informiert + wir selber auch
//    */
//  private def reportRx (
//    data: String
//  ) = {
//    self ! InternalRx(data)
//    reportTrafficRx(data)
//  }
//
//  /**
//    * <code>reportToThisActor</code> wird über den Transfer informiert informiert
//    */
//  private def reportTx (
//    data: String,
//    log: Boolean
//  ) = if (log) reportTrafficTx(data)
//
//  /**
//    * Wir wollen uns auf dem Telnet einloggen. Also Erst-Kontakt, und das kam zurück.
//    * Und auf diese Antwort(en) wird mit dieser Methode reagiert
//    *
//    * @param in ist der empfangene String der jetzt ausgewertet werden soll
//    * @return
//    *         stay() .. Mülldaten empfangen, Username soll gesendet werden
//    *         goto(Idle) .. wir haben einen Prompt
//    *         goto(WaitForLoginValidation) .. pwd eingegeben und jetzt auf Bestätigung warten
//    */
//  private def checkPromptRx (
//    in: String
//  ): State = {
//    in match {
//      case TelnetClientActorOld.PROMPT_FOR_USER_REGEX() => write(user,     log = false)
//      case TelnetClientActorOld.PROMPT_FOR_PWD_REGEX()  => write(password, log = false, nextState = Some(WaitForLogin))
//      case TelnetClientActorOld.PROMPT_REGEX()          => goto(Idle)
//
//      case _ => stay()
//    }
//  }
//
//  /**
//    * Wir wollen uns auf dem Telnet einloggen und es wurde bereits ein pwd gesendet -> hier wird die Antwort ausgewertet.
//    *
//    * @param in ist der empfangene String
//    * @return
//    *         stay() .. Mülldaten empfangen
//    *         goto(Idle) .. Login erfolgreich
//    *         goto(Failure) .. Login nicht erfolgreich
//    */
//  private def checkLoginRx (
//    in: String
//  ): State = {
//    in match {
//      case TelnetClientActorOld.PROMPT_REGEX()       => goto(TelnetClientActorOld.Idle)
//      case TelnetClientActorOld.LOGIN_FAILED_REGEX() => goto(TelnetClientActorOld.Failure) using ErrorData(TelnetException("login failed"))
//
//      case _ => stay()
//    }
//  }
//
//  /**
//    * Stop operation an disconnect from client
//    *
//    * @return
//    *         goto(Stopped) .. in jedem Fall
//    */
//  private def stopOp(): Unit = {
//    if(_telnet.isConnected) {
//      writeTo("exit\n", pauseAfterWrite, log = true)
//      _reader.shutdown()
//    }
//    _telnet.disconnect()
//  }
//
//  // fsm zeuch
//
//  /**
//    * Dieser State ist solange aktiv, bis wir einen Prompt bekommen, oder ein Passwort gesendet haben.
//    */
//  when(WaitForPrompt, stateTimeout = timeout) {
//    case Event(InternalRx(s), _) => checkPromptRx(s)
//
//    case Event(StateTimeout, _)  => goto(TelnetClientActorOld.Failure) using ErrorData(TelnetException("connection timeout"))
//  }
//
//  /**
//    * Dieser State ist solange aktiv, bis der Login (also eingegebener User+PWD) validiert wurde und uns der Client eine
//    * Antwort schickt.
//    */
//  when(WaitForLogin, stateTimeout = timeout) {
//    case Event(InternalRx(s), _) => checkLoginRx(s)
//
//    case Event(StateTimeout, _)  => goto(TelnetClientActorOld.Failure) using ErrorData(TelnetException("connection timeout while waiting for login"))
//  }
//
//  /**
//    * Hier idled man halt rum. Warten auf das zu Schreiben, weiterleiten von empfangenen Daten
//    */
//  when(Idle) {
//    case Event(Write(s, log), _)    => write(s, log = log)
//    case Event(InternalRx(s), _)    => reportRx(s); stay()
//
//    case Event(InternalError(ex), _) => goto(TelnetClientActorOld.Failure) using ErrorData(ex)
//  }
//
//  when(TelnetClientActorOld.Failure) {
//    case _ => stay()
//  }
//
//  when(Stopped) {
//    case _ => stay()
//  }
//
//  whenUnhandled {
//    case Event(Write(s, log), _) => write(s, writeIntoQueue = true, log = log)
//    case Event(PoisonPill, _)    => goto(Stopped)
//
//    case _ => stay()
//  }
//
//  onTransition {
//    case _ -> TelnetClientActorOld.Idle    => _writeQueue.foreach { case (text,log) => writeTo(text, pauseAfterWrite, log) }
//    case _ -> TelnetClientActorOld.Failure => reportError(nextStateData)
//    case _ -> TelnetClientActorOld.Stopped => stopOp()
//  }
//
//  // start the fsm
//  startWith(WaitForPrompt, NoData)
//  initialize()
//
//  // connect to client
//  Try(_telnet.connect(address.getAddress, address.getPort)) match {
//    case Success(_) => _reader.start()
//    case Failure(e) => goto(TelnetClientActorOld.Failure) using ErrorData(TelnetException(s"could not connect to $address. Got ex: $e"))
//  }
//
//  class Reader extends Runnable {
//    private var _isShutdown = false
//    private val _thread = new Thread(this)
//    private val _loginDecoder = new LineExtractor(reportOp = reportRx, charset = StandardCharsets.ISO_8859_1)
//
//    def run() {
//      val buf = new Array[Byte](4 * 1024)
//      var readBytes = 0
//
//      try {
//        while({ readBytes = _telnet.getInputStream.read(buf); readBytes } != -1 && !_isShutdown) {
//          _loginDecoder.decode(in = ByteString(buf.slice(0, readBytes)), timestamp = Instant.now())
//        }
//      } catch {
//        case e: IOException => self ! InternalError(ex = TelnetException(s"error while op: $e"))
//      }
//    }
//
//    def shutdown() {
//      if(_thread.isAlive) {
//        _isShutdown = true
//        _telnet.getInputStream.close()
//      }
//    }
//
//    def start(): Unit = {
//      if(!_thread.isAlive)
//        _thread.start()
//    }
//  }
//}
