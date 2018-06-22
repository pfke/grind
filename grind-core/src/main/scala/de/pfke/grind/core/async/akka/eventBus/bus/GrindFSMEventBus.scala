package de.pfke.grind.core.async.akka.eventBus.bus

import akka.actor.ActorRef
import de.pfke.grind.core.async.akka.eventBus.msg._

class GrindFSMEventBus (
  a: ActorRef
)
  extends GrindEventBus
{
  override type Subscriber = GrindEventBus.SubscriberIsActor

  override def publish (
    event: Event,
    subscriber: Subscriber
  ): Unit = subscriber ! event.payload

  def forward (
    sender: ActorRef,
    msg: SubscribeRequest
  ): Unit = {
    msg match {
      case SubscribeMe(subscriber, toClassifier) => sender ! subscribeMe(subscriber, toClassifier)
    }
  }

  def forward (
    sender: ActorRef,
    msg: UnSubscribeRequest
  ): Unit = {
    msg match {
      case UnSubscribeMe(subscriber) => sender ! unSubscribeMe(subscriber)
      case UnSubscribeMeFromClassifier(subscriber, fromClassifier) => sender ! unSubscribeMe(subscriber, fromClassifier)
    }
  }

  private def subscribeMe (
    subscriber: Subscriber,
    toClassifier: Classifier
  ): SubscribeResponse = {
    if (subscribe(subscriber, toClassifier))
      SubscribeMeSuccess(a, toClassifier)
    else
      SubscribeMeFailure(a, toClassifier)
  }

  private def unSubscribeMe (
    subscriber: Subscriber
  ): UnSubscribeResponse = {
    unsubscribe(subscriber)
    UnSubscribeMeSuccess(a, None)
  }

  private def unSubscribeMe (
    subscriber: Subscriber,
    fromClassifier: Classifier
  ): UnSubscribeResponse = {
    if (unsubscribe(subscriber, fromClassifier))
      UnSubscribeMeSuccess(a, Some(fromClassifier))
    else
      UnSubscribeMeFailure(a, Some(fromClassifier))
  }
}
