package de.pfke.grind.core.async.akka.eventBus.provider

import de.pfke.grind.core.async.akka.eventBus.bus.{GrindActorEventBus, GrindCallbackEventBus, GrindEventBus}

object ProviderFactory {
  //---
  def subscribe (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor, classifier: GrindEventBus.Classifier): Boolean = provider.subscribe(subscriber, classifier)

  def subscribeToChange (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.subscribe(subscriber, ChangeEventProvider.CHANGE_CLASSIFIER)
  def subscribeToChangePropChanged (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.subscribe(subscriber, ChangeEventProvider.CHANGE_PROPCHANGED_CLASSIFIER)
  def subscribeToChangeDeleted (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.subscribe(subscriber, ChangeEventProvider.CHANGE_DELETED_CLASSIFIER)

  def subscribeToLog (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.subscribe(subscriber, LoggingEventProvider.LOGGING_CLASSIFIER)
  def subscribeToLogDebug (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.subscribe(subscriber, LoggingEventProvider.LOGGING_DEBUG_CLASSIFIER)
  def subscribeToLogInfo (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.subscribe(subscriber, LoggingEventProvider.LOGGING_INFO_CLASSIFIER)
  def subscribeToLogWarning (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.subscribe(subscriber, LoggingEventProvider.LOGGING_WARNING_CLASSIFIER)
  def subscribeToLogError (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.subscribe(subscriber, LoggingEventProvider.LOGGING_ERROR_CLASSIFIER)
  def subscribeToLogCritical (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.subscribe(subscriber, LoggingEventProvider.LOGGING_CRITICAL_CLASSIFIER)
  def subscribeToLogFatal (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.subscribe(subscriber, LoggingEventProvider.LOGGING_FATAL_CLASSIFIER)

  def subscribeToProgress (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.subscribe(subscriber, ProgressEventProvider.PROGRESS_CLASSIFIER)
  def subscribeToProgressStart (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.subscribe(subscriber, ProgressEventProvider.PROGRESS_START_CLASSIFIER)
  def subscribeToProgressProgress (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.subscribe(subscriber, ProgressEventProvider.PROGRESS_PROGRESS_CLASSIFIER)
  def subscribeToProgressFinish (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.subscribe(subscriber, ProgressEventProvider.PROGRESS_FINISH_CLASSIFIER)

  def subscribeToRunLevel (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.subscribe(subscriber, RunlevelEventProvider.RUNLEVEL_CLASSIFIER)
  def subscribeToRunLevelInit (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.subscribe(subscriber, RunlevelEventProvider.RUNLEVEL_INIT_CLASSIFIER)
  def subscribeToRunLevelRunning (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.subscribe(subscriber, RunlevelEventProvider.RUNLEVEL_RUNNING_CLASSIFIER)
  def subscribeToRunLevelBeforeShutdown (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.subscribe(subscriber, RunlevelEventProvider.RUNLEVEL_BEFORESHUTDOWN_CLASSIFIER)
  def subscribeToRunLevelAfterShutdown (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.subscribe(subscriber, RunlevelEventProvider.RUNLEVEL_AFTERSHUTDOWN_CLASSIFIER)
  def subscribeToRunLevelFailure (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.subscribe(subscriber, RunlevelEventProvider.RUNLEVEL_FAILURE_CLASSIFIER)

  def subscribeToTraffic (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.subscribe(subscriber, TrafficEventProvider.TRAFFIC_CLASSIFIER)
  def subscribeToTrafficRx (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.subscribe(subscriber, TrafficEventProvider.TRAFFIC_RX_CLASSIFIER)
  def subscribeToTrafficFaulty1Rx (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.subscribe(subscriber, TrafficEventProvider.TRAFFIC_FAULTYRX_CLASSIFIER)
  def subscribeToTrafficTx (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.subscribe(subscriber, TrafficEventProvider.TRAFFIC_TX_CLASSIFIER)

  //---
  def subscribe (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp, classifier: GrindEventBus.Classifier): Boolean = provider.subscribe(subscriber, classifier)

  def subscribeToChange (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.subscribe(subscriber, ChangeEventProvider.CHANGE_CLASSIFIER)
  def subscribeToChangePropChanged (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.subscribe(subscriber, ChangeEventProvider.CHANGE_PROPCHANGED_CLASSIFIER)
  def subscribeToChangeDeleted (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.subscribe(subscriber, ChangeEventProvider.CHANGE_DELETED_CLASSIFIER)

  def subscribeToLog (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.subscribe(subscriber, LoggingEventProvider.LOGGING_CLASSIFIER)
  def subscribeToLogDebug (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.subscribe(subscriber, LoggingEventProvider.LOGGING_DEBUG_CLASSIFIER)
  def subscribeToLogInfo (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.subscribe(subscriber, LoggingEventProvider.LOGGING_INFO_CLASSIFIER)
  def subscribeToLogWarning (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.subscribe(subscriber, LoggingEventProvider.LOGGING_WARNING_CLASSIFIER)
  def subscribeToLogError (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.subscribe(subscriber, LoggingEventProvider.LOGGING_ERROR_CLASSIFIER)
  def subscribeToLogCritical (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.subscribe(subscriber, LoggingEventProvider.LOGGING_CRITICAL_CLASSIFIER)
  def subscribeToLogFatal (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.subscribe(subscriber, LoggingEventProvider.LOGGING_FATAL_CLASSIFIER)

  def subscribeToProgress (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.subscribe(subscriber, ProgressEventProvider.PROGRESS_CLASSIFIER)
  def subscribeToProgressStart (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.subscribe(subscriber, ProgressEventProvider.PROGRESS_START_CLASSIFIER)
  def subscribeToProgressProgress (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.subscribe(subscriber, ProgressEventProvider.PROGRESS_PROGRESS_CLASSIFIER)
  def subscribeToProgressFinish (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.subscribe(subscriber, ProgressEventProvider.PROGRESS_FINISH_CLASSIFIER)

  def subscribeToRunLevel (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.subscribe(subscriber, RunlevelEventProvider.RUNLEVEL_CLASSIFIER)
  def subscribeToRunLevelInit (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.subscribe(subscriber, RunlevelEventProvider.RUNLEVEL_INIT_CLASSIFIER)
  def subscribeToRunLevelRunning (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.subscribe(subscriber, RunlevelEventProvider.RUNLEVEL_RUNNING_CLASSIFIER)
  def subscribeToRunLevelBeforeShutdown (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.subscribe(subscriber, RunlevelEventProvider.RUNLEVEL_BEFORESHUTDOWN_CLASSIFIER)
  def subscribeToRunLevelAfterShutdown (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.subscribe(subscriber, RunlevelEventProvider.RUNLEVEL_AFTERSHUTDOWN_CLASSIFIER)
  def subscribeToRunLevelFailure (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.subscribe(subscriber, RunlevelEventProvider.RUNLEVEL_FAILURE_CLASSIFIER)

  def subscribeToTraffic (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.subscribe(subscriber, TrafficEventProvider.TRAFFIC_CLASSIFIER)
  def subscribeToTrafficRx (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.subscribe(subscriber, TrafficEventProvider.TRAFFIC_RX_CLASSIFIER)
  def subscribeToTrafficFaulty1Rx (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.subscribe(subscriber, TrafficEventProvider.TRAFFIC_FAULTYRX_CLASSIFIER)
  def subscribeToTrafficTx (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.subscribe(subscriber, TrafficEventProvider.TRAFFIC_TX_CLASSIFIER)

  //---
  def unsubscribe (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Unit = provider.unsubscribe(subscriber)

  def unsubscribeFromChange (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.unsubscribe(subscriber, ChangeEventProvider.CHANGE_CLASSIFIER)
  def unsubscribeFromChangePropChanged (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.unsubscribe(subscriber, ChangeEventProvider.CHANGE_PROPCHANGED_CLASSIFIER)
  def unsubscribeFromChangeDeleted (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.unsubscribe(subscriber, ChangeEventProvider.CHANGE_DELETED_CLASSIFIER)

  def unsubscribeFromLog (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.unsubscribe(subscriber, LoggingEventProvider.LOGGING_CLASSIFIER)
  def unsubscribeFromLogDebug (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.unsubscribe(subscriber, LoggingEventProvider.LOGGING_DEBUG_CLASSIFIER)
  def unsubscribeFromLogInfo (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.unsubscribe(subscriber, LoggingEventProvider.LOGGING_INFO_CLASSIFIER)
  def unsubscribeFromLogWarning (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.unsubscribe(subscriber, LoggingEventProvider.LOGGING_WARNING_CLASSIFIER)
  def unsubscribeFromLogError (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.unsubscribe(subscriber, LoggingEventProvider.LOGGING_ERROR_CLASSIFIER)
  def unsubscribeFromLogCritical (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.unsubscribe(subscriber, LoggingEventProvider.LOGGING_CRITICAL_CLASSIFIER)
  def unsubscribeFromLogFatal (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.unsubscribe(subscriber, LoggingEventProvider.LOGGING_FATAL_CLASSIFIER)

  def unsubscribeFromProgress (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.unsubscribe(subscriber, ProgressEventProvider.PROGRESS_CLASSIFIER)
  def unsubscribeFromProgressStart (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.unsubscribe(subscriber, ProgressEventProvider.PROGRESS_START_CLASSIFIER)
  def unsubscribeFromProgressProgress (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.unsubscribe(subscriber, ProgressEventProvider.PROGRESS_PROGRESS_CLASSIFIER)
  def unsubscribeFromProgressFinish (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.unsubscribe(subscriber, ProgressEventProvider.PROGRESS_FINISH_CLASSIFIER)

  def unsubscribeFromRunLevel (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.unsubscribe(subscriber, RunlevelEventProvider.RUNLEVEL_CLASSIFIER)
  def unsubscribeFromRunLevelInit (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.unsubscribe(subscriber, RunlevelEventProvider.RUNLEVEL_INIT_CLASSIFIER)
  def unsubscribeFromRunLevelRunning (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.unsubscribe(subscriber, RunlevelEventProvider.RUNLEVEL_RUNNING_CLASSIFIER)
  def unsubscribeFromRunLevelBeforeShutdown (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.unsubscribe(subscriber, RunlevelEventProvider.RUNLEVEL_BEFORESHUTDOWN_CLASSIFIER)
  def unsubscribeFromRunLevelAfterShutdown (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.unsubscribe(subscriber, RunlevelEventProvider.RUNLEVEL_AFTERSHUTDOWN_CLASSIFIER)
  def unsubscribeFromRunLevelFailure (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.unsubscribe(subscriber, RunlevelEventProvider.RUNLEVEL_FAILURE_CLASSIFIER)

  def unsubscribeFromTraffic (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.unsubscribe(subscriber, TrafficEventProvider.TRAFFIC_CLASSIFIER)
  def unsubscribeFromTrafficRx (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.unsubscribe(subscriber, TrafficEventProvider.TRAFFIC_RX_CLASSIFIER)
  def unsubscribeFromTrafficRxFaulty (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.unsubscribe(subscriber, TrafficEventProvider.TRAFFIC_FAULTYRX_CLASSIFIER)
  def unsubscribeFromTrafficTx (provider: GrindActorEventBus, subscriber: GrindEventBus.SubscriberIsActor): Boolean = provider.unsubscribe(subscriber, TrafficEventProvider.TRAFFIC_TX_CLASSIFIER)

  //---
  def unsubscribe (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Unit = provider.unsubscribe(subscriber)

  def unsubscribeFromChange (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.unsubscribe(subscriber, ChangeEventProvider.CHANGE_CLASSIFIER)
  def unsubscribeFromChangePropChanged (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.unsubscribe(subscriber, ChangeEventProvider.CHANGE_PROPCHANGED_CLASSIFIER)
  def unsubscribeFromChangeDeleted (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.unsubscribe(subscriber, ChangeEventProvider.CHANGE_DELETED_CLASSIFIER)

  def unsubscribeFromLog (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.unsubscribe(subscriber, LoggingEventProvider.LOGGING_CLASSIFIER)
  def unsubscribeFromLogDebug (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.unsubscribe(subscriber, LoggingEventProvider.LOGGING_DEBUG_CLASSIFIER)
  def unsubscribeFromLogInfo (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.unsubscribe(subscriber, LoggingEventProvider.LOGGING_INFO_CLASSIFIER)
  def unsubscribeFromLogWarning (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.unsubscribe(subscriber, LoggingEventProvider.LOGGING_WARNING_CLASSIFIER)
  def unsubscribeFromLogError (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.unsubscribe(subscriber, LoggingEventProvider.LOGGING_ERROR_CLASSIFIER)
  def unsubscribeFromLogCritical (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.unsubscribe(subscriber, LoggingEventProvider.LOGGING_CRITICAL_CLASSIFIER)
  def unsubscribeFromLogFatal (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.unsubscribe(subscriber, LoggingEventProvider.LOGGING_FATAL_CLASSIFIER)

  def unsubscribeFromProgress (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.unsubscribe(subscriber, ProgressEventProvider.PROGRESS_CLASSIFIER)
  def unsubscribeFromProgressStart (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.unsubscribe(subscriber, ProgressEventProvider.PROGRESS_START_CLASSIFIER)
  def unsubscribeFromProgressProgress (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.unsubscribe(subscriber, ProgressEventProvider.PROGRESS_PROGRESS_CLASSIFIER)
  def unsubscribeFromProgressFinish (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.unsubscribe(subscriber, ProgressEventProvider.PROGRESS_FINISH_CLASSIFIER)

  def unsubscribeFromRunLevel (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.unsubscribe(subscriber, RunlevelEventProvider.RUNLEVEL_CLASSIFIER)
  def unsubscribeFromRunLevelInit (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.unsubscribe(subscriber, RunlevelEventProvider.RUNLEVEL_INIT_CLASSIFIER)
  def unsubscribeFromRunLevelRunning (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.unsubscribe(subscriber, RunlevelEventProvider.RUNLEVEL_RUNNING_CLASSIFIER)
  def unsubscribeFromRunLevelBeforeShutdown (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.unsubscribe(subscriber, RunlevelEventProvider.RUNLEVEL_BEFORESHUTDOWN_CLASSIFIER)
  def unsubscribeFromRunLevelAfterShutdown (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.unsubscribe(subscriber, RunlevelEventProvider.RUNLEVEL_AFTERSHUTDOWN_CLASSIFIER)
  def unsubscribeFromRunLevelFailure (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.unsubscribe(subscriber, RunlevelEventProvider.RUNLEVEL_FAILURE_CLASSIFIER)

  def unsubscribeFromTraffic (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.unsubscribe(subscriber, TrafficEventProvider.TRAFFIC_CLASSIFIER)
  def unsubscribeFromTrafficRx (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.unsubscribe(subscriber, TrafficEventProvider.TRAFFIC_RX_CLASSIFIER)
  def unsubscribeFromTrafficRxFaulty (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.unsubscribe(subscriber, TrafficEventProvider.TRAFFIC_FAULTYRX_CLASSIFIER)
  def unsubscribeFromTrafficTx (provider: GrindCallbackEventBus, subscriber: GrindEventBus.SubscriberIsOp): Boolean = provider.unsubscribe(subscriber, TrafficEventProvider.TRAFFIC_TX_CLASSIFIER)
}
