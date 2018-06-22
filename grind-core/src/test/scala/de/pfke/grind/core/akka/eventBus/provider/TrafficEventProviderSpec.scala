package de.pfke.grind.core.akka.eventBus.provider

import de.pfke.grind.core.async.akka.eventBus.bus.{GrindCallbackEventBus, GrindEventBus}
import de.pfke.grind.core.async.akka.eventBus.provider.{ProviderFactory, TrafficEventProvider}
import de.pfke.grind.core.async.event.TrafficEventApi

import scala.collection.mutable

class TrafficEventProviderMock
  extends GrindCallbackEventBus
    with TrafficEventProvider {
  def pupFaultyRx(): Unit = publishTrafficFaultyRx("1")
  def pupRx(): Unit = publishTrafficRx("2")
  def pupTx(): Unit = publishTrafficTx("3")
}

class TrafficEventProviderSpec
  extends ProviderSpec {
  "using different subscribe methods" when {
    "using method 'subscribeToTraffic'" should {
      "firing FaultyRx msgs" in {
        val (tto, results) = create_subscribeToTraffic()
        tto.pupFaultyRx()

        compare(Map("faultyRx" -> 1, "rx" -> 0, "tx" -> 0), results)
      }

      "firing Rx msgs" in {
        val (tto, results) = create_subscribeToTraffic()
        tto.pupRx()

        compare(Map("faultyRx" -> 0, "rx" -> 2, "tx" -> 0), results)
      }

      "firing Tx msgs" in {
        val (tto, results) = create_subscribeToTraffic()
        tto.pupTx()

        compare(Map("faultyRx" -> 0, "rx" -> 0, "tx" -> 3), results)
      }
    }

    "using method 'subscribeToTrafficFaultyRx'" should {
      "firing FaultyRx msgs" in {
        val (tto, results) = create_subscribeToTrafficFaultyRx()
        tto.pupFaultyRx()

        compare(Map("faultyRx" -> 1, "rx" -> 0, "tx" -> 0), results)
      }

      "firing Rx msgs" in {
        val (tto, results) = create_subscribeToTrafficFaultyRx()
        tto.pupRx()

        compare(Map("faultyRx" -> 0, "rx" -> 0, "tx" -> 0), results)
      }

      "firing Tx msgs" in {
        val (tto, results) = create_subscribeToTrafficFaultyRx()
        tto.pupTx()

        compare(Map("faultyRx" -> 0, "rx" -> 0, "tx" -> 0), results)
      }
    }

    "using method 'subscribeToTrafficRx'" should {
      "firing FaultyRx msgs" in {
        val (tto, results) = create_subscribeToTrafficRx()
        tto.pupFaultyRx()

        compare(Map("faultyRx" -> 0, "rx" -> 0, "tx" -> 0), results)
      }

      "firing Rx msgs" in {
        val (tto, results) = create_subscribeToTrafficRx()
        tto.pupRx()

        compare(Map("faultyRx" -> 0, "rx" -> 2, "tx" -> 0), results)
      }

      "firing Tx msgs" in {
        val (tto, results) = create_subscribeToTrafficRx()
        tto.pupTx()

        compare(Map("faultyRx" -> 0, "rx" -> 0, "tx" -> 0), results)
      }
    }

    "using method 'subscribeToTrafficTx'" should {
      "firing FaultyRx msgs" in {
        val (tto, results) = create_subscribeToTrafficTx()
        tto.pupFaultyRx()

        compare(Map("faultyRx" -> 0, "rx" -> 0, "tx" -> 0), results)
      }

      "firing Rx msgs" in {
        val (tto, results) = create_subscribeToTrafficTx()
        tto.pupRx()

        compare(Map("faultyRx" -> 0, "rx" -> 0, "tx" -> 0), results)
      }

      "firing Tx msgs" in {
        val (tto, results) = create_subscribeToTrafficTx()
        tto.pupTx()

        compare(Map("faultyRx" -> 0, "rx" -> 0, "tx" -> 3), results)
      }
    }
  }

  private def create_subscribe(
    subscribeOp: (GrindCallbackEventBus, GrindEventBus.SubscriberIsOp) => Boolean
  ): (TrafficEventProviderMock, mutable.Map[String, Int]) = {
    val tto = new TrafficEventProviderMock
    val results = mutable.Map(
      "faultyRx" -> 0,
      "rx" -> 0,
      "tx" -> 0
    )

    subscribeOp(
      tto,
      {
        case _: TrafficEventApi.FaultyRx => results("faultyRx") = results("faultyRx") + 1
        case _: TrafficEventApi.Rx => results("rx") = results("rx") + 2
        case _: TrafficEventApi.Tx => results("tx") = results("tx") + 3
      }
    )

    (tto, results)
  }

  private def create_subscribeToTraffic(): (TrafficEventProviderMock, mutable.Map[String, Int]) = {
    create_subscribe(ProviderFactory.subscribeToTraffic)
  }

  private def create_subscribeToTrafficFaultyRx(): (TrafficEventProviderMock, mutable.Map[String, Int]) = {
    create_subscribe(ProviderFactory.subscribeToTrafficFaulty1Rx)
  }

  private def create_subscribeToTrafficRx(): (TrafficEventProviderMock, mutable.Map[String, Int]) = {
    create_subscribe(ProviderFactory.subscribeToTrafficRx)
  }

  private def create_subscribeToTrafficTx(): (TrafficEventProviderMock, mutable.Map[String, Int]) = {
    create_subscribe(ProviderFactory.subscribeToTrafficTx)
  }
}
