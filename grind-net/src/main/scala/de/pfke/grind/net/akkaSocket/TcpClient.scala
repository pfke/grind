package de.pfke.grind.net.akkaSocket

import java.net.InetSocketAddress

import akka.actor._
import akka.io
import akka.io.Tcp
import akka.io.Tcp._

import scala.collection.mutable
import scala.concurrent.duration._
import scala.language.postfixOps

//object TcpClient {
//  /**
//    * Connect as client to a remote host.
//    * @param host is the ip address as string
//    * @param port is the remote port to connect to
//    * @param observer is the function for notify events
//    * @param actorSystem is used to create the TCPClient actor
//    * @return TCPClient actor
//    */
//  def connect (
//    host: String,
//    port: Int,
//    observer: Observer[GrindEvent]
//  ) (
//    implicit
//    actorSystem: ActorSystem
//  ): ActorRef = connect(new InetSocketAddress(host, port), observer)
//
//  /**
//    * Connect as client to a remote host.
//    * @param remote is the remote ip address
//    * @param handler is the handler class def which is instantiated when a connection is established (this connection notifies will be passed to the handler, so calling code must only describe to this class to get notified)
//    * @param notify is the function for notify events
//    * @param actorSystem is used to create the TCPClient actor
//    * @return TCPClient actor
//    */
//  def connect (
//    remote: InetSocketAddress,
//    notify: Observer[GrindEvent],
//    handler: Class[_ <: TcpHandler] = classOf[AckHandler]
//  ) (
//    implicit
//    actorSystem: ActorSystem
//  ): ActorRef = ActorHeHyphenMan[TcpClient](remote, handler, notify)
//}
//
//class TcpClient (
//  remoteAdr: InetSocketAddress,
//  handlerClass: Class[_ <: TcpHandler],
//  observer: Observer[GrindEvent]
//)
//  extends Actor
//    with RxBus[GrindEvent] {
//  import context.system
//  // fields
//  private var _handler: Option[ActorRef] = None
//  private val _txQueue = new mutable.Queue[TcpHandler.tx]()
//
//  // ctor
//  registerObserver(observer)
//  io.IO(Tcp) ! Connect(remoteAdr, timeout = Some(1 second))
//
//  /**
//    * Receive method while waiting for connection.
//    */
//  def receive: Receive = {
//    case CommandFailed(m: Connect) =>
//      post(ConnectionFailed(cause = m.toString))
//      context stop self
//
//    case Connected(remote, local) =>
//      val handler = TcpHandler.create(handlerClass = handlerClass, peerActor = sender(), peerAddress = remote, localAddress = local, notifyFromPeerActor = Some(this))
//
//      _handler = Some(handler)
//
//      sender ! Tcp.Register(handler, keepOpenOnPeerClosed = true)
//      post(ConnectionEstablished(handler, remote, local))
//
//      _txQueue
//        .dequeueAll(_ => true)
//        .foreach(handler forward _)
//
//      // sign death pact: this actor terminates when connection breaks
//      context watch handler
//      context become receiveWhenEstablished
//
//    case TcpHandler.close => context stop self
//    case m: TcpHandler.tx => _txQueue.enqueue(m)
//  }
//
//  /**
//    * This is hte rx method for every-day-use messages.
//    */
//  def receiveWhenEstablished: Actor.Receive = {
//    case Terminated(_) => context stop self
//    case m@_           => dispatch(m)
//  }
//
//  /**
//    * Close connection
//    */
//  private def dispatch(m: Any) {
//    _handler match {
//      case Some(x) => x ! m
//      case None =>
//        m match {
//          case Tcp.Close => context stop self
//          case _ =>
//        }
//    }
//  }
//}
