package de.pfke.grind.core.async.akka.eventBus.bus

import akka.actor.ActorRef
import akka.event.{EventBus, SubchannelClassification}
import akka.util.Subclassification
import de.pfke.grind.core.async.akka.eventBus.GrindEventEnvelope
import de.pfke.grind.core.async.event.GrindEvent

class StartsWithSubclassification
  extends Subclassification[String] {
  override def isEqual(x: String, y: String): Boolean = x == y
  override def isSubclass(x: String, y: String): Boolean = x.startsWith(y)
}

object GrindEventBus {
  type Event = GrindEventEnvelope
  type Classifier = String
  type SubscriberIsOp = PartialFunction[GrindEvent, Unit]
  type SubscriberIsActor = ActorRef
}

trait GrindEventBus
  extends EventBus
    with SubchannelClassification
{
  override type Event = GrindEventBus.Event
  override type Classifier = GrindEventBus.Classifier

  override protected implicit def subclassification: Subclassification[Classifier] = new StartsWithSubclassification()

  override protected def classify (event: Event): Classifier = event.classifier
}


