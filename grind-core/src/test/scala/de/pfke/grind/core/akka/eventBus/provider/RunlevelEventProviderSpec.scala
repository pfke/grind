package de.pfke.grind.core.akka.eventBus.provider

import de.pfke.grind.core.async.akka.eventBus.bus.{GrindCallbackEventBus, GrindEventBus}
import de.pfke.grind.core.async.akka.eventBus.provider.{ProviderFactory, RunlevelEventProvider}
import de.pfke.grind.core.async.event.RunlevelEventApi

import scala.collection.mutable

class RunlevelEventProviderMock
  extends GrindCallbackEventBus
    with RunlevelEventProvider {
  def pupAfterShutdown(): Unit = publishRunlevelAfterShutdown()
  def pupBeforeShutdown(): Unit = publishRunlevelBeforeShutdown()
  def pupFailure(): Unit = publishRunlevelFailure()
  def pupInit(): Unit = publishRunlevelInit()
  def pupRunning(): Unit = publishRunlevelRunning()
}

class RunlevelEventProviderSpec
  extends ProviderSpec {
  "using different subscribe methods" when {
    "using method 'subscribeToRunLevel'" should {
      "firing AfterShutdown msgs" in {
        val (tto, results) = create_subscribeToRunLevel()
        tto.pupAfterShutdown()

        compare(Map("AfterShutdown" -> 1, "BeforeShutdown" -> 0, "Failure" -> 0, "Init" -> 4, "Running" -> 0), results)
      }

      "firing BeforeShutdown msgs" in {
        val (tto, results) = create_subscribeToRunLevel()
        tto.pupBeforeShutdown()

        compare(Map("AfterShutdown" -> 0, "BeforeShutdown" -> 2, "Failure" -> 0, "Init" -> 4, "Running" -> 0), results)
      }

      "firing Failure msgs" in {
        val (tto, results) = create_subscribeToRunLevel()
        tto.pupFailure()

        compare(Map("AfterShutdown" -> 0, "BeforeShutdown" -> 0, "Failure" -> 3, "Init" -> 4, "Running" -> 0), results)
      }

      "firing Init msgs" in {
        val (tto, results) = create_subscribeToRunLevel()
        tto.pupInit()

        compare(Map("AfterShutdown" -> 0, "BeforeShutdown" -> 0, "Failure" -> 0, "Init" -> 4, "Running" -> 0), results)
      }

      "firing Running msgs" in {
        val (tto, results) = create_subscribeToRunLevel()
        tto.pupRunning()

        compare(Map("AfterShutdown" -> 0, "BeforeShutdown" -> 0, "Failure" -> 0, "Init" -> 4, "Running" -> 5), results)
      }
    }

    "using method 'subscribeToRunLevelAfterShutdown()'" should {
      "firing AfterShutdown msgs" in {
        val (tto, results) = create_subscribeToRunLevelAfterShutdown()
        tto.pupAfterShutdown()

        compare(Map("AfterShutdown" -> 1, "BeforeShutdown" -> 0, "Failure" -> 0, "Init" -> 0, "Running" -> 0), results)
      }

      "firing BeforeShutdown msgs" in {
        val (tto, results) = create_subscribeToRunLevelAfterShutdown()
        tto.pupBeforeShutdown()

        compare(Map("AfterShutdown" -> 0, "BeforeShutdown" -> 0, "Failure" -> 0, "Init" -> 0, "Running" -> 0), results)
      }

      "firing Failure msgs" in {
        val (tto, results) = create_subscribeToRunLevelAfterShutdown()
        tto.pupFailure()

        compare(Map("AfterShutdown" -> 0, "BeforeShutdown" -> 0, "Failure" -> 0, "Init" -> 0, "Running" -> 0), results)
      }

      "firing Init msgs" in {
        val (tto, results) = create_subscribeToRunLevelAfterShutdown()
        tto.pupInit()

        compare(Map("AfterShutdown" -> 0, "BeforeShutdown" -> 0, "Failure" -> 0, "Init" -> 0, "Running" -> 0), results)
      }

      "firing Running msgs" in {
        val (tto, results) = create_subscribeToRunLevelAfterShutdown()
        tto.pupRunning()

        compare(Map("AfterShutdown" -> 0, "BeforeShutdown" -> 0, "Failure" -> 0, "Init" -> 0, "Running" -> 0), results)
      }
    }

    "using method 'subscribeToRunLevelBeforeShutdown()'" should {
      "firing AfterShutdown msgs" in {
        val (tto, results) = create_subscribeToRunLevelBeforeShutdown()
        tto.pupAfterShutdown()

        compare(Map("AfterShutdown" -> 0, "BeforeShutdown" -> 0, "Failure" -> 0, "Init" -> 0, "Running" -> 0), results)
      }

      "firing BeforeShutdown msgs" in {
        val (tto, results) = create_subscribeToRunLevelBeforeShutdown()
        tto.pupBeforeShutdown()

        compare(Map("AfterShutdown" -> 0, "BeforeShutdown" -> 2, "Failure" -> 0, "Init" -> 0, "Running" -> 0), results)
      }

      "firing Failure msgs" in {
        val (tto, results) = create_subscribeToRunLevelBeforeShutdown()
        tto.pupFailure()

        compare(Map("AfterShutdown" -> 0, "BeforeShutdown" -> 0, "Failure" -> 0, "Init" -> 0, "Running" -> 0), results)
      }

      "firing Init msgs" in {
        val (tto, results) = create_subscribeToRunLevelBeforeShutdown()
        tto.pupInit()

        compare(Map("AfterShutdown" -> 0, "BeforeShutdown" -> 0, "Failure" -> 0, "Init" -> 0, "Running" -> 0), results)
      }

      "firing Running msgs" in {
        val (tto, results) = create_subscribeToRunLevelBeforeShutdown()
        tto.pupRunning()

        compare(Map("AfterShutdown" -> 0, "BeforeShutdown" -> 0, "Failure" -> 0, "Init" -> 0, "Running" -> 0), results)
      }
    }

    "using method 'subscribeToRunLevelFailure()'" should {
      "firing AfterShutdown msgs" in {
        val (tto, results) = create_subscribeToRunLevelFailure()
        tto.pupAfterShutdown()

        compare(Map("AfterShutdown" -> 0, "BeforeShutdown" -> 0, "Failure" -> 0, "Init" -> 0, "Running" -> 0), results)
      }

      "firing BeforeShutdown msgs" in {
        val (tto, results) = create_subscribeToRunLevelFailure()
        tto.pupBeforeShutdown()

        compare(Map("AfterShutdown" -> 0, "BeforeShutdown" -> 0, "Failure" -> 0, "Init" -> 0, "Running" -> 0), results)
      }

      "firing Failure msgs" in {
        val (tto, results) = create_subscribeToRunLevelFailure()
        tto.pupFailure()

        compare(Map("AfterShutdown" -> 0, "BeforeShutdown" -> 0, "Failure" -> 3, "Init" -> 0, "Running" -> 0), results)
      }

      "firing Init msgs" in {
        val (tto, results) = create_subscribeToRunLevelFailure()
        tto.pupInit()

        compare(Map("AfterShutdown" -> 0, "BeforeShutdown" -> 0, "Failure" -> 0, "Init" -> 0, "Running" -> 0), results)
      }

      "firing Running msgs" in {
        val (tto, results) = create_subscribeToRunLevelFailure()
        tto.pupRunning()

        compare(Map("AfterShutdown" -> 0, "BeforeShutdown" -> 0, "Failure" -> 0, "Init" -> 0, "Running" -> 0), results)
      }
    }

    "using method 'subscribeToRunLevelInit()'" should {
      "firing AfterShutdown msgs" in {
        val (tto, results) = create_subscribeToRunLevelInit()
        tto.pupAfterShutdown()

        compare(Map("AfterShutdown" -> 0, "BeforeShutdown" -> 0, "Failure" -> 0, "Init" -> 4, "Running" -> 0), results)
      }

      "firing BeforeShutdown msgs" in {
        val (tto, results) = create_subscribeToRunLevelInit()
        tto.pupBeforeShutdown()

        compare(Map("AfterShutdown" -> 0, "BeforeShutdown" -> 0, "Failure" -> 0, "Init" -> 4, "Running" -> 0), results)
      }

      "firing Failure msgs" in {
        val (tto, results) = create_subscribeToRunLevelInit()
        tto.pupFailure()

        compare(Map("AfterShutdown" -> 0, "BeforeShutdown" -> 0, "Failure" -> 0, "Init" -> 4, "Running" -> 0), results)
      }

      "firing Init msgs" in {
        val (tto, results) = create_subscribeToRunLevelInit()
        tto.pupInit()

        compare(Map("AfterShutdown" -> 0, "BeforeShutdown" -> 0, "Failure" -> 0, "Init" -> 4, "Running" -> 0), results)
      }

      "firing Running msgs" in {
        val (tto, results) = create_subscribeToRunLevelInit()
        tto.pupRunning()

        compare(Map("AfterShutdown" -> 0, "BeforeShutdown" -> 0, "Failure" -> 0, "Init" -> 4, "Running" -> 0), results)
      }
    }

    "using method 'subscribeToRunLevelRunning()'" should {
      "firing AfterShutdown msgs" in {
        val (tto, results) = create_subscribeToRunLevelRunning()
        tto.pupAfterShutdown()

        compare(Map("AfterShutdown" -> 0, "BeforeShutdown" -> 0, "Failure" -> 0, "Init" -> 0, "Running" -> 0), results)
      }

      "firing BeforeShutdown msgs" in {
        val (tto, results) = create_subscribeToRunLevelRunning()
        tto.pupBeforeShutdown()

        compare(Map("AfterShutdown" -> 0, "BeforeShutdown" -> 0, "Failure" -> 0, "Init" -> 0, "Running" -> 0), results)
      }

      "firing Failure msgs" in {
        val (tto, results) = create_subscribeToRunLevelRunning()
        tto.pupFailure()

        compare(Map("AfterShutdown" -> 0, "BeforeShutdown" -> 0, "Failure" -> 0, "Init" -> 0, "Running" -> 0), results)
      }

      "firing Init msgs" in {
        val (tto, results) = create_subscribeToRunLevelRunning()
        tto.pupInit()

        compare(Map("AfterShutdown" -> 0, "BeforeShutdown" -> 0, "Failure" -> 0, "Init" -> 0, "Running" -> 0), results)
      }

      "firing Running msgs" in {
        val (tto, results) = create_subscribeToRunLevelRunning()
        tto.pupRunning()

        compare(Map("AfterShutdown" -> 0, "BeforeShutdown" -> 0, "Failure" -> 0, "Init" -> 0, "Running" -> 5), results)
      }
    }
  }

  "pruefen, ob Events permanent anliegen" when {
    "testing init method (no action needed)" should {
      val tto = new RunlevelEventProviderMock
      val results = mutable.Map(
        "AfterShutdown" -> 0,
        "BeforeShutdown" -> 0,
        "Failure" -> 0,
        "Init" -> 0,
        "Running" -> 0
      )

      "init should be published to us" in {
        ProviderFactory.subscribeToRunLevel(
          tto,
          {
            case _: RunlevelEventApi.AfterShutdown => results("AfterShutdown") = results("AfterShutdown") + 1
            case _: RunlevelEventApi.BeforeShutdown => results("BeforeShutdown") = results("BeforeShutdown") + 2
            case _: RunlevelEventApi.Failure => results("Failure") = results("Failure") + 3
            case _: RunlevelEventApi.Init => results("Init") = results("Init") + 4
            case _: RunlevelEventApi.Running => results("Running") = results("Running") + 5
          }: GrindEventBus.SubscriberIsOp
        )

        compare(Map("AfterShutdown" -> 0, "BeforeShutdown" -> 0, "Failure" -> 0, "Init" -> 4, "Running" -> 0), results)
      }
    }
  }

  private def create_subscribe(
    subscribeOp: (GrindCallbackEventBus, GrindEventBus.SubscriberIsOp) => Boolean
  ): (RunlevelEventProviderMock, mutable.Map[String, Int]) = {
    val tto = new RunlevelEventProviderMock
    val results = mutable.Map(
      "AfterShutdown" -> 0,
      "BeforeShutdown" -> 0,
      "Failure" -> 0,
      "Init" -> 0,
      "Running" -> 0
    )

    subscribeOp(
      tto,
      {
        case _: RunlevelEventApi.AfterShutdown => results("AfterShutdown") = results("AfterShutdown") + 1
        case _: RunlevelEventApi.BeforeShutdown => results("BeforeShutdown") = results("BeforeShutdown") + 2
        case _: RunlevelEventApi.Failure => results("Failure") = results("Failure") + 3
        case _: RunlevelEventApi.Init => results("Init") = results("Init") + 4
        case _: RunlevelEventApi.Running => results("Running") = results("Running") + 5
      }
    )

    (tto, results)
  }

  private def create_subscribeToRunLevel(): (RunlevelEventProviderMock, mutable.Map[String, Int]) = {
    create_subscribe(ProviderFactory.subscribeToRunLevel)
  }

  private def create_subscribeToRunLevelAfterShutdown(): (RunlevelEventProviderMock, mutable.Map[String, Int]) = {
    create_subscribe(ProviderFactory.subscribeToRunLevelAfterShutdown)
  }

  private def create_subscribeToRunLevelBeforeShutdown(): (RunlevelEventProviderMock, mutable.Map[String, Int]) = {
    create_subscribe(ProviderFactory.subscribeToRunLevelBeforeShutdown)
  }

  private def create_subscribeToRunLevelFailure(): (RunlevelEventProviderMock, mutable.Map[String, Int]) = {
    create_subscribe(ProviderFactory.subscribeToRunLevelFailure)
  }

  private def create_subscribeToRunLevelInit(): (RunlevelEventProviderMock, mutable.Map[String, Int]) = {
    create_subscribe(ProviderFactory.subscribeToRunLevelInit)
  }

  private def create_subscribeToRunLevelRunning(): (RunlevelEventProviderMock, mutable.Map[String, Int]) = {
    create_subscribe(ProviderFactory.subscribeToRunLevelRunning)
  }
}
