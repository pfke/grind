package de.pfke.grind.core.async.event

import java.time.Instant

sealed trait TrafficEvent extends GrindEvent {
  def data: Any
  def timeStamp: Instant
}

object TrafficEvent {
  /**
   * Message received.
   */
  case class Rx (data: Any, timeStamp: Instant = Instant.now()) extends TrafficEvent

  /**
   * Faulty message received.
   */
  case class FaultyRx (data: Any, timeStamp: Instant = Instant.now()) extends TrafficEvent

  /**
   * Message transmitted.
   */
  case class Tx (data: Any, timeStamp: Instant = Instant.now()) extends TrafficEvent
}
