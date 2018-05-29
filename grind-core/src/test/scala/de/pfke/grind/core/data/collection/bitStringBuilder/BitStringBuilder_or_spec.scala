package de.pfke.grind.core.data.collection.bitStringBuilder

import java.nio.ByteOrder

import akka.util.ByteString
import de.pfke.grind.core.data.collection.{BitStringAlignment, BitStringBuilder}
import org.scalatest.{Matchers, WordSpecLike}

class BitStringBuilder_or_spec
  extends WordSpecLike
    with Matchers {
  "testing method '|(Byte)' (32bit aligned)" when {
    "testing with an empty builder" should {
      implicit val byteOrder: ByteOrder = ByteOrder.BIG_ENDIAN
      val alignment = BitStringAlignment._32Bit

      val tto = BitStringBuilder.newBuilder(alignment = alignment)
      tto | 0x01.toByte

      "return correct 'lengthBit'" in {
        tto.lengthBit should be (8)
      }

      "result should be correct" in {
        tto.result() should be (ByteString(0x00, 0x00, 0x00, 0x01))
      }
    }

    "testing with an non-empty builder" should {
      implicit val byteOrder: ByteOrder = ByteOrder.BIG_ENDIAN

      val tto = BitStringBuilder.newBuilder()
      tto | 0x80.toByte
      tto | 0x03.toByte

      "return correct 'lengthBit'" in {
        tto.lengthBit should be (8)
      }

      "result should be correct" in {
        tto.result() should be (ByteString(0x00, 0x00, 0x00, 0x83))
      }
    }
  }
}
