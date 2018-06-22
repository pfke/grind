package de.pfke.grind.core.async.akka.eventBus.provider

import de.pfke.grind.core.async.akka.eventBus.bus.GrindEventBus
import de.pfke.grind.core.async.event.ChangeEventApi.{Deleted, PropertyChange}

object ChangeEventProvider {
  val CHANGE_CLASSIFIER: String = BaseProvider.buildClassifier(BaseProvider.BASE_CLASSIFIER, "change")

  val CHANGE_PROPCHANGED_CLASSIFIER: String = BaseProvider.buildClassifier(CHANGE_CLASSIFIER, "propChanged")
  val CHANGE_DELETED_CLASSIFIER: String = BaseProvider.buildClassifier(CHANGE_CLASSIFIER, "deleted")
}

trait ChangeEventProvider
  extends BaseProvider { self: GrindEventBus =>
  import ChangeEventProvider._

  def subscribeToChange (subscriber: Subscriber): Boolean = subscribe(subscriber, CHANGE_CLASSIFIER)
  def subscribeToChangePropChanged (subscriber: Subscriber): Boolean = subscribe(subscriber, CHANGE_PROPCHANGED_CLASSIFIER)
  def subscribeToChangeDeleted (subscriber: Subscriber): Boolean = subscribe(subscriber, CHANGE_DELETED_CLASSIFIER)

  def publishChangeProp (name: String, oldValue: Any, newValue: Any): Unit = publishGrindEvent(CHANGE_PROPCHANGED_CLASSIFIER, PropertyChange(name, oldValue, newValue))
  def publishChangeDelete (that: Any, txt: String): Unit = publishGrindEvent(CHANGE_PROPCHANGED_CLASSIFIER, Deleted(that, txt))
}
