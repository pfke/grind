package de.pfke.grind.core.async.akka.eventBus.bus

trait GrindCallbackEventBus
  extends GrindEventBus
{
  override type Subscriber = GrindEventBus.SubscriberIsOp

  override protected def publish (
    event: Event,
    subscriber: Subscriber
  ): Unit = subscriber(event.payload)
}
