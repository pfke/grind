package de.pfke.grind.core.async.akka.eventBus.msg

import akka.actor.ActorRef
import de.pfke.grind.core.async.akka.eventBus.bus.GrindEventBus

sealed trait EventBusMessage

sealed trait SubscribeMsg extends EventBusMessage
sealed trait SubscribeRequest extends SubscribeMsg
sealed trait SubscribeResponse extends SubscribeMsg

case class SubscribeMe (
  subscriber: ActorRef,
  toClassifier: GrindEventBus.Classifier
) extends SubscribeRequest

case class SubscribeMeSuccess (
  eventProvider: ActorRef,
  toClassifier: GrindEventBus.Classifier
) extends SubscribeResponse

case class SubscribeMeFailure (
  eventProvider: ActorRef,
  toClassifier: GrindEventBus.Classifier
) extends SubscribeResponse

sealed trait UnSubscribeMsg extends EventBusMessage
sealed trait UnSubscribeRequest extends UnSubscribeMsg
sealed trait UnSubscribeResponse extends UnSubscribeMsg

case class UnSubscribeMe (
  subscriber: ActorRef
) extends UnSubscribeRequest

case class UnSubscribeMeFromClassifier (
  subscriber: ActorRef,
  fromClassifier: GrindEventBus.Classifier
) extends UnSubscribeRequest

case class UnSubscribeMeSuccess (
  eventProvider: ActorRef,
  fromClassifier: Option[GrindEventBus.Classifier]
) extends UnSubscribeResponse

case class UnSubscribeMeFailure (
  eventProvider: ActorRef,
  fromClassifier: Option[GrindEventBus.Classifier]
) extends UnSubscribeResponse
