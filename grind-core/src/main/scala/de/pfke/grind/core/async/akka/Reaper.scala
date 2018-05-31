package de.pfke.grind.core.async.akka

import akka.actor.{Actor, ActorRef, Terminated}

import scala.collection.mutable.ArrayBuffer

object Reaper {
  // Used by others to register an Actor for watching
  case class WatchMe(ref: ActorRef)
}

trait Reaper extends Actor {
  import Reaper._

  // Keep track of what we're watching
  private val _watchedActors = ArrayBuffer.empty[ActorRef]

  // Derivations need to implement this method. It's the
  // hook that's called when everything's dead
  def allSoulsReaped(): Unit

  // Watch and check for termination
  final def receive: Receive = {
    case WatchMe(ref) =>
      context.watch(ref)
      _watchedActors += ref
    case Terminated(ref) =>
      _watchedActors -= ref
      if (_watchedActors.isEmpty) allSoulsReaped()
  }
}
