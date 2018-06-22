package de.pfke.grind.net.akkaSocket

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef, ActorSystem}
import akka.io
import akka.io.Tcp
import akka.io.Tcp._
import de.pfke.grind.core.async.akka.ActorHeHyphenMan

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
object TcpServer{
  /**
    * Listen on this port.
    *
    * You will get a TcpHandler.Established() event.
    */
  def listen (
    port: Int
  ) (
    implicit
    actorSystem: ActorSystem
  ): ActorRef = ActorHeHyphenMan[TcpServer](port, classOf[AckHandler])
}

class TcpServer(
  port: Int,
  handlerClass: Class[_ <: TcpHandler]
)
  extends Actor
{
  import context.system

  // ctor
  io.IO(Tcp) ! Bind(self, new InetSocketAddress("localhost", 0))

  /**
    * Receive method
    */
  def receive: Receive = {
    case b @ Bound(localAddress) =>
    //      info(s"server is listening at $localAddress")

    case CommandFailed(_: Bind) ⇒ context stop self

    case c @ Connected(remote, local) =>
      //      info(s"[$local] incoming connection from $remote")

      val handler = TcpHandler.create(handlerClass = handlerClass, peerActor = sender(), peerAddress = remote, localAddress = local)

      sender ! Tcp.Register(handler, keepOpenOnPeerClosed = true)
    //      post(ConnectionEstablished(handler, remote, local))
  }
}
