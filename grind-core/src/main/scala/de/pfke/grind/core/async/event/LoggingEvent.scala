package de.pfke.grind.core.async.event

import java.time.Instant

sealed trait LoggingEvent
  extends GrindEvent {
  def err: Any
  def timeStamp: Instant
}

object LoggingEventApi {
  /**
    * A really serious error.
    * Normal program flow will be disturbed.
    */
  case class Fatal (err: Any, timeStamp: Instant = Instant.now()) extends LoggingEvent

  /**
    * A really serious error.
    * Normal program flow will be disturbed.
    */
  case class Critical (err: Any, timeStamp: Instant = Instant.now()) extends LoggingEvent

  /**
    * A error.
    */
  case class Error (err: Any, timeStamp: Instant = Instant.now()) extends LoggingEvent

  /**
    * A warning. just feel you informed.
    */
  case class Warning (err: Any, timeStamp: Instant = Instant.now()) extends LoggingEvent

  /**
    * A warning. just feel you informed.
    */
  case class Debug (err: Any, timeStamp: Instant = Instant.now()) extends LoggingEvent

  /**
    * A warning. just feel you informed.
    */
  case class Info (err: Any, timeStamp: Instant = Instant.now()) extends LoggingEvent
}
