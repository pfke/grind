package de.pfke.grind.core.refl.sizeOf

import de.pfke.grind.core.refl.SizeOf
import org.scalatest.{Matchers, WordSpec}

class sizeOf_consts_spec
  extends WordSpec
    with Matchers {
  "testing sizeOf constants" when {
    "query constant variables" should {
      "'Boolean' should have correct size" in {
        SizeOf.Boolean should be (1)
      }

      "'Byte' should have correct size" in {
        SizeOf.Byte should be (1)
      }

      "'Char' should have correct size" in {
        SizeOf.Char should be (2)
      }

      "'Double' should have correct size" in {
        SizeOf.Double should be (8)
      }

      "'Float' should have correct size" in {
        SizeOf.Float should be (4)
      }

      "'Int' should have correct size" in {
        SizeOf.Int should be (4)
      }

      "'Long' should have correct size" in {
        SizeOf.Long should be (8)
      }

      "'Short' should have correct size" in {
        SizeOf.Short should be (2)
      }
    }
  }
}
