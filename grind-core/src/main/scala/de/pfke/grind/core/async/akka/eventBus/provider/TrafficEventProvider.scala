package de.pfke.grind.core.async.akka.eventBus.provider

import java.time.Instant

import de.pfke.grind.core.async.event.TrafficEventApi

object TrafficEventProvider {
  val TRAFFIC_CLASSIFIER: String = BaseProvider.buildClassifier(BaseProvider.BASE_CLASSIFIER, "traffic")

  val TRAFFIC_FAULTYRX_CLASSIFIER: String = BaseProvider.buildClassifier(TRAFFIC_CLASSIFIER, "faultyRx")
  val TRAFFIC_RX_CLASSIFIER: String = BaseProvider.buildClassifier(TRAFFIC_CLASSIFIER, "rx")
  val TRAFFIC_TX_CLASSIFIER: String = BaseProvider.buildClassifier(TRAFFIC_CLASSIFIER, "tx")
}

trait TrafficEventProvider
  extends BaseProvider {
  import TrafficEventProvider._

  def subscribeToTraffic (subscriber: Subscriber): Boolean = subscribe(subscriber, TrafficEventProvider.TRAFFIC_CLASSIFIER)
  def subscribeToTrafficFaultyRx (subscriber: Subscriber): Boolean = subscribe(subscriber, TrafficEventProvider.TRAFFIC_FAULTYRX_CLASSIFIER)
  def subscribeToTrafficRx (subscriber: Subscriber): Boolean = subscribe(subscriber, TrafficEventProvider.TRAFFIC_RX_CLASSIFIER)
  def subscribeToTrafficTx (subscriber: Subscriber): Boolean = subscribe(subscriber, TrafficEventProvider.TRAFFIC_TX_CLASSIFIER)

  def publishTrafficRx (data: Any, timeStamp: Instant = Instant.now()): Unit = publishGrindEvent(TRAFFIC_RX_CLASSIFIER, TrafficEventApi.Rx(data, timeStamp))
  def publishTrafficFaultyRx (msg: Any, timeStamp: Instant = Instant.now()): Unit = publishGrindEvent(TRAFFIC_FAULTYRX_CLASSIFIER, TrafficEventApi.FaultyRx(msg, timeStamp))
  def publishTrafficTx (msg: Any, timeStamp: Instant = Instant.now()): Unit = publishGrindEvent(TRAFFIC_TX_CLASSIFIER, TrafficEventApi.Tx(msg, timeStamp))
}
