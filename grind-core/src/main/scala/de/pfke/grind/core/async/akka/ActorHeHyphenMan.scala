/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Heiko Blobner
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.pfke.grind.core.async.akka

import java.util.UUID

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import de.pfke.grind.core.async.akka.Reaper.WatchMe

import scala.concurrent.ExecutionContext.Implicits.global
import scala.reflect.ClassTag
import scala.util.{Failure, Success}

object ActorHeHyphenMan {
  // fields
  private val _reaperActorCache = new collection.mutable.HashMap[ActorSystem, ActorRef]() // cache for all existing reapers

  /**
    * Create a actor ref and registers on the shutdown watcher.
    *
    * @param args is passed to newly created actor
    * @param classTag is the type information
    * @param actorSystem the actor is created within this system
    * @tparam A is the type information
    * @return ActorRef of our new actor
    */
  def apply[A <: Actor] (
    args: Any*
  ) (
    implicit
    classTag: ClassTag[A],
    actorSystem: ActorSystem
  ): ActorRef = ActorHeHyphenMan.create(classTag.runtimeClass.asInstanceOf[Class[A]], args:_*)

  /**
    * Create a actor ref and registers on the shutdown watcher.
    *
    * @param args is passed to newly created actor
    * @param actorSystem the actor is created within this system
    * @return ActorRef of our new actor
    */
  def create (
    className: String,
    args: Any*
  ) (
    implicit
    actorSystem: ActorSystem
  ): ActorRef = ActorHeHyphenMan.create(Class.forName(className).asInstanceOf[Class[Actor]], args:_*)

  /**
    * Create a actor ref and registers on the shutdown watcher.
    *
    * @param clazz is the actor class
    * @param args is passed to newly created actor
    * @param actorSystem the actor is created within this system
    * @return ActorRef of our new actor
    */
  def create[A <: Actor: ClassTag] (
    clazz: Class[A],
    args: Any*
  ) (
    implicit
    actorSystem: ActorSystem
  ): ActorRef = ActorHeHyphenMan.create(Props(clazz, args:_*))

  /**
    * Create a actor ref and registers on the shutdown watcher.
    *
    * @param props is the actor properties
    * @param actorSystem the actor is created within this system
    * @return ActorRef of our new actor
    */
  def create (
    props: Props
  ) (
    implicit
    actorSystem: ActorSystem
  ): ActorRef = watchShutdown(actorSystem.actorOf(props, s"${props.actorClass().getName}_${UUID.randomUUID()}"))

  /**
    * Set actor on watch list. If no actor is alive anymore, the actor system will be shut down.
    */
  def watchShutdown (
    actor: ActorRef
  ) (
    implicit
    actorSystem: ActorSystem
  ): ActorRef = {
    getReaper() ! WatchMe(actor)
    actor
  }

  /**
    * Return the reaper for the given actor system.
    * Register actor system for removing.
    */
  private def getReaper () (
    implicit
    actorSystem: ActorSystem
  ) = {
    _reaperActorCache synchronized {
      val registerForRemoving = !_reaperActorCache.contains(actorSystem)

      val reaper = _reaperActorCache.getOrElseUpdate(actorSystem, actorSystem.actorOf(Props(classOf[ProductionReaper])))

      if (registerForRemoving) {
        actorSystem.whenTerminated onComplete {
          case Success(t) => _reaperActorCache -= actorSystem
          case Failure(t) => _reaperActorCache -= actorSystem // error(s"ActorSystem '${actorSystem.name}' died with an error: '$t'")
        }
      }

      reaper
    }
  }
}

object ActorHeHyphenManIncludes
  extends ActorHeHyphenManIncludes

trait ActorHeHyphenManIncludes {
  implicit class ActorHeHyphenManIncludes_fromActorRef (
    in: ActorRef
  ) (
    implicit
    actorSystem: ActorSystem
  ) {
    def watchShutdown: ActorRef = ActorHeHyphenMan.watchShutdown(in)
  }

  implicit class ActorHeHyphenManIncludes_fromClass[A <: Actor: ClassTag] (
    in: Class[A]
  ) (
    implicit
    actorSystem: ActorSystem
  ) {
    def createActor(args: Any*): ActorRef = ActorHeHyphenMan.create(in, args:_*)
  }

  implicit class ActorHeHyphenManIncludes_fromClassName (
    in: String
  ) (
    implicit
    actorSystem: ActorSystem
  ) {
    private val clazz = Class.forName(in)
    require(clazz.isInstanceOf[Actor], message = s"pass class is not of type Actor ($in)")

    def createActor(args: Any*) = ActorHeHyphenMan.create(clazz.asInstanceOf[Class[Actor]], args:_*)
  }
}
