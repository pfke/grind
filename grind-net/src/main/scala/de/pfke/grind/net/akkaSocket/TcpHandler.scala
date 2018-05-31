package de.pfke.grind.net.akkaSocket

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef, ActorSystem}
import akka.util.ByteString
import de.pfke.grind.core.async.event.GrindEvent
import de.pfke.grind.net.akkaSocket.AkkaTcpEvent.ConnectionTerminated

object TcpHandler {
  sealed trait tcphMsg
  sealed trait tcphReq extends tcphMsg // base trait for all requesting messages

  case object  close                  extends tcphReq // close connection
  case class   tx(data: ByteString)   extends tcphReq // send data to remote

  /**
    * Create the given handler.
    */
  def create (
    handlerClass: Class[_ <: TcpHandler],
    peerActor: ActorRef,
    peerAddress: InetSocketAddress,
    localAddress: InetSocketAddress,
    notifyFromPeerActor: Option[Observer[GrindEvent]] = None
  )(implicit context: ActorSystem): ActorRef = ActorHeHyphenMan.create(handlerClass, peerActor, peerAddress, notifyFromPeerActor)
}

abstract class TcpHandler(
  _peerActor: ActorRef,
  _peerAddress: InetSocketAddress,
  _notifyFromPeerActor: Option[Observer[GrindEvent]]
)
  extends Actor
    with RxBus[GrindEvent]
    with TrafficEventReporter {

  // ctor
  _notifyFromPeerActor match {
    case Some(t) => registerObserver(t)
    case None =>
  }

  // sign death pact: this actor terminates when connection breaks
  context watch peerActor

  protected def peerActor: ActorRef = _peerActor
  protected def peerAddress: InetSocketAddress = _peerAddress

  /**
    * Close connection because of this cause.
    */
  protected def closeConnection(msg: String)

  /**
    * Handle rx data.
    */
  protected def handleRx(data: ByteString)

  /**
    * Handle tx.
    */
  protected def handleTx(data: ByteString)

  /**
    * Actor is down
    */
  override def postStop(): Unit = {
    post(ConnectionTerminated)
  }
}
