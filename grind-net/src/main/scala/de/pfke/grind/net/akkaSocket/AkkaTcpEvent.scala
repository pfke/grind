package de.pfke.grind.net.akkaSocket

import java.net.InetSocketAddress

import akka.actor.ActorRef
import de.pfke.grind.core.async.event.GrindEvent

sealed trait AkkaTcpEvent extends GrindEvent

object AkkaTcpEvent {
  case class ConnectionClosed(msg: String) extends AkkaTcpEvent // connection is closed
  case class ConnectionEstablished(handler: ActorRef, remote: InetSocketAddress, local: InetSocketAddress) extends AkkaTcpEvent // connection is up
  case class ConnectionFailed(cause: String) extends AkkaTcpEvent
  case object ConnectionTerminated extends AkkaTcpEvent // handler actor is terminated

  case class CommandFailed(cmd: String) extends AkkaTcpEvent // one tcp command has failed

  case object ResumeReading extends AkkaTcpEvent // resume reading
  case object SuspendReading extends AkkaTcpEvent // reading is suspended, because our send buffer is full
}
