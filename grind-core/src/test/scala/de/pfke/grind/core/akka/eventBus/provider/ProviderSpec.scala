package de.pfke.grind.core.akka.eventBus.provider

import org.scalatest.{Matchers, WordSpec}

class ProviderSpec
  extends WordSpec
    with Matchers {
  protected def compare(
    wantedMap: scala.collection.Map[String, Int],
    resultMap: scala.collection.Map[String, Int]
  ): Unit = {
    wantedMap
      .foreach {
        case (k, v) =>
          withClue(s"value does not match (key='$k', given='${resultMap(k)}', wanted='$v')") {
            resultMap(k) should be (v)
          }
    }
  }
}
