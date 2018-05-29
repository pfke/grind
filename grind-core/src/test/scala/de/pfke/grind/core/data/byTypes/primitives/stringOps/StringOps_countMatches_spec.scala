package de.pfke.grind.core.data.byTypes.primitives.stringOps

import de.pfke.grind.core.data.byTypes.primitives.StringOps
import org.scalatest.{Matchers, WordSpecLike}

class StringOps_countMatches_spec
  extends WordSpecLike
  with Matchers {
  "on implicit class StringUtils" when {
    "using method 'countMatches'" should {
      "count 0" in {
        StringOps.countMatches(in = "so", toMatch = "os") should be (0)
      }
      "return 0, if s1 is empty" in {
        StringOps.countMatches(in = "", toMatch = "os") should be (0)
      }
      "return 0, if s2 is empty" in {
        StringOps.countMatches(in = "frfrrf", toMatch = "") should be (0)
      }
      "return correct, if s1+s2 are equal" in {
        StringOps.countMatches(in = "sdfres", toMatch = "sdfres") should be (6)
      }
      "return correct, if s1.length < s2.length" in {
        StringOps.countMatches(in = "sdf", toMatch = "sdfres") should be (3)
      }
      "return correct, if s1.length > s2.length" in {
        StringOps.countMatches(in = "sdfdesde", toMatch = "sdf") should be (3)
      }
      "return correct, if s1(x) != s2(x) " in {
        StringOps.countMatches(in = "sdfdesde", toMatch = "sdfeffrfr") should be (3)
      }
    }
  }
}
