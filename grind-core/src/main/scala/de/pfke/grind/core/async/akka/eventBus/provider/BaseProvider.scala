package de.pfke.grind.core.async.akka.eventBus.provider

import de.pfke.grind.core.async.akka.eventBus.GrindEventEnvelope
import de.pfke.grind.core.async.akka.eventBus.bus.GrindEventBus
import de.pfke.grind.core.async.event.GrindEvent

object BaseProvider {
  val BASE_CLASSIFIER = "grindEvent"

  def buildClassifier(topic: String, sub: String): String = s"$topic//$sub"
}

trait BaseProvider
  extends GrindEventBus {
  /**
   * A few report methods for easier traffic notifying.
   */
  protected def publishGrindEvent (
    classifier: Classifier,
    event: GrindEvent
  ): Unit = publish(GrindEventEnvelope(classifier = classifier, payload = event))
}

trait PendingBaseProvider
  extends BaseProvider {
  // fields
  private var _currentRunlevel: Option[(Classifier, GrindEvent)] = None

  /**
   * A few report methods for easier traffic notifying.
   */
  override protected def publishGrindEvent (
    classifier: Classifier,
    event: GrindEvent
  ): Unit = {
    if (_currentRunlevel.isDefined && (_currentRunlevel.get._1 == classifier)) {
      return
    }

    _currentRunlevel = Some((classifier, event))

    super.publishGrindEvent(classifier, event)
  }

  /**
    * Subscribe abfangen, um dem Subscriber unseren akuellen Runlevel mitzuteilen.
    */
  override def subscribe (
    subscriber: Subscriber,
    to: Classifier
  ): Boolean = {
    val result = super.subscribe(subscriber, to)

    _currentRunlevel match {
      case Some((classy, event)) if classy.startsWith(to) => publish(GrindEventEnvelope(classifier = classy, payload = event), subscriber)
      case _ =>
    }

    result
  }

}
