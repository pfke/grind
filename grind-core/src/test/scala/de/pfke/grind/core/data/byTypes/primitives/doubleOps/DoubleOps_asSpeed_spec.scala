package de.pfke.grind.core.data.byTypes.primitives.doubleOps

import de.pfke.grind.core.data.byTypes.primitives.DoubleOps
import org.scalatest.{Matchers, WordSpecLike}

import scala.concurrent.duration._

class DoubleOps_asSpeed_spec
  extends WordSpecLike
    with Matchers {
  "on implicit class StringIncludes.SpeedFormatter(Long)" when {
    "using method 'asSpeed(Duration)'" should {
      "convert to B/s" in {
        DoubleOps.asSpeed(in = 563d, 1 second) should be ("563 B/s")
      }

      "convert to kB/s" in {
        DoubleOps.asSpeed(in = 5630d, 1 second) should be ("5,630 kB/s")
      }

      "convert to MB/s" in {
        DoubleOps.asSpeed(in = 563001000d, 1 second) should be ("563,001 MB/s")
      }

      "convert to GB/s" in {
        DoubleOps.asSpeed(in = 563001000000d, 1 second) should be ("563,001 GB/s")
      }

      "convert to TB/s" in {
        DoubleOps.asSpeed(in = 563001000000000d, 1 second) should be ("563,001 TB/s")
      }

      "convert to PB/s" in {
        DoubleOps.asSpeed(in = 563001000000000000d, 1 second) should be ("563,001 PB/s")
      }

      "convert from milli duration)" in {
        DoubleOps.asSpeed(in = 563d, 1 milli) should be ("563,000 kB/s")
      }
    }
  }
}
