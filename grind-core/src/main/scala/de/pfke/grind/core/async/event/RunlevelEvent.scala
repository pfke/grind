package de.pfke.grind.core.async.event

import java.time.Instant

sealed trait RunlevelEvent
  extends GrindEvent

object RunlevelEventApi {
  /**
    * Object is in init phase.
    */
  case class Init (timestamp: Instant = Instant.now()) extends RunlevelEvent

  /**
    * Object is up and ready to work.
    */
  case class Running (timestamp: Instant = Instant.now()) extends RunlevelEvent

  /**
    * Object is being shut down.
    */
  case class BeforeShutdown (timestamp: Instant = Instant.now()) extends RunlevelEvent

  /**
    * Object is shut down.
    */
  case class  AfterShutdown(cause: Any, timestamp: Instant = Instant.now()) extends RunlevelEvent

  /**
    * Object is in an error state.
    */
  case class  Failure(cause: Any, timestamp: Instant = Instant.now()) extends RunlevelEvent
}
