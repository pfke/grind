package de.pfke.grind.net.telnet.old

import java.io.{IOException, InputStream, OutputStream}
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.time.Instant

import akka.actor.{ActorRef, ActorSystem, FSM, PoisonPill}
import akka.util.ByteString

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

//object TelnetClientActor {
//  private[TelnetClientActor] val LOGIN_FAILED_REGEX = """Login failed.*""".r
//  private[TelnetClientActor] val PROMPT_FOR_USER_REGEX = """.*Fritz!Box user:\s*""".r
//  private[TelnetClientActor] val PROMPT_FOR_PWD_REGEX = """(?s).*password:\s*""".r
//  private[TelnetClientActor] val PROMPT_REGEX = """.*#\s*""".r
//
//  val DEFAULT_PORT = 23
//
//  // message classes
//  sealed trait TelnetMsg
//  sealed trait TelnetReq extends TelnetMsg
//  sealed trait TelnetRsp extends TelnetMsg
//
//  sealed trait ConnectMsg extends TelnetMsg
//  case class   ConnectReq (descr: TelnetClientDescr)     extends ConnectMsg with TelnetReq
//  sealed trait ConnectRsp                                extends ConnectMsg with TelnetRsp
//  case class   ConnectFailure (ex: Exception)            extends ConnectRsp
//  case class   ConnectSuccess (descr: TelnetClientDescr) extends ConnectRsp
//
//  sealed trait DisconnectMsg extends TelnetMsg
//  case object  DisconnectReq                     extends DisconnectMsg with TelnetReq
//  sealed trait DisconnectRsp                     extends DisconnectMsg with TelnetRsp
//  case class   DisconnectFailure (ex: Exception) extends DisconnectRsp
//  case object  DisconnectSuccess                 extends DisconnectRsp
//
//  sealed trait ListenerMsg extends TelnetMsg
//  case class   ListenerReq(actor: ActorRef) extends ListenerMsg with TelnetReq
//  case class   ListenerRsp(actor: ActorRef) extends ListenerMsg with TelnetRsp
//
//  sealed trait TrafficMsg
//  private[telnet] case class InternalRx(data: String, timeStamp: Instant = Instant.now())
//  case class Rx(data: String, timeStamp: Instant) extends TrafficMsg with TelnetRsp
//  case class Tx(data: String, timeStamp: Instant = Instant.now()) extends TrafficMsg with TelnetRsp
//
//  private[telnet] case class InternalError(msg: String)
//  case object ConnectionClosed extends TelnetRsp
//  case class Error(msg: String) extends TelnetRsp
//  case class RunLevel(name: String) extends TelnetRsp
//
//  /**
//    * FSM States
//    */
//  private[telnet] sealed trait State
//  private[telnet] case object NotConnected  extends State
//  private[telnet] case object Connecting    extends State
//  private[telnet] case object Connected     extends State
//  private[telnet] case object Disconnecting extends State
//
//  /**
//    * FSM Event Data
//    */
//  private[telnet] sealed trait Data
//  private[telnet] case object NoData extends Data
//  private[telnet] case class ConnectingData (
//    descr: TelnetClientDescr,
//    connectFuture: Future[Unit]
//  ) extends Data
//
//  private[telnet] case class ClientData (
//    descr: TelnetClientDescr,
//    socket: Socket,
//    inputStream: InputStream,
//    outputStream: OutputStream,
//    telnetReader: TelnetClientReader,
//    writeQueue: mutable.Queue[(String, Boolean)],
//    var lastWriteTimeStamp: Instant = Instant.now()
//  ) extends Data
//
//  private[telnet] case class ErrorData (
//    err: String,
//    clientData: ClientData
//  ) extends Data
//
//  /**
//    * Create an actor which is a telnet client..
//    */
//  def apply () (
//    implicit
//    actorSystem: ActorSystem
//  ): ActorRef = ActorHeHyphenMan[TelnetClientActor]()
//}
//
//class TelnetClientActor ()
//  extends FSM[State, Data] {
//  sealed trait InternalMsg
//  case class InternalConnectSuccess(descr: TelnetClientDescr, socket: Socket)
//  case class InternalConnectFailure(descr: TelnetClientDescr, ex: Throwable)
//
//  // fields
//  private val _eventBus = new GrindFSMEventBus(self)
//    with ConnectionEventProvider
//    with TrafficEventProvider
//
//  // fsm zeuch
//
//  /**
//    * Dieser State ist aktiv, wenn keinerlei Vetbindung zu einem Telnet-svr besteht.
//    * Es werden keine Daten erwartet
//    */
//  when(TelnetClientActor.NotConnected) {
//    case Event(ConnectReq(descr), _) => connectTo(descr = descr)
//  }
//
//  when(TelnetClientActor.Connecting) {
//    case Event(DisconnectReq, _) => goto(TelnetClientActor.Disconnecting)
//
//    case Event(m: InternalConnectSuccess, _) => connectSuccess(m.descr, m.socket)
//    case Event(m: InternalConnectFailure, _) => connectError(m.descr, m.ex)
//  }
//
//  when(TelnetClientActor.Connected) {
//    case Event(m: InternalRx, _) => reportTraffic(m)
//  }
//
//  when(TelnetClientActor.Disconnecting) {
//    case Event(DisconnectReq, _) => stay()
//  }
//
//  whenUnhandled {
//    case Event(PoisonPill, clientData: ClientData) => closeConnection(clientData)
////    case Event(WriteReq(s, log), clientData: ClientData) => send(s, clientData, log = log)
//    case Event(InternalError(err), clientData: ClientData) => fail(err, clientData)
//
//    case Event(msg: SubscribeRequest, _) => _eventBus.forward(sender(), msg); stay()
//    case Event(msg: UnSubscribeRequest, _) => _eventBus.forward(sender(), msg); stay()
//
//    case e => println("unhandled msg: " + e); stay()
//  }
//
//  onTransition {
//    case _ -> TelnetClientActor.NotConnected => _eventBus.publishConnectionNotConnected()
//    case _ -> TelnetClientActor.Connecting => _eventBus.publishConnectionConnecting()
//    case _ -> TelnetClientActor.Connected => _eventBus.publishConnectionConnected()
//    case _ -> TelnetClientActor.Disconnecting => _eventBus.publishConnectionDisconnecting()
//  }
//
//  startWith(TelnetClientActor.NotConnected, TelnetClientActor.NoData)
//  initialize()
//
//  /**
//    * Verbindung schliessen
//    */
//  private def closeConnection (
//    clientData: ClientData,
//    cause: Option[String] = None
//  ): State = {
//    if (clientData.socket.isConnected) {
////      sendNow("exit\n", clientData, log = true)
//      clientData.telnetReader.shutdown()
//    }
//    clientData.socket.close()
//
//    _eventBus.publishConnectionDisconnecting(cause = cause)
//
//    goto(TelnetClientActor.NotConnected) using NoData
//  }
//
//  /**
//    * Verbindung schliessen
//    */
//  private def closeConnection (
//    errorData: ErrorData
//  ): State = closeConnection(errorData.clientData, cause = Some(errorData.err))
//
//  /**
//    * Verbindung zu dem Telnt-svr aufnehmen.
//    */
//  private def connectTo (
//    descr: TelnetClientDescr
//  ): State = {
//    goto(TelnetClientActor.Connecting) using ConnectingData(
//      descr = descr,
//      connectFuture = Future {
//        val socket = new Socket()
//
//        Try(socket.connect(descr.address)) match {
//          case Success(_) => self ! InternalConnectSuccess(descr = descr, socket = socket)
//          case Failure(e) => self ! InternalConnectFailure(descr = descr, ex = e)
//        }
//      }
//    )
//  }
//
//  /**
//    * Fehler beim Verbindungsaufbau.
//    * Wir verbleiben um <code>NotConnected</code> State und senden einen Fehler an den Anfrager.
//    */
//  private def connectError (
//    descr: TelnetClientDescr,
//    ex: Throwable
//  ): State = {
//    require(stateName == TelnetClientActor.Connecting, s"expected the current state is $Connecting, but was $stateName")
//
//    // TODO: Fehler senden
//
//    goto(TelnetClientActor.NotConnected)
//  }
//
//  /**
//    * Erfolg beim Verbindungsaufbau.
//    * Wir gehen zum nächsten State und senden den Status an den Anfrager.
//    */
//  private def connectSuccess (
//    descr: TelnetClientDescr,
//    socket: Socket
//  ): State = {
//    require(stateName == Connecting, s"expected the current state is $NotConnected, but was $stateName")
//
//    setStateTimeout(TelnetClientActor.Connecting, Some(descr.timeout))
//
//    goto(TelnetClientActor.Connected) using ClientData(
//      descr = descr,
//      socket = socket,
//      inputStream = socket.getInputStream,
//      outputStream = socket.getOutputStream,
//      telnetReader = new TelnetClientReader(self, inputStream = socket.getInputStream),
//      writeQueue = new mutable.Queue[(String, Boolean)]()
//    )
//  }
//
//  /**
//    * Irgendwas ist schief gegangen. Uns selber eine Error-msg schicken und im selben State bleiben
//    */
//  private def fail (
//    msg: String,
//    clientData: ClientData
//  ): State = {
//    val data = ErrorData(msg, clientData)
//
//    closeConnection(data)
//  }
//
//  /**
//    * Alls Nachrichten weiterleiten an unsere Listener.
//    */
//  private def reportTraffic (
//    msg: InternalRx
//  ): State = {
//    _eventBus.publishTrafficRx(data = msg.data, timeStamp = msg.timeStamp)
//    stay()
//  }
//}
//
//private[telnet] class TelnetClientReader (
//  reportToActor: ActorRef,
//  inputStream: InputStream
//) extends Runnable {
//  private var _isShutdown = false
//  private val _loginDecoder = new LineExtractor(reportOp = { data =>
//    reportToActor ! InternalRx(data = data)
//  }, charset = StandardCharsets.ISO_8859_1)
//  private val _thread = new Thread(this)
//
//  // ctor
//  _thread.start()
//
//  def run() {
//    val buf = new Array[Byte](4 * 1024)
//    var readBytes = 0
//
//    try {
//      while({ readBytes = inputStream.read(buf); readBytes } != -1 && !_isShutdown) {
//        _loginDecoder.decode(in = ByteString(buf.slice(0, readBytes)), timestamp = Instant.now())
//      }
//    } catch {
//      case e: IOException => reportToActor ! InternalError(msg = s"error while reading: $e")
//    }
//  }
//
//  def shutdown() {
//    if(_thread.isAlive) {
//      _isShutdown = true
//      inputStream.close()
//    }
//  }
//}
