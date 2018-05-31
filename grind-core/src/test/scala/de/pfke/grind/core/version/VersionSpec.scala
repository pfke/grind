package de.pfke.grind.core.version

import org.scalatest.{Matchers, WordSpec}

class VersionSpec
  extends WordSpec
  with Matchers {
  "implicit def apply(:String)" should {
    "convert 1.5" in {
      Version("1.5").isInstanceOf[TwoNumberVersion] should be(right = true)
    }

    "convert 1.5-13" in {
      Version("1.5-13").isInstanceOf[PatchLevelVersion] should be(right = true)
    }
  }
}
