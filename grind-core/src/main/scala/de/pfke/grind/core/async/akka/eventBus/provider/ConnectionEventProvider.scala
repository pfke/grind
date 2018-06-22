package de.pfke.grind.core.async.akka.eventBus.provider

import java.time.Instant

import de.pfke.grind.core.async.event.ConnectionEventApi

object ConnectionEventProvider {
  val CONNECTION_CLASSIFIER: String = BaseProvider.buildClassifier(BaseProvider.BASE_CLASSIFIER, "connection")

  val CONNECTION_NOTCONNECTED_CLASSIFIER: String = BaseProvider.buildClassifier(CONNECTION_CLASSIFIER, "notConnected")
  val CONNECTION_CONNECTING_CLASSIFIER: String = BaseProvider.buildClassifier(CONNECTION_CLASSIFIER, "connecting")
  val CONNECTION_CONNECTED_CLASSIFIER: String = BaseProvider.buildClassifier(CONNECTION_CLASSIFIER, "connected")
  val CONNECTION_DISCONNECTING_CLASSIFIER: String = BaseProvider.buildClassifier(CONNECTION_CLASSIFIER, "disconnecting")
  val CONNECTION_CONNECTIONERROR_CLASSIFIER: String = BaseProvider.buildClassifier(CONNECTION_CLASSIFIER, "connectionError")
}

trait ConnectionEventProvider
  extends PendingBaseProvider {
  import ConnectionEventProvider._

  // ctor
  publishConnectionNotConnected()

  def subscribeToConnection (subscriber: Subscriber): Boolean = subscribe(subscriber, CONNECTION_CLASSIFIER)
  def subscribeToConnectionNotConnected (subscriber: Subscriber): Boolean = subscribe(subscriber, CONNECTION_NOTCONNECTED_CLASSIFIER)
  def subscribeToConnectionConnecting (subscriber: Subscriber): Boolean = subscribe(subscriber, CONNECTION_CONNECTING_CLASSIFIER)
  def subscribeToConnectionConnected (subscriber: Subscriber): Boolean = subscribe(subscriber, CONNECTION_CONNECTED_CLASSIFIER)
  def subscribeToConnectionDisconnecting (subscriber: Subscriber): Boolean = subscribe(subscriber, CONNECTION_DISCONNECTING_CLASSIFIER)
  def subscribeToConnectionConnectionError (subscriber: Subscriber): Boolean = subscribe(subscriber, CONNECTION_CONNECTIONERROR_CLASSIFIER)

  def publishConnectionNotConnected (
    timestamp: Instant = Instant.now()
  ): Unit = publishGrindEvent(CONNECTION_NOTCONNECTED_CLASSIFIER, ConnectionEventApi.NotConnected(timestamp = timestamp))

  def publishConnectionConnecting (
    timestamp: Instant = Instant.now()
  ): Unit = publishGrindEvent(CONNECTION_CONNECTING_CLASSIFIER, ConnectionEventApi.Connecting(timestamp = timestamp))

  def publishConnectionConnected (
    timestamp: Instant = Instant.now()
  ): Unit = publishGrindEvent(CONNECTION_CONNECTED_CLASSIFIER, ConnectionEventApi.Connected(timestamp = timestamp))

  def publishConnectionDisconnecting (
    cause: Option[String] = None,
    timestamp: Instant = Instant.now()
  ): Unit = publishGrindEvent(CONNECTION_DISCONNECTING_CLASSIFIER, ConnectionEventApi.Disconnecting(cause = cause, timestamp = timestamp))

  def publishConnectionConnectionError (
    cause: String,
    timestamp: Instant = Instant.now()
  ): Unit = publishGrindEvent(CONNECTION_CONNECTIONERROR_CLASSIFIER, ConnectionEventApi.ConnectionError(cause = cause, timestamp = timestamp))
}
