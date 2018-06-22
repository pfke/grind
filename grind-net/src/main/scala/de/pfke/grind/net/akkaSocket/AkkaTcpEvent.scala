package de.pfke.grind.net.akkaSocket

import java.net.InetSocketAddress
import java.time.Instant

import akka.actor.ActorRef
import de.pfke.grind.core.async.akka.eventBus.provider.BaseProvider
import de.pfke.grind.core.async.event.GrindEvent

sealed trait AkkaTcpEvent extends GrindEvent

object AkkaTcpEventApi {
  case class ConnectionClosed(msg: String, timeStamp: Instant) extends AkkaTcpEvent // connection is closed
  case class ConnectionEstablished(handler: ActorRef, remote: InetSocketAddress, local: InetSocketAddress) extends AkkaTcpEvent // connection is up
  case class ConnectionFailed(cause: String) extends AkkaTcpEvent
  case object ConnectionTerminated extends AkkaTcpEvent // handler actor is terminated

  case class CommandFailed(cmd: String) extends AkkaTcpEvent // one tcp command has failed

  case object ResumeReading extends AkkaTcpEvent // resume reading
  case object SuspendReading extends AkkaTcpEvent // reading is suspended, because our send buffer is full
}

object AkkaTcpEventProvider {
  val AKKATCPEVENT_CLASSIFIER: String = BaseProvider.buildClassifier(BaseProvider.BASE_CLASSIFIER, "akkaTcpEvent")

  val AKKATCPEVENT_CONNECTIONCLOSED_CLASSIFIER: String = BaseProvider.buildClassifier(AKKATCPEVENT_CLASSIFIER, sub ="connectionClosed")
  val AKKATCPEVENT_CONNECTIONESTABLISHED_CLASSIFIER: String = BaseProvider.buildClassifier(AKKATCPEVENT_CLASSIFIER, sub ="connectionEstablished")
  val AKKATCPEVENT_CONNECTIONFAILED_CLASSIFIER: String = BaseProvider.buildClassifier(AKKATCPEVENT_CLASSIFIER, sub ="connectionFailed")
  val AKKATCPEVENT_CONNECTIONTERMINATED_CLASSIFIER: String = BaseProvider.buildClassifier(AKKATCPEVENT_CLASSIFIER, sub ="connectionTerminated")

  val AKKATCPEVENT_COMMANDFAILED_CLASSIFIER: String = BaseProvider.buildClassifier(AKKATCPEVENT_CLASSIFIER, sub ="commandFailed")

  val AKKATCPEVENT_RESUMEREADING_CLASSIFIER: String = BaseProvider.buildClassifier(AKKATCPEVENT_CLASSIFIER, sub ="resumeReading")
  val AKKATCPEVENT_SUSPENDREADING_CLASSIFIER: String = BaseProvider.buildClassifier(AKKATCPEVENT_CLASSIFIER, sub ="suspendReading")
}

trait AkkaTcpEventProvider
  extends BaseProvider {
  import AkkaTcpEventProvider._

//  def subscribeToTraffic (subscriber: Subscriber): Boolean = subscribe(subscriber, TrafficEventProvider.TRAFFIC_CLASSIFIER)
//  def subscribeToTrafficFaultyRx (subscriber: Subscriber): Boolean = subscribe(subscriber, TrafficEventProvider.TRAFFIC_FAULTYRX_CLASSIFIER)
//  def subscribeToTrafficRx (subscriber: Subscriber): Boolean = subscribe(subscriber, TrafficEventProvider.TRAFFIC_RX_CLASSIFIER)
//  def subscribeToTrafficTx (subscriber: Subscriber): Boolean = subscribe(subscriber, TrafficEventProvider.TRAFFIC_TX_CLASSIFIER)

  def publishConnectionClosed (
    msg: String
  ): Unit = publishGrindEvent(AKKATCPEVENT_CONNECTIONCLOSED_CLASSIFIER, AkkaTcpEventApi.ConnectionClosed(msg,  timeStamp = Instant.now()))

//  def publishConnectionEstablished (
//    handler: ActorRef,
//    local: InetSocketAddress,
//    remote: InetSocketAddress,
//  ): Unit = publishGrindEvent(AKKATCPEVENT_CONNECTIONCLOSED_CLASSIFIER, AkkaTcpEventApi.ConnectionClosed(msg,  timeStamp = Instant.now()))
}
