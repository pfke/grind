package de.pfke.grind.core.async.akka.eventBus.provider

import java.time.Instant

import de.pfke.grind.core.async.event.RunlevelEventApi

object RunlevelEventProvider {
  val RUNLEVEL_CLASSIFIER: String = BaseProvider.buildClassifier(BaseProvider.BASE_CLASSIFIER, "runlevel")

  val RUNLEVEL_INIT_CLASSIFIER: String = BaseProvider.buildClassifier(RUNLEVEL_CLASSIFIER, "init")
  val RUNLEVEL_RUNNING_CLASSIFIER: String = BaseProvider.buildClassifier(RUNLEVEL_CLASSIFIER, "running")
  val RUNLEVEL_BEFORESHUTDOWN_CLASSIFIER: String = BaseProvider.buildClassifier(RUNLEVEL_CLASSIFIER, "beforeshutdown")
  val RUNLEVEL_AFTERSHUTDOWN_CLASSIFIER: String = BaseProvider.buildClassifier(RUNLEVEL_CLASSIFIER, "aftershutdown")
  val RUNLEVEL_FAILURE_CLASSIFIER: String = BaseProvider.buildClassifier(RUNLEVEL_CLASSIFIER, "failure")
}

trait RunlevelEventProvider
  extends PendingBaseProvider {
  import RunlevelEventProvider._

  // ctor
  publishRunlevelInit()

  def subscribeToRunLevel (subscriber: Subscriber): Boolean = subscribe(subscriber, RUNLEVEL_CLASSIFIER)
  def subscribeToRunLevelInit (subscriber: Subscriber): Boolean = subscribe(subscriber, RUNLEVEL_INIT_CLASSIFIER)
  def subscribeToRunLevelRunning (subscriber: Subscriber): Boolean = subscribe(subscriber, RUNLEVEL_RUNNING_CLASSIFIER)
  def subscribeToRunLevelBeforeShutdown (subscriber: Subscriber): Boolean = subscribe(subscriber, RUNLEVEL_BEFORESHUTDOWN_CLASSIFIER)
  def subscribeToRunLevelAfterShutdown (subscriber: Subscriber): Boolean = subscribe(subscriber, RUNLEVEL_AFTERSHUTDOWN_CLASSIFIER)
  def subscribeToRunLevelFailure (subscriber: Subscriber): Boolean = subscribe(subscriber, RUNLEVEL_FAILURE_CLASSIFIER)

  def publishRunlevelInit (
    timestamp: Instant = Instant.now()
  ): Unit = publishGrindEvent(RUNLEVEL_INIT_CLASSIFIER, RunlevelEventApi.Init(timestamp = timestamp))

  def publishRunlevelRunning (
    timestamp: Instant = Instant.now()
  ): Unit = publishGrindEvent(RUNLEVEL_RUNNING_CLASSIFIER, RunlevelEventApi.Running(timestamp = timestamp))

  def publishRunlevelBeforeShutdown (
    timestamp: Instant = Instant.now()
  ): Unit = publishGrindEvent(RUNLEVEL_BEFORESHUTDOWN_CLASSIFIER, RunlevelEventApi.BeforeShutdown(timestamp = timestamp))

  def publishRunlevelAfterShutdown (
    cause: Any = "",
    timestamp: Instant = Instant.now()
  ): Unit = publishGrindEvent(RUNLEVEL_AFTERSHUTDOWN_CLASSIFIER, RunlevelEventApi.AfterShutdown(cause = cause, timestamp = timestamp))

  def publishRunlevelFailure (
    cause: Any = "",
    timestamp: Instant = Instant.now()
  ): Unit = publishGrindEvent(RUNLEVEL_FAILURE_CLASSIFIER, RunlevelEventApi.Failure(cause = cause, timestamp = timestamp))
}
