package de.pfke.grind.core.async.akka.eventBus.provider

import akka.actor.ActorRef
import de.pfke.grind.core.async.akka.eventBus._
import de.pfke.grind.core.async.akka.eventBus.bus.{GrindActorEventBus, GrindCallbackEventBus, GrindEventBus}
import de.pfke.grind.core.async.akka.eventBus.msg.{SubscribeMe, UnSubscribeMeFromClassifier}

object ProviderIncludes
  extends ProviderIncludes

trait ProviderIncludes {
  implicit class ProviderIncludes_forActorRef (
    in: ActorRef
  ) {
    type thisSubscriber = GrindEventBus.SubscriberIsActor

    def subscribe (subscriber: thisSubscriber, classifier: GrindEventBus.Classifier): Unit = in ! SubscribeMe(subscriber, classifier)

    def subscribeToChange (subscriber: thisSubscriber): Unit = in ! SubscribeMe(subscriber, ChangeEventProvider.CHANGE_CLASSIFIER)
    def subscribeToChangePropChanged (subscriber: thisSubscriber): Unit = in ! SubscribeMe(subscriber, ChangeEventProvider.CHANGE_PROPCHANGED_CLASSIFIER)
    def subscribeToChangeDeleted (subscriber: thisSubscriber): Unit = in ! SubscribeMe(subscriber, ChangeEventProvider.CHANGE_DELETED_CLASSIFIER)

    def subscribeToConnection (subscriber: thisSubscriber): Unit = in ! SubscribeMe(subscriber, ConnectionEventProvider.CONNECTION_CLASSIFIER)
    def subscribeToConnectionNotConnected (subscriber: thisSubscriber): Unit = in ! SubscribeMe(subscriber, ConnectionEventProvider.CONNECTION_NOTCONNECTED_CLASSIFIER)
    def subscribeToConnectionConnecting (subscriber: thisSubscriber): Unit = in ! SubscribeMe(subscriber, ConnectionEventProvider.CONNECTION_CONNECTING_CLASSIFIER)
    def subscribeToConnectionConnected (subscriber: thisSubscriber): Unit = in ! SubscribeMe(subscriber, ConnectionEventProvider.CONNECTION_CONNECTED_CLASSIFIER)
    def subscribeToConnectionDisconnecting (subscriber: thisSubscriber): Unit = in ! SubscribeMe(subscriber, ConnectionEventProvider.CONNECTION_DISCONNECTING_CLASSIFIER)
    def subscribeToConnectionConnectionError (subscriber: thisSubscriber): Unit = in ! SubscribeMe(subscriber, ConnectionEventProvider.CONNECTION_CONNECTIONERROR_CLASSIFIER)

    def subscribeToLogging (subscriber: thisSubscriber): Unit = in ! SubscribeMe(subscriber, LoggingEventProvider.LOGGING_CLASSIFIER)
    def subscribeToLoggingDebug (subscriber: thisSubscriber): Unit = in ! SubscribeMe(subscriber, LoggingEventProvider.LOGGING_DEBUG_CLASSIFIER)
    def subscribeToLoggingInfo (subscriber: thisSubscriber): Unit = in ! SubscribeMe(subscriber, LoggingEventProvider.LOGGING_INFO_CLASSIFIER)
    def subscribeToLoggingWarning (subscriber: thisSubscriber): Unit = in ! SubscribeMe(subscriber, LoggingEventProvider.LOGGING_WARNING_CLASSIFIER)
    def subscribeToLoggingError (subscriber: thisSubscriber): Unit = in ! SubscribeMe(subscriber, LoggingEventProvider.LOGGING_ERROR_CLASSIFIER)
    def subscribeToLoggingCritical (subscriber: thisSubscriber): Unit = in ! SubscribeMe(subscriber, LoggingEventProvider.LOGGING_CRITICAL_CLASSIFIER)
    def subscribeToLoggingFatal (subscriber: thisSubscriber): Unit = in ! SubscribeMe(subscriber, LoggingEventProvider.LOGGING_FATAL_CLASSIFIER)

    def subscribeToProgress (subscriber: thisSubscriber): Unit = in ! SubscribeMe(subscriber, ProgressEventProvider.PROGRESS_CLASSIFIER)
    def subscribeToProgressStart (subscriber: thisSubscriber): Unit = in ! SubscribeMe(subscriber, ProgressEventProvider.PROGRESS_START_CLASSIFIER)
    def subscribeToProgressProgress (subscriber: thisSubscriber): Unit = in ! SubscribeMe(subscriber, ProgressEventProvider.PROGRESS_PROGRESS_CLASSIFIER)
    def subscribeToProgressFinish (subscriber: thisSubscriber): Unit = in ! SubscribeMe(subscriber, ProgressEventProvider.PROGRESS_FINISH_CLASSIFIER)

    def subscribeToRunLevel (subscriber: thisSubscriber): Unit = in ! SubscribeMe(subscriber, RunlevelEventProvider.RUNLEVEL_CLASSIFIER)
    def subscribeToRunLevelInit (subscriber: thisSubscriber): Unit = in ! SubscribeMe(subscriber, RunlevelEventProvider.RUNLEVEL_INIT_CLASSIFIER)
    def subscribeToRunLevelRunning (subscriber: thisSubscriber): Unit = in ! SubscribeMe(subscriber, RunlevelEventProvider.RUNLEVEL_RUNNING_CLASSIFIER)
    def subscribeToRunLevelBeforeShutdown (subscriber: thisSubscriber): Unit = in ! SubscribeMe(subscriber, RunlevelEventProvider.RUNLEVEL_BEFORESHUTDOWN_CLASSIFIER)
    def subscribeToRunLevelAfterShutdown (subscriber: thisSubscriber): Unit = in ! SubscribeMe(subscriber, RunlevelEventProvider.RUNLEVEL_AFTERSHUTDOWN_CLASSIFIER)
    def subscribeToRunLevelFailure (subscriber: thisSubscriber): Unit = in ! SubscribeMe(subscriber, RunlevelEventProvider.RUNLEVEL_FAILURE_CLASSIFIER)

    def subscribeToTraffic (subscriber: thisSubscriber): Unit = in ! SubscribeMe(subscriber, TrafficEventProvider.TRAFFIC_CLASSIFIER)
    def subscribeToTrafficRx (subscriber: thisSubscriber): Unit = in ! SubscribeMe(subscriber, TrafficEventProvider.TRAFFIC_RX_CLASSIFIER)
    def subscribeToTrafficRxFaulty (subscriber: thisSubscriber): Unit = in ! SubscribeMe(subscriber, TrafficEventProvider.TRAFFIC_FAULTYRX_CLASSIFIER)
    def subscribeToTrafficTx (subscriber: thisSubscriber): Unit = in ! SubscribeMe(subscriber, TrafficEventProvider.TRAFFIC_TX_CLASSIFIER)

    def unsubscribe (subscriber: thisSubscriber): Unit = in.unsubscribe(subscriber)

    def unsubscribeFromChange (subscriber: thisSubscriber): Unit = in ! UnSubscribeMeFromClassifier(subscriber, ChangeEventProvider.CHANGE_CLASSIFIER)
    def unsubscribeFromChangePropChanged (subscriber: thisSubscriber): Unit = in ! UnSubscribeMeFromClassifier(subscriber, ChangeEventProvider.CHANGE_PROPCHANGED_CLASSIFIER)
    def unsubscribeFromChangeDeleted (subscriber: thisSubscriber): Unit = in ! UnSubscribeMeFromClassifier(subscriber, ChangeEventProvider.CHANGE_DELETED_CLASSIFIER)

    def unsubscribeFromError (subscriber: thisSubscriber): Unit = in ! UnSubscribeMeFromClassifier(subscriber, LoggingEventProvider.LOGGING_CLASSIFIER)
    def unsubscribeFromErrorDebug (subscriber: thisSubscriber): Unit = in ! UnSubscribeMeFromClassifier(subscriber, LoggingEventProvider.LOGGING_DEBUG_CLASSIFIER)
    def unsubscribeFromErrorInfo (subscriber: thisSubscriber): Unit = in ! UnSubscribeMeFromClassifier(subscriber, LoggingEventProvider.LOGGING_INFO_CLASSIFIER)
    def unsubscribeFromErrorWarning (subscriber: thisSubscriber): Unit = in ! UnSubscribeMeFromClassifier(subscriber, LoggingEventProvider.LOGGING_WARNING_CLASSIFIER)
    def unsubscribeFromErrorError (subscriber: thisSubscriber): Unit = in ! UnSubscribeMeFromClassifier(subscriber, LoggingEventProvider.LOGGING_ERROR_CLASSIFIER)
    def unsubscribeFromErrorCritical (subscriber: thisSubscriber): Unit = in ! UnSubscribeMeFromClassifier(subscriber, LoggingEventProvider.LOGGING_CRITICAL_CLASSIFIER)
    def unsubscribeFromErrorFatal (subscriber: thisSubscriber): Unit = in ! UnSubscribeMeFromClassifier(subscriber, LoggingEventProvider.LOGGING_FATAL_CLASSIFIER)

    def unsubscribeFromProgress (subscriber: thisSubscriber): Unit = in ! UnSubscribeMeFromClassifier(subscriber, ProgressEventProvider.PROGRESS_CLASSIFIER)
    def unsubscribeFromProgressStart (subscriber: thisSubscriber): Unit = in ! UnSubscribeMeFromClassifier(subscriber, ProgressEventProvider.PROGRESS_START_CLASSIFIER)
    def unsubscribeFromProgressProgress (subscriber: thisSubscriber): Unit = in ! UnSubscribeMeFromClassifier(subscriber, ProgressEventProvider.PROGRESS_PROGRESS_CLASSIFIER)
    def unsubscribeFromProgressFinish (subscriber: thisSubscriber): Unit = in ! UnSubscribeMeFromClassifier(subscriber, ProgressEventProvider.PROGRESS_FINISH_CLASSIFIER)

    def unsubscribeFromRunLevel (subscriber: thisSubscriber): Unit = in ! UnSubscribeMeFromClassifier(subscriber, RunlevelEventProvider.RUNLEVEL_CLASSIFIER)
    def unsubscribeFromRunLevelInit (subscriber: thisSubscriber): Unit = in ! UnSubscribeMeFromClassifier(subscriber, RunlevelEventProvider.RUNLEVEL_INIT_CLASSIFIER)
    def unsubscribeFromRunLevelRunning (subscriber: thisSubscriber): Unit = in ! UnSubscribeMeFromClassifier(subscriber, RunlevelEventProvider.RUNLEVEL_RUNNING_CLASSIFIER)
    def unsubscribeFromRunLevelBeforeShutdown (subscriber: thisSubscriber): Unit = in ! UnSubscribeMeFromClassifier(subscriber, RunlevelEventProvider.RUNLEVEL_BEFORESHUTDOWN_CLASSIFIER)
    def unsubscribeFromRunLevelAfterShutdown (subscriber: thisSubscriber): Unit = in ! UnSubscribeMeFromClassifier(subscriber, RunlevelEventProvider.RUNLEVEL_AFTERSHUTDOWN_CLASSIFIER)
    def unsubscribeFromRunLevelFailure (subscriber: thisSubscriber): Unit = in ! UnSubscribeMeFromClassifier(subscriber, RunlevelEventProvider.RUNLEVEL_FAILURE_CLASSIFIER)

    def unsubscribeFromTraffic (subscriber: thisSubscriber): Unit = in ! UnSubscribeMeFromClassifier(subscriber, TrafficEventProvider.TRAFFIC_CLASSIFIER)
    def unsubscribeFromTrafficRx (subscriber: thisSubscriber): Unit = in ! UnSubscribeMeFromClassifier(subscriber, TrafficEventProvider.TRAFFIC_RX_CLASSIFIER)
    def unsubscribeFromTrafficFaultyRx (subscriber: thisSubscriber): Unit = in ! UnSubscribeMeFromClassifier(subscriber, TrafficEventProvider.TRAFFIC_FAULTYRX_CLASSIFIER)
    def unsubscribeFromTrafficTx (subscriber: thisSubscriber): Unit = in ! UnSubscribeMeFromClassifier(subscriber, TrafficEventProvider.TRAFFIC_TX_CLASSIFIER)
  }

  implicit class ProviderIncludes_forGrindActorEventBus (
    in: GrindActorEventBus
  ) {
    type thisSubscriber = GrindEventBus.SubscriberIsActor

    def subscribe (subscriber: thisSubscriber, classifier: GrindEventBus.Classifier): Boolean = in.subscribe(subscriber, classifier)

    def subscribeToChange (subscriber: thisSubscriber): Boolean = in.subscribe(subscriber, ChangeEventProvider.CHANGE_CLASSIFIER)
    def subscribeToChangePropChanged (subscriber: thisSubscriber): Boolean = in.subscribe(subscriber, ChangeEventProvider.CHANGE_PROPCHANGED_CLASSIFIER)
    def subscribeToChangeDeleted (subscriber: thisSubscriber): Boolean = in.subscribe(subscriber, ChangeEventProvider.CHANGE_DELETED_CLASSIFIER)

    def subscribeToConnection (subscriber: thisSubscriber): Unit = in.subscribe(subscriber, ConnectionEventProvider.CONNECTION_CLASSIFIER)
    def subscribeToConnectionNotConnected (subscriber: thisSubscriber): Unit = in.subscribe(subscriber, ConnectionEventProvider.CONNECTION_NOTCONNECTED_CLASSIFIER)
    def subscribeToConnectionConnecting (subscriber: thisSubscriber): Unit = in.subscribe(subscriber, ConnectionEventProvider.CONNECTION_CONNECTING_CLASSIFIER)
    def subscribeToConnectionConnected (subscriber: thisSubscriber): Unit = in.subscribe(subscriber, ConnectionEventProvider.CONNECTION_CONNECTED_CLASSIFIER)
    def subscribeToConnectionDisconnecting (subscriber: thisSubscriber): Unit = in.subscribe(subscriber, ConnectionEventProvider.CONNECTION_DISCONNECTING_CLASSIFIER)
    def subscribeToConnectionConnectionError (subscriber: thisSubscriber): Unit = in.subscribe(subscriber, ConnectionEventProvider.CONNECTION_CONNECTIONERROR_CLASSIFIER)

    def subscribeToLogging (subscriber: thisSubscriber): Unit = in.subscribe(subscriber, LoggingEventProvider.LOGGING_CLASSIFIER)
    def subscribeToLoggingDebug (subscriber: thisSubscriber): Unit = in.subscribe(subscriber, LoggingEventProvider.LOGGING_DEBUG_CLASSIFIER)
    def subscribeToLoggingInfo (subscriber: thisSubscriber): Unit = in.subscribe(subscriber, LoggingEventProvider.LOGGING_INFO_CLASSIFIER)
    def subscribeToLoggingWarning (subscriber: thisSubscriber): Unit = in.subscribe(subscriber, LoggingEventProvider.LOGGING_WARNING_CLASSIFIER)
    def subscribeToLoggingError (subscriber: thisSubscriber): Unit = in.subscribe(subscriber, LoggingEventProvider.LOGGING_ERROR_CLASSIFIER)
    def subscribeToLoggingCritical (subscriber: thisSubscriber): Unit = in.subscribe(subscriber, LoggingEventProvider.LOGGING_CRITICAL_CLASSIFIER)
    def subscribeToLoggingFatal (subscriber: thisSubscriber): Unit = in.subscribe(subscriber, LoggingEventProvider.LOGGING_FATAL_CLASSIFIER)

    def subscribeToProgress (subscriber: thisSubscriber): Boolean = in.subscribe(subscriber, ProgressEventProvider.PROGRESS_CLASSIFIER)
    def subscribeToProgressStart (subscriber: thisSubscriber): Boolean = in.subscribe(subscriber, ProgressEventProvider.PROGRESS_START_CLASSIFIER)
    def subscribeToProgressProgress (subscriber: thisSubscriber): Boolean = in.subscribe(subscriber, ProgressEventProvider.PROGRESS_PROGRESS_CLASSIFIER)
    def subscribeToProgressFinish (subscriber: thisSubscriber): Boolean = in.subscribe(subscriber, ProgressEventProvider.PROGRESS_FINISH_CLASSIFIER)

    def subscribeToRunLevel (subscriber: thisSubscriber): Boolean = in.subscribe(subscriber, RunlevelEventProvider.RUNLEVEL_CLASSIFIER)
    def subscribeToRunLevelInit (subscriber: thisSubscriber): Boolean = in.subscribe(subscriber, RunlevelEventProvider.RUNLEVEL_INIT_CLASSIFIER)
    def subscribeToRunLevelRunning (subscriber: thisSubscriber): Boolean = in.subscribe(subscriber, RunlevelEventProvider.RUNLEVEL_RUNNING_CLASSIFIER)
    def subscribeToRunLevelBeforeShutdown (subscriber: thisSubscriber): Boolean = in.subscribe(subscriber, RunlevelEventProvider.RUNLEVEL_BEFORESHUTDOWN_CLASSIFIER)
    def subscribeToRunLevelAfterShutdown (subscriber: thisSubscriber): Boolean = in.subscribe(subscriber, RunlevelEventProvider.RUNLEVEL_AFTERSHUTDOWN_CLASSIFIER)
    def subscribeToRunLevelFailure (subscriber: thisSubscriber): Boolean = in.subscribe(subscriber, RunlevelEventProvider.RUNLEVEL_FAILURE_CLASSIFIER)

    def subscribeToTraffic (subscriber: thisSubscriber): Boolean = in.subscribe(subscriber, TrafficEventProvider.TRAFFIC_CLASSIFIER)
    def subscribeToTrafficRx (subscriber: thisSubscriber): Boolean = in.subscribe(subscriber, TrafficEventProvider.TRAFFIC_RX_CLASSIFIER)
    def subscribeToTrafficRxFaulty (subscriber: thisSubscriber): Boolean = in.subscribe(subscriber, TrafficEventProvider.TRAFFIC_FAULTYRX_CLASSIFIER)
    def subscribeToTrafficTx (subscriber: thisSubscriber): Boolean = in.subscribe(subscriber, TrafficEventProvider.TRAFFIC_TX_CLASSIFIER)

    def unsubscribe (subscriber: thisSubscriber): Unit = in.unsubscribe(subscriber)

    def unsubscribeFromChange (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, ChangeEventProvider.CHANGE_CLASSIFIER)
    def unsubscribeFromChangePropChanged (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, ChangeEventProvider.CHANGE_PROPCHANGED_CLASSIFIER)
    def unsubscribeFromChangeDeleted (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, ChangeEventProvider.CHANGE_DELETED_CLASSIFIER)

    def unsubscribeFromConnection (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, ConnectionEventProvider.CONNECTION_CLASSIFIER)
    def unsubscribeFromConnectionNotConnected (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, ConnectionEventProvider.CONNECTION_NOTCONNECTED_CLASSIFIER)
    def unsubscribeFromConnectionConnecting (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, ConnectionEventProvider.CONNECTION_CONNECTING_CLASSIFIER)
    def unsubscribeFromConnectionConnected (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, ConnectionEventProvider.CONNECTION_CONNECTED_CLASSIFIER)
    def unsubscribeFromConnectionDisconnecting (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, ConnectionEventProvider.CONNECTION_DISCONNECTING_CLASSIFIER)
    def unsubscribeFromConnectionConnectionError (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, ConnectionEventProvider.CONNECTION_CONNECTIONERROR_CLASSIFIER)

    def unsubscribeFromError (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, LoggingEventProvider.LOGGING_CLASSIFIER)
    def unsubscribeFromErrorDebug (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, LoggingEventProvider.LOGGING_DEBUG_CLASSIFIER)
    def unsubscribeFromErrorInfo (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, LoggingEventProvider.LOGGING_INFO_CLASSIFIER)
    def unsubscribeFromErrorWarning (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, LoggingEventProvider.LOGGING_WARNING_CLASSIFIER)
    def unsubscribeFromErrorError (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, LoggingEventProvider.LOGGING_ERROR_CLASSIFIER)
    def unsubscribeFromErrorCritical (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, LoggingEventProvider.LOGGING_CRITICAL_CLASSIFIER)
    def unsubscribeFromErrorFatal (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, LoggingEventProvider.LOGGING_FATAL_CLASSIFIER)

    def unsubscribeFromProgress (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, ProgressEventProvider.PROGRESS_CLASSIFIER)
    def unsubscribeFromProgressStart (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, ProgressEventProvider.PROGRESS_START_CLASSIFIER)
    def unsubscribeFromProgressProgress (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, ProgressEventProvider.PROGRESS_PROGRESS_CLASSIFIER)
    def unsubscribeFromProgressFinish (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, ProgressEventProvider.PROGRESS_FINISH_CLASSIFIER)

    def unsubscribeFromRunLevel (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, RunlevelEventProvider.RUNLEVEL_CLASSIFIER)
    def unsubscribeFromRunLevelInit (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, RunlevelEventProvider.RUNLEVEL_INIT_CLASSIFIER)
    def unsubscribeFromRunLevelRunning (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, RunlevelEventProvider.RUNLEVEL_RUNNING_CLASSIFIER)
    def unsubscribeFromRunLevelBeforeShutdown (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, RunlevelEventProvider.RUNLEVEL_BEFORESHUTDOWN_CLASSIFIER)
    def unsubscribeFromRunLevelAfterShutdown (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, RunlevelEventProvider.RUNLEVEL_AFTERSHUTDOWN_CLASSIFIER)
    def unsubscribeFromRunLevelFailure (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, RunlevelEventProvider.RUNLEVEL_FAILURE_CLASSIFIER)

    def unsubscribeFromTraffic (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, TrafficEventProvider.TRAFFIC_CLASSIFIER)
    def unsubscribeFromTrafficRx (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, TrafficEventProvider.TRAFFIC_RX_CLASSIFIER)
    def unsubscribeFromTrafficFaultyRx (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, TrafficEventProvider.TRAFFIC_FAULTYRX_CLASSIFIER)
    def unsubscribeFromTrafficTx (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, TrafficEventProvider.TRAFFIC_TX_CLASSIFIER)
  }

  implicit class ProviderIncludes_forGrindCallbackEventBus (
    in: GrindCallbackEventBus
  ) {
    type thisSubscriber = GrindEventBus.SubscriberIsOp

    def subscribe (subscriber: thisSubscriber, classifier: GrindEventBus.Classifier): Boolean = in.subscribe(subscriber, classifier)

    def subscribeToChange (subscriber: thisSubscriber): Boolean = in.subscribe(subscriber, ChangeEventProvider.CHANGE_CLASSIFIER)
    def subscribeToChangePropChanged (subscriber: thisSubscriber): Boolean = in.subscribe(subscriber, ChangeEventProvider.CHANGE_PROPCHANGED_CLASSIFIER)
    def subscribeToChangeDeleted (subscriber: thisSubscriber): Boolean = in.subscribe(subscriber, ChangeEventProvider.CHANGE_DELETED_CLASSIFIER)

    def subscribeToConnection (subscriber: thisSubscriber): Unit = in.subscribe(subscriber, ConnectionEventProvider.CONNECTION_CLASSIFIER)
    def subscribeToConnectionNotConnected (subscriber: thisSubscriber): Unit = in.subscribe(subscriber, ConnectionEventProvider.CONNECTION_NOTCONNECTED_CLASSIFIER)
    def subscribeToConnectionConnecting (subscriber: thisSubscriber): Unit = in.subscribe(subscriber, ConnectionEventProvider.CONNECTION_CONNECTING_CLASSIFIER)
    def subscribeToConnectionConnected (subscriber: thisSubscriber): Unit = in.subscribe(subscriber, ConnectionEventProvider.CONNECTION_CONNECTED_CLASSIFIER)
    def subscribeToConnectionDisconnecting (subscriber: thisSubscriber): Unit = in.subscribe(subscriber, ConnectionEventProvider.CONNECTION_DISCONNECTING_CLASSIFIER)
    def subscribeToConnectionConnectionError (subscriber: thisSubscriber): Unit = in.subscribe(subscriber, ConnectionEventProvider.CONNECTION_CONNECTIONERROR_CLASSIFIER)

    def subscribeToLogging (subscriber: thisSubscriber): Unit = in.subscribe(subscriber, LoggingEventProvider.LOGGING_CLASSIFIER)
    def subscribeToLoggingDebug (subscriber: thisSubscriber): Unit = in.subscribe(subscriber, LoggingEventProvider.LOGGING_DEBUG_CLASSIFIER)
    def subscribeToLoggingInfo (subscriber: thisSubscriber): Unit = in.subscribe(subscriber, LoggingEventProvider.LOGGING_INFO_CLASSIFIER)
    def subscribeToLoggingWarning (subscriber: thisSubscriber): Unit = in.subscribe(subscriber, LoggingEventProvider.LOGGING_WARNING_CLASSIFIER)
    def subscribeToLoggingError (subscriber: thisSubscriber): Unit = in.subscribe(subscriber, LoggingEventProvider.LOGGING_ERROR_CLASSIFIER)
    def subscribeToLoggingCritical (subscriber: thisSubscriber): Unit = in.subscribe(subscriber, LoggingEventProvider.LOGGING_CRITICAL_CLASSIFIER)
    def subscribeToLoggingFatal (subscriber: thisSubscriber): Unit = in.subscribe(subscriber, LoggingEventProvider.LOGGING_FATAL_CLASSIFIER)

    def subscribeToProgress (subscriber: thisSubscriber): Boolean = in.subscribe(subscriber, ProgressEventProvider.PROGRESS_CLASSIFIER)
    def subscribeToProgressStart (subscriber: thisSubscriber): Boolean = in.subscribe(subscriber, ProgressEventProvider.PROGRESS_START_CLASSIFIER)
    def subscribeToProgressProgress (subscriber: thisSubscriber): Boolean = in.subscribe(subscriber, ProgressEventProvider.PROGRESS_PROGRESS_CLASSIFIER)
    def subscribeToProgressFinish (subscriber: thisSubscriber): Boolean = in.subscribe(subscriber, ProgressEventProvider.PROGRESS_FINISH_CLASSIFIER)

    def subscribeToRunLevel (subscriber: thisSubscriber): Boolean = in.subscribe(subscriber, RunlevelEventProvider.RUNLEVEL_CLASSIFIER)
    def subscribeToRunLevelInit (subscriber: thisSubscriber): Boolean = in.subscribe(subscriber, RunlevelEventProvider.RUNLEVEL_INIT_CLASSIFIER)
    def subscribeToRunLevelRunning (subscriber: thisSubscriber): Boolean = in.subscribe(subscriber, RunlevelEventProvider.RUNLEVEL_RUNNING_CLASSIFIER)
    def subscribeToRunLevelBeforeShutdown (subscriber: thisSubscriber): Boolean = in.subscribe(subscriber, RunlevelEventProvider.RUNLEVEL_BEFORESHUTDOWN_CLASSIFIER)
    def subscribeToRunLevelAfterShutdown (subscriber: thisSubscriber): Boolean = in.subscribe(subscriber, RunlevelEventProvider.RUNLEVEL_AFTERSHUTDOWN_CLASSIFIER)
    def subscribeToRunLevelFailure (subscriber: thisSubscriber): Boolean = in.subscribe(subscriber, RunlevelEventProvider.RUNLEVEL_FAILURE_CLASSIFIER)

    def subscribeToTraffic (subscriber: thisSubscriber): Boolean = in.subscribe(subscriber, TrafficEventProvider.TRAFFIC_CLASSIFIER)
    def subscribeToTrafficRx (subscriber: thisSubscriber): Boolean = in.subscribe(subscriber, TrafficEventProvider.TRAFFIC_RX_CLASSIFIER)
    def subscribeToTrafficFaulty1Rx (subscriber: thisSubscriber): Boolean = in.subscribe(subscriber, TrafficEventProvider.TRAFFIC_FAULTYRX_CLASSIFIER)
    def subscribeToTrafficTx (subscriber: thisSubscriber): Boolean = in.subscribe(subscriber, TrafficEventProvider.TRAFFIC_TX_CLASSIFIER)

    def unsubscribe (subscriber: thisSubscriber): Unit = in.unsubscribe(subscriber)

    def unsubscribeFromChange (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, ChangeEventProvider.CHANGE_CLASSIFIER)
    def unsubscribeFromChangePropChanged (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, ChangeEventProvider.CHANGE_PROPCHANGED_CLASSIFIER)
    def unsubscribeFromChangeDeleted (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, ChangeEventProvider.CHANGE_DELETED_CLASSIFIER)

    def unsubscribeFromConnection (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, ConnectionEventProvider.CONNECTION_CLASSIFIER)
    def unsubscribeFromConnectionNotConnected (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, ConnectionEventProvider.CONNECTION_NOTCONNECTED_CLASSIFIER)
    def unsubscribeFromConnectionConnecting (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, ConnectionEventProvider.CONNECTION_CONNECTING_CLASSIFIER)
    def unsubscribeFromConnectionConnected (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, ConnectionEventProvider.CONNECTION_CONNECTED_CLASSIFIER)
    def unsubscribeFromConnectionDisconnecting (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, ConnectionEventProvider.CONNECTION_DISCONNECTING_CLASSIFIER)
    def unsubscribeFromConnectionConnectionError (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, ConnectionEventProvider.CONNECTION_CONNECTIONERROR_CLASSIFIER)

    def unsubscribeFromError (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, LoggingEventProvider.LOGGING_CLASSIFIER)
    def unsubscribeFromErrorDebug (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, LoggingEventProvider.LOGGING_DEBUG_CLASSIFIER)
    def unsubscribeFromErrorInfo (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, LoggingEventProvider.LOGGING_INFO_CLASSIFIER)
    def unsubscribeFromErrorWarning (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, LoggingEventProvider.LOGGING_WARNING_CLASSIFIER)
    def unsubscribeFromErrorError (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, LoggingEventProvider.LOGGING_ERROR_CLASSIFIER)
    def unsubscribeFromErrorCritical (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, LoggingEventProvider.LOGGING_CRITICAL_CLASSIFIER)
    def unsubscribeFromErrorFatal (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, LoggingEventProvider.LOGGING_FATAL_CLASSIFIER)

    def unsubscribeFromProgress (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, ProgressEventProvider.PROGRESS_CLASSIFIER)
    def unsubscribeFromProgressStart (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, ProgressEventProvider.PROGRESS_START_CLASSIFIER)
    def unsubscribeFromProgressProgress (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, ProgressEventProvider.PROGRESS_PROGRESS_CLASSIFIER)
    def unsubscribeFromProgressFinish (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, ProgressEventProvider.PROGRESS_FINISH_CLASSIFIER)

    def unsubscribeFromRunLevel (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, RunlevelEventProvider.RUNLEVEL_CLASSIFIER)
    def unsubscribeFromRunLevelInit (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, RunlevelEventProvider.RUNLEVEL_INIT_CLASSIFIER)
    def unsubscribeFromRunLevelRunning (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, RunlevelEventProvider.RUNLEVEL_RUNNING_CLASSIFIER)
    def unsubscribeFromRunLevelBeforeShutdown (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, RunlevelEventProvider.RUNLEVEL_BEFORESHUTDOWN_CLASSIFIER)
    def unsubscribeFromRunLevelAfterShutdown (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, RunlevelEventProvider.RUNLEVEL_AFTERSHUTDOWN_CLASSIFIER)
    def unsubscribeFromRunLevelFailure (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, RunlevelEventProvider.RUNLEVEL_FAILURE_CLASSIFIER)

    def unsubscribeFromTraffic (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, TrafficEventProvider.TRAFFIC_CLASSIFIER)
    def unsubscribeFromTrafficRx (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, TrafficEventProvider.TRAFFIC_RX_CLASSIFIER)
    def unsubscribeFromTrafficRxFaulty (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, TrafficEventProvider.TRAFFIC_FAULTYRX_CLASSIFIER)
    def unsubscribeFromTrafficTx (subscriber: thisSubscriber): Boolean = in.unsubscribe(subscriber, TrafficEventProvider.TRAFFIC_TX_CLASSIFIER)
  }
}
