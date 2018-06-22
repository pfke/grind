package de.pfke.grind.net.akkaSocket

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef, ReceiveTimeout}
import akka.io.Tcp
import akka.io.Tcp._
import akka.util.ByteString
import de.pfke.grind.core.async.akka.eventBus.bus.GrindActorEventBus
import de.pfke.grind.core.async.akka.eventBus.provider.TrafficEventProvider

import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * This handler sends one packet to remote and wait for an ack and then sends the next packet.
  */
class AckHandler(
  _peerActor: ActorRef,
  _peerAddress: InetSocketAddress,
  _notifyFromPeerActor: Option[Observer[GrindEvent]]
)
  extends TcpHandler(_peerActor, _peerAddress, _notifyFromPeerActor)
    with GrindActorEventBus
    with TrafficEventProvider
{
  case object Ack extends Tcp.Event

  // fields
  private var storage = Vector.empty[ByteString]
  private var stored = 0L
  private var transferred = 0L
  private var isClosing = false

  private val maxStored = 100000000L
  private val highWatermark = maxStored * 5 / 10
  private val lowWatermark = maxStored * 3 / 10
  private var suspended = false

  /**
    * Our actor receive method while waiting for a connect..
    */
  def receive: Receive = {
    case Tcp.Aborted           => closeConnection("ack receive timeout")
    case Tcp.Closed            => closeConnection("user close")
    case Tcp.PeerClosed        => closeConnection("peer closed")
    case Tcp.Received(msg)     => handleRx(msg)

    case TcpHandler.close      => closeConnection("close request from user")
    case TcpHandler.tx(data)   => startTx(data)
  }

  /**
    * Our actor receive method while waiting for an ack.
    */
  def receiveWhileWaitingForAck: Actor.Receive = {
    case Ack                   => handleAcknowledge()

    case Tcp.CommandFailed(m)  => post(CommandFailed(m.toString))
    case Tcp.PeerClosed        => isClosing = true
    case Tcp.Received(msg)     => handleRx(msg)

    case TcpHandler.close      => peerActor ! Tcp.Close; context.unbecome()
    case TcpHandler.tx(msg)    => bufferThis(msg)

    case ReceiveTimeout        => peerActor ! Tcp.Abort; context.unbecome()
  }

  /**
    * Got an ack -> remove sent packet from queue and send a new one if there is one.
    */
  private def handleAcknowledge(): Unit = {
    require(storage.nonEmpty, "storage was empty")

    context.setReceiveTimeout(Duration.Undefined)

    val size = storage.head.size
    stored -= size
    transferred += size

    storage = storage drop 1

    if (suspended && stored < lowWatermark) {
      post(AkkaTcpEvent.ResumeReading)
      peerActor ! Tcp.ResumeReading
      suspended = false
    }

    if (storage.isEmpty) {
      if (isClosing) {
        closeConnection("peer closed while waiting for an ack")
      } else {
        context.unbecome()
      }
    } else {
      handleTx(storage.head)
    }
  }

  /**
    * Cache data before send.
    */
  private def bufferThis(
    data: ByteString
  ): Unit = {
    storage :+= data
    stored += data.size

    if (stored > maxStored) {
      closeConnection(s"drop connection to [$peerAddress] (buffer overrun)")
    } else if (stored > highWatermark) {
      post(AkkaTcpEvent.SuspendReading)
      peerActor ! Tcp.SuspendReading
      suspended = true
    }
  }

  /**
    * Close connection because of this cause.
    */
  protected def closeConnection(
    msg: String
  ) {
    post(AkkaTcpEvent.ConnectionClosed(s"$msg: transferred ${transferred.asHumanReadableSiByte} from/to [$peerAddress]"))
    context stop self
  }

  /**
    * Handle rx data.
    */
  protected def handleRx(data: ByteString): Unit = publishTrafficRx(data = data)

  /**
    * Handle tx.
    */
  protected def handleTx(
    data: ByteString
  ) {
    peerActor ! Write(data, Ack)

    publishTrafficTx(data = data)
    context.setReceiveTimeout(1 second)
  }

  /**
    * Send data and wait for an ack.
    */
  private def startTx(
    data: ByteString
  ): Unit = {
    bufferThis(data)
    handleTx(data)

    // switch context while waiting for acknowledge
    context.become(receiveWhileWaitingForAck, discardOld = false)
  }
}
