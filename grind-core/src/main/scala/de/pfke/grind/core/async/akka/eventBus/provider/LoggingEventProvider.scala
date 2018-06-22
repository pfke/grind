package de.pfke.grind.core.async.akka.eventBus.provider

import de.pfke.grind.core.async.akka.eventBus.bus.GrindEventBus
import de.pfke.grind.core.async.event.LoggingEventApi

object LoggingEventProvider {
  val LOGGING_CLASSIFIER: String = BaseProvider.buildClassifier(BaseProvider.BASE_CLASSIFIER, "error")

  val LOGGING_DEBUG_CLASSIFIER: String = BaseProvider.buildClassifier(LOGGING_CLASSIFIER, "debug")
  val LOGGING_INFO_CLASSIFIER: String = BaseProvider.buildClassifier(LOGGING_DEBUG_CLASSIFIER, "info")
  val LOGGING_WARNING_CLASSIFIER: String = BaseProvider.buildClassifier(LOGGING_INFO_CLASSIFIER, "warning")
  val LOGGING_ERROR_CLASSIFIER: String = BaseProvider.buildClassifier(LOGGING_WARNING_CLASSIFIER, "error")
  val LOGGING_CRITICAL_CLASSIFIER: String = BaseProvider.buildClassifier(LOGGING_ERROR_CLASSIFIER, "critical")
  val LOGGING_FATAL_CLASSIFIER: String = BaseProvider.buildClassifier(LOGGING_CRITICAL_CLASSIFIER, "fatal")
}

trait LoggingEventProvider
  extends BaseProvider { self: GrindEventBus =>
  import LoggingEventProvider._

  def subscribeToLogging (subscriber: Subscriber): Boolean = subscribe(subscriber, LOGGING_CLASSIFIER)
  def subscribeToLoggingDebug (subscriber: Subscriber): Boolean = subscribe(subscriber, LOGGING_DEBUG_CLASSIFIER)
  def subscribeToLoggingInfo (subscriber: Subscriber): Boolean = subscribe(subscriber, LOGGING_INFO_CLASSIFIER)
  def subscribeToLoggingWarning (subscriber: Subscriber): Boolean = subscribe(subscriber, LOGGING_WARNING_CLASSIFIER)
  def subscribeToLoggingError (subscriber: Subscriber): Boolean = subscribe(subscriber, LOGGING_ERROR_CLASSIFIER)
  def subscribeToLoggingCritical (subscriber: Subscriber): Boolean = subscribe(subscriber, LOGGING_CRITICAL_CLASSIFIER)
  def subscribeToLoggingFatal (subscriber: Subscriber): Boolean = subscribe(subscriber, LOGGING_FATAL_CLASSIFIER)

  def publishLoggingFatal(msg: Any): Unit = publishGrindEvent(LOGGING_FATAL_CLASSIFIER, LoggingEventApi.Fatal(msg))
  def publishLoggingCritical(msg: Any): Unit = publishGrindEvent(LOGGING_CRITICAL_CLASSIFIER, LoggingEventApi.Critical(msg))
  def publishLoggingError(msg: Any): Unit = publishGrindEvent(LOGGING_ERROR_CLASSIFIER, LoggingEventApi.Error(msg))
  def publishLoggingWarning(msg: Any): Unit = publishGrindEvent(LOGGING_WARNING_CLASSIFIER, LoggingEventApi.Warning(msg))
  def publishLoggingInfo(msg: Any): Unit = publishGrindEvent(LOGGING_INFO_CLASSIFIER, LoggingEventApi.Info(msg))
  def publishLoggingDebug(msg: Any): Unit = publishGrindEvent(LOGGING_DEBUG_CLASSIFIER, LoggingEventApi.Debug(msg))
}
