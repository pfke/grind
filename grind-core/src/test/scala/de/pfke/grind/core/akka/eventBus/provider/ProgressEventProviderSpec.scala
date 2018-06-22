package de.pfke.grind.core.akka.eventBus.provider

import de.pfke.grind.core.async.akka.eventBus.bus.{GrindCallbackEventBus, GrindEventBus}
import de.pfke.grind.core.async.akka.eventBus.provider.{ProgressEventProvider, ProviderFactory}
import de.pfke.grind.core.async.event.ProgressEventApi

import scala.collection.mutable

class ProgressEventProviderMock
  extends GrindCallbackEventBus
    with ProgressEventProvider {
  def pupFinish(): Unit = publishProgressFinish("1")
  def pupProgress(): Unit = publishProgressProgress("2", 30f)
  def pupStart(): Unit = publishProgressStart("3")
}

class ProgressEventProviderSpec
  extends ProviderSpec {
  "using different subscribe methods" when {
    "using method 'subscribeToProgress'" should {
      "firing FaultyRx msgs" in {
        val (tto, results) = create_subscribeToProgress()
        tto.pupFinish()

        compare(Map("progressFinish" -> 1, "progressProgress" -> 0, "progressStart" -> 0), results)
      }

      "firing Rx msgs" in {
        val (tto, results) = create_subscribeToProgress()
        tto.pupProgress()

        compare(Map("progressFinish" -> 0, "progressProgress" -> 2, "progressStart" -> 0), results)
      }

      "firing progressStart msgs" in {
        val (tto, results) = create_subscribeToProgress()
        tto.pupStart()

        compare(Map("progressFinish" -> 0, "progressProgress" -> 0, "progressStart" -> 3), results)
      }
    }

    "using method 'subscribeToTrafficFaultyRx'" should {
      "firing progressFinish msgs" in {
        val (tto, results) = create_subscribeToProgressFinish()
        tto.pupFinish()

        compare(Map("progressFinish" -> 1, "progressProgress" -> 0, "progressStart" -> 0), results)
      }

      "firing progressProgress msgs" in {
        val (tto, results) = create_subscribeToProgressFinish()
        tto.pupProgress()

        compare(Map("progressFinish" -> 0, "progressProgress" -> 0, "progressStart" -> 0), results)
      }

      "firing progressStart msgs" in {
        val (tto, results) = create_subscribeToProgressFinish()
        tto.pupStart()

        compare(Map("progressFinish" -> 0, "progressProgress" -> 0, "progressStart" -> 0), results)
      }
    }

    "using method 'subscribeToTrafficRx'" should {
      "firing progressFinish msgs" in {
        val (tto, results) = create_subscribeToProgressProgress()
        tto.pupFinish()

        compare(Map("progressFinish" -> 0, "progressProgress" -> 0, "progressStart" -> 0), results)
      }

      "firing progressProgress msgs" in {
        val (tto, results) = create_subscribeToProgressProgress()
        tto.pupProgress()

        compare(Map("progressFinish" -> 0, "progressProgress" -> 2, "progressStart" -> 0), results)
      }

      "firing progressStart msgs" in {
        val (tto, results) = create_subscribeToProgressProgress()
        tto.pupStart()

        compare(Map("progressFinish" -> 0, "progressProgress" -> 0, "progressStart" -> 0), results)
      }
    }

    "using method 'subscribeToTrafficTx'" should {
      "firing progressFinish msgs" in {
        val (tto, results) = create_subscribeToProgressStart()
        tto.pupFinish()

        compare(Map("progressFinish" -> 0, "progressProgress" -> 0, "progressStart" -> 0), results)
      }

      "firing progressProgress msgs" in {
        val (tto, results) = create_subscribeToProgressStart()
        tto.pupProgress()

        compare(Map("progressFinish" -> 0, "progressProgress" -> 0, "progressStart" -> 0), results)
      }

      "firing progressStart msgs" in {
        val (tto, results) = create_subscribeToProgressStart()
        tto.pupStart()

        compare(Map("progressFinish" -> 0, "progressProgress" -> 0, "progressStart" -> 3), results)
      }
    }
  }

  private def create_subscribe(
    subscribeOp: (GrindCallbackEventBus, GrindEventBus.SubscriberIsOp) => Boolean
  ): (ProgressEventProviderMock, mutable.Map[String, Int]) = {
    val tto = new ProgressEventProviderMock
    val results = mutable.Map(
      "progressFinish" -> 0,
      "progressStart" -> 0,
      "progressProgress" -> 0
    )

    subscribeOp(
      tto,
      {
        case _: ProgressEventApi.Finish => results("progressFinish") = results("progressFinish") + 1
        case _: ProgressEventApi.Progress => results("progressProgress") = results("progressProgress") + 2
        case _: ProgressEventApi.Start => results("progressStart") = results("progressStart") + 3
      }
    )

    (tto, results)
  }

  private def create_subscribeToProgress(): (ProgressEventProviderMock, mutable.Map[String, Int]) = {
    create_subscribe(ProviderFactory.subscribeToProgress)
  }

  private def create_subscribeToProgressFinish(): (ProgressEventProviderMock, mutable.Map[String, Int]) = {
    create_subscribe(ProviderFactory.subscribeToProgressFinish)
  }

  private def create_subscribeToProgressProgress(): (ProgressEventProviderMock, mutable.Map[String, Int]) = {
    create_subscribe(ProviderFactory.subscribeToProgressProgress)
  }

  private def create_subscribeToProgressStart(): (ProgressEventProviderMock, mutable.Map[String, Int]) = {
    create_subscribe(ProviderFactory.subscribeToProgressStart)
  }
}
