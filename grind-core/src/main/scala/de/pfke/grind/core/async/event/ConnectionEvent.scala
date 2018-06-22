package de.pfke.grind.core.async.event

import java.time.Instant

sealed trait ConnectionEvent
  extends GrindEvent

object ConnectionEventApi {
  /**
    * Object is in init phase.
    */
  case class NotConnected (timestamp: Instant = Instant.now()) extends ConnectionEvent

  /**
    * Object is up and ready to work.
    */
  case class Connecting (timestamp: Instant = Instant.now()) extends ConnectionEvent

  /**
    * Object is being shut down.
    */
  case class Connected (timestamp: Instant = Instant.now()) extends ConnectionEvent

  /**
    * Object is shut down.
    */
  case class Disconnecting(cause: Option[String], timestamp: Instant = Instant.now()) extends ConnectionEvent

  /**
    * Object is shut down.
    */
  case class ConnectionError(cause: String, timestamp: Instant = Instant.now()) extends ConnectionEvent
}
