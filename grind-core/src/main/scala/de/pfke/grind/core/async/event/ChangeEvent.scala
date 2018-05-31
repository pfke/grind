package de.pfke.grind.core.async.event

sealed trait ChangeEvent extends GrindEvent

object ChangeEvent {
  /**
   * Property has changed.
   */
  case class PropertyChange(name: String, oldValue: Any, newValue: Any) extends ChangeEvent

  /**
   * Object is deleted event.
   */
  case class Deleted(that: Any, txt: String) extends ChangeEvent
}
