package de.pfke.grind.net.akkaSocket

import akka.actor.{Actor, ActorRef, ActorSystem}
import akka.io
import akka.io.Tcp
import java.net.InetSocketAddress

import Tcp._
import de.pfke.grind.core.async.event.GrindEvent
import de.pfke.grind.net.akkaSocket.AkkaTcpEvent.ConnectionEstablished

/**
  * Example handler:
  *
  * class SimplisticHandler(conn: ActorRef) extends Actor {
  *   import Tcp._
  *   def receive = {
  *     case Command.Close => conn ! Close
  *     case Command.Send(data) => conn ! Write(data)
  *     case Received(data) => ...
  *     case PeerClosed => context stop self
  *   }}
  */
object TCPServer{
  /**
    * Listen on this port.
    *
    * You will get a TcpHandler.Established() event.
    */
  def listen (
    port: Int,
    notify: Observer[GrindEvent]
  ) (
    implicit
    actorSystem: ActorSystem
  ): ActorRef = ActorHeHyphenMan[TCPServer](port, classOf[AckHandler], notify)
}

class TCPServer(
  port: Int,
  handlerClass: Class[_ <: TcpHandler],
  notify: Observer[GrindEvent]
)
  extends Actor
    with RxBus[GrindEvent]
    with Logging {
  import context.system

  // ctor
  registerObserver(notify)
  io.IO(Tcp) ! Bind(self, new InetSocketAddress("localhost", 0))

  /**
    * Receive method
    *
    * @return whatever
    */
  def receive: Receive = {
    case b @ Bound(localAddress) =>
      // do some logging or setup ...
      info(s"server is listening at $localAddress")

    case CommandFailed(_: Bind) ⇒ context stop self

    case c @ Connected(remote, local) =>
      info(s"[$local] incoming connection from $remote")

      val handler = TcpHandler.create(handlerClass = handlerClass, peerActor = sender(), peerAddress = remote, localAddress = local)

      sender ! Tcp.Register(handler, keepOpenOnPeerClosed = true)
      post(ConnectionEstablished(handler, remote, local))
  }
}
