package de.pfke.grind.core.async.akka.eventBus.bus

import de.pfke.grind.core.async.akka.DaisyChainedActor
import de.pfke.grind.core.async.akka.eventBus.msg._

trait GrindActorEventBus
  extends DaisyChainedActor
    with GrindEventBus
{
  override type Subscriber = GrindEventBus.SubscriberIsActor

  override protected def publish (
    event: Event,
    subscriber: Subscriber
  ): Unit = subscriber ! event.payload

  receiver {
    case SubscribeMe(subscriber, toClassifier) => sender() ! subscribeMe(subscriber, toClassifier)
    case UnSubscribeMe(subscriber) => sender() ! unSubscribeMe(subscriber)
    case UnSubscribeMeFromClassifier(subscriber, fromClassifier) => sender() ! unSubscribeMe(subscriber, fromClassifier)
  }

  private def subscribeMe (
    subscriber: Subscriber,
    toClassifier: Classifier
  ): SubscribeResponse = {
    if (subscribe(subscriber, toClassifier))
      SubscribeMeSuccess(self, toClassifier)
    else
      SubscribeMeFailure(self, toClassifier)
  }

  private def unSubscribeMe (
    subscriber: Subscriber
  ): UnSubscribeResponse = {
    unsubscribe(subscriber)
    UnSubscribeMeSuccess(self, None)
  }

  private def unSubscribeMe (
    subscriber: Subscriber,
    fromClassifier: Classifier
  ): UnSubscribeResponse = {
    if (unsubscribe(subscriber, fromClassifier))
      UnSubscribeMeSuccess(self, Some(fromClassifier))
    else
      UnSubscribeMeFailure(self, Some(fromClassifier))
  }
}
