package de.pfke.grind.core.data.collection.bitStringAlignment

import de.pfke.grind.core.data.collection.BitStringAlignment
import org.scalatest.{Matchers, WordSpecLike}

class BitStringAlignment_spec
  extends WordSpecLike
    with Matchers {
  "testing the enum ids" when {
    "using the _xByte values" should {
      "1Byte should be 1" in {
        BitStringAlignment._8Bit.id should be (8)
      }

      "2Byte should be 1" in {
        BitStringAlignment._16Bit.id should be (16)
      }

      "4Byte should be 1" in {
        BitStringAlignment._32Bit.id should be (32)
      }
    }
  }
}
