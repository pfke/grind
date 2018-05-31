package de.pfke.grind.core.async.event

sealed trait ProgressEvent extends GrindEvent

object ProgressEvent {
  case class Start (id: Any, descr: Option[String]) extends ProgressEvent
  case class Progress (id: Any, percent: Float, additional: Option[Any]) extends ProgressEvent
  case class Finish (id: Any) extends ProgressEvent
}
