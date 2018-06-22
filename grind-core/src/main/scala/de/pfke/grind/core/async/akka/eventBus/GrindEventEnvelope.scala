package de.pfke.grind.core.async.akka.eventBus

import de.pfke.grind.core.async.event.GrindEvent

private[eventBus] final case class GrindEventEnvelope (
  classifier: String,
  payload: GrindEvent
)
