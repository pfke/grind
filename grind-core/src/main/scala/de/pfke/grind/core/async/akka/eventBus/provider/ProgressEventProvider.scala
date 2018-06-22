package de.pfke.grind.core.async.akka.eventBus.provider

import de.pfke.grind.core.async.akka.eventBus.bus.GrindEventBus
import de.pfke.grind.core.async.event.ProgressEventApi

object ProgressEventProvider {
  val PROGRESS_CLASSIFIER: String = BaseProvider.buildClassifier(BaseProvider.BASE_CLASSIFIER, "progress")

  val PROGRESS_START_CLASSIFIER: String = BaseProvider.buildClassifier(PROGRESS_CLASSIFIER, "start")
  val PROGRESS_PROGRESS_CLASSIFIER: String = BaseProvider.buildClassifier(PROGRESS_CLASSIFIER, "progress")
  val PROGRESS_FINISH_CLASSIFIER: String = BaseProvider.buildClassifier(PROGRESS_CLASSIFIER, "finish")
}

trait ProgressEventProvider
  extends BaseProvider { self: GrindEventBus =>
  import ProgressEventProvider._

  def subscribeToProgress (subscriber: Subscriber): Boolean = subscribe(subscriber, PROGRESS_CLASSIFIER)
  def subscribeToProgressStart (subscriber: Subscriber): Boolean = subscribe(subscriber, PROGRESS_START_CLASSIFIER)
  def subscribeToProgressProgress (subscriber: Subscriber): Boolean = subscribe(subscriber, PROGRESS_PROGRESS_CLASSIFIER)
  def subscribeToProgressFinish (subscriber: Subscriber): Boolean = subscribe(subscriber, PROGRESS_FINISH_CLASSIFIER)

  def publishProgressStart (id: Any, descr: Option[String] = None): Unit = publishGrindEvent(PROGRESS_START_CLASSIFIER, ProgressEventApi.Start(id, descr))
  def publishProgressProgress (id: Any, percent: Float, additional: Option[Any] = None): Unit = publishGrindEvent(PROGRESS_PROGRESS_CLASSIFIER, ProgressEventApi.Progress(id, percent, additional))
  def publishProgressFinish (id: Any): Unit = publishGrindEvent(PROGRESS_FINISH_CLASSIFIER, ProgressEventApi.Finish(id))
}
