package de.pfke.grind.core.data.collection.bitStringIterator

import java.nio.ByteOrder

import akka.util.ByteString
import de.pfke.grind.core.data.collection.{BitStringAlignment, BitStringIterator}
import org.scalatest.{Matchers, WordSpecLike}

class BitStringIterator_apply_ByteString_BitStringAlignment_spec
  extends WordSpecLike
    with Matchers {
  "testing method 'apply(ByteString, BitStringAlignment)" when {
    implicit val byteOrder: ByteOrder = ByteOrder.BIG_ENDIAN

    "given ByteString is unaligned" should {
      "16Bit alignment with 1Byte input should throw an exception" in {
        an[IllegalArgumentException] shouldBe thrownBy(BitStringIterator(ByteString(0x01), BitStringAlignment._16Bit).read(8))
      }

      "16Bit alignment with 3Byte input should throw an exception" in {
        an[IllegalArgumentException] shouldBe thrownBy(BitStringIterator(ByteString(0x01, 0x02, 0x03), BitStringAlignment._16Bit).read(24))
      }

      "32Bit alignment with 1Byte input should throw an exception" in {
        an[IllegalArgumentException] shouldBe thrownBy(BitStringIterator(ByteString(0x01), BitStringAlignment._32Bit).read(8))
      }

      "32Bit alignment with 2Byte input should throw an exception" in {
        an[IllegalArgumentException] shouldBe thrownBy(BitStringIterator(ByteString(0x01, 0x02), BitStringAlignment._32Bit).read(8))
      }

      "32Bit alignment with 3Byte input should throw an exception" in {
        an[IllegalArgumentException] shouldBe thrownBy(BitStringIterator(ByteString(0x01, 0x02, 0x03), BitStringAlignment._32Bit).read(8))
      }

      "32Bit alignment with 5Byte input should throw an exception" in {
        an[IllegalArgumentException] shouldBe thrownBy(BitStringIterator(ByteString(0x01, 0x02, 0x03, 0x04, 0x05), BitStringAlignment._32Bit).read(40))
      }
    }
  }
}
