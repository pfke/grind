package de.pfke.grind.core.data.collection.bitStringBuilder

import java.nio.ByteOrder

import akka.util.ByteString
import de.pfke.grind.core.data.collection.{BitStringAlignment, BitStringBuilder}
import org.scalatest.{Matchers, WordSpecLike}

class BitStringBuilder_result_spec
  extends WordSpecLike
    with Matchers {
  "testing error in other specs" when {
    "using the test for aligned bits" should {
      implicit val byteOrder: ByteOrder = ByteOrder.BIG_ENDIAN
      val tto = BitStringBuilder.newBuilder(alignment = BitStringAlignment._32Bit)

      tto.appendBits(bits = 8, value = 0x81)
      tto.appendBits(bits = 16, value = 0x7e7e)
      tto.appendBits(bits = 8, value = 0x32)
      tto.appendBits(bits = 32, value = 0x12345678)
      tto.appendBits(bits = 16, value = 0)
      tto.appendBits(bits = 8, value = 0x9a)
      tto.appendBits(bits = 8, value = 0xbc)

      "result should be correct" in {
        tto.result() should be(ByteString(
          0x81, 0x7e, 0x7e, 0x32,
          0x12, 0x34, 0x56, 0x78,
          0x00, 0x00, 0x9a, 0xbc
        ))
      }
    }
  }
}
