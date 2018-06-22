package de.pfke.grind.core.async.akka

import akka.actor.Actor

/**
  * Trait von dem sich Actor ableiten können.
  * Alle Empfangenen Nachrichten werden an alle Instanzen weitergeleitet:
  *
  * class Actor1 extends DaisyChainedActor {
  *   receiver {
  *     case PoisonPill => // to was
  *   }
  * }
  *
  * class Actor2 extends Actor1 {
  *   receiver {
  *     case PoisonPill => // to was
  *   }
  * }
  */
trait DaisyChainedActor
  extends Actor {
  var receivers: Actor.Receive = Actor.emptyBehavior
  def receiver(next: Actor.Receive) { receivers = receivers orElse next }
  def receive: Receive = receivers // Actor.receive definition
}
