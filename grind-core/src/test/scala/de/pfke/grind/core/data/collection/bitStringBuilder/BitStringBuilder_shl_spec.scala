package de.pfke.grind.core.data.collection.bitStringBuilder

import java.nio.ByteOrder

import akka.util.ByteString
import de.pfke.grind.core.data.collection.{BitStringAlignment, BitStringBuilder}
import org.scalatest.{Matchers, WordSpecLike}

class BitStringBuilder_shl_spec
  extends WordSpecLike
    with Matchers {
  "testing method '<<(Int)' (8bit aligned) (BIG_ENDIAN)" when {
    implicit val byteOrder: ByteOrder = ByteOrder.BIG_ENDIAN
    val alignment = BitStringAlignment._8Bit

    "testing with an empty builder" should {
      val tto = BitStringBuilder.newBuilder(alignment = alignment)
      tto << 7

      "return correct 'lengthBit'" in {
        tto.lengthBit should be (7)
      }

      "result should be correct" in {
        tto.result() should be (ByteString(0x00))
      }
    }

    "testing with an non-empty builder" should {
      val tto = BitStringBuilder.newBuilder(alignment = alignment)
      tto | 0x80.toByte
      tto << 10

      // builder: 02 00 00
      // string:  02 00 00

      "return correct 'lengthBit'" in {
        tto.lengthBit should be (18)
      }

      "result should be correct" in {
        tto.result() should be (ByteString(0x02, 0x00, 0x00))
      }
    }

    "testing with an non-empty builder (über ne long Grenze hinweg)" should {
      val tto = BitStringBuilder.newBuilder(alignment = alignment)
      tto | 0x80.toByte
      tto << 64

      // builder: 80 | 00 00 00 00 | 00 00 00 00
      // string:  80 | 00 00 00 00 | 00 00 00 00

      "return correct 'lengthBit'" in {
        tto.lengthBit should be (72)
      }

      "result should be correct" in {
        tto.result() should be (
          ByteString(
            0x80, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00,
            0x00
          ))
      }
    }

    "testing with an non-empty builder (70 bit, shift um 3)" should {
      val tto = BitStringBuilder.newBuilder(alignment = alignment)
      tto | 0x80.toByte
      tto << 64
      tto << 3

      // builder: 04 00 00 00 00 00 00 00 00 00
      // string:  04 00 00 00 | 00 00 00 00 | 00 00

      "return correct 'lengthBit'" in {
        tto.lengthBit should be (75)
      }

      "result should be correct" in {
        tto.result() should be (
          ByteString(
            0x04, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00,
            0x00, 0x00
          ))
      }
    }

    "testing with an non-empty builder (viele bits)" should {
      val tto = BitStringBuilder.newBuilder(alignment = alignment)
      tto | 0x01.toByte
      tto << 8
      tto | 0x02.toByte
      tto << 8
      tto | 0x03.toByte
      tto << 8
      tto | 0x04.toByte
      tto << 8
      tto | 0x05.toByte
      tto << 8
      tto | 0x06.toByte
      tto << 8
      tto | 0x07.toByte
      tto << 8
      tto | 0x08.toByte
      tto << 8

      // builder: 01 02 03 04 05 06 07 08 00
      // string:  01 02 03 04 05 06 07 08 00

      "return correct 'lengthBit'" in {
        tto.lengthBit should be (72)
      }

      "result should be correct" in {
        tto.result() should be (
          ByteString(
            0x01, 0x02, 0x03, 0x04,
            0x05, 0x06, 0x07, 0x08,
            0x00
          ))
      }
    }
  }

  "testing method '<<(Int)' (16bit aligned) (BIG_ENDIAN)" when {
    implicit val byteOrder: ByteOrder = ByteOrder.BIG_ENDIAN
    val alignment = BitStringAlignment._16Bit

    "testing with an empty builder" should {
      val tto = BitStringBuilder.newBuilder(alignment = alignment)
      tto << 7

      "return correct 'lengthBit'" in {
        tto.lengthBit should be (7)
      }

      "result should be correct" in {
        tto.result() should be (ByteString(0x00, 0x00))
      }
    }

    "testing with an non-empty builder" should {
      val tto = BitStringBuilder.newBuilder(alignment = alignment)
      tto | 0x80.toByte
      tto << 10

      // builder: 02 00 00
      // string:  00 02 | 00 00

      "return correct 'lengthBit'" in {
        tto.lengthBit should be (18)
      }

      "result should be correct" in {
        tto.result() should be (ByteString(0x00, 0x02, 0x00, 0x00))
      }
    }

    "testing with an non-empty builder (über ne long Grenze hinweg)" should {
      val tto = BitStringBuilder.newBuilder(alignment = alignment)
      tto | 0x80.toByte
      tto << 64

      // builder: 80 | 00 00 00 00 00 00 00 00
      // string:  00 80 00 00 | 00 00 00 00 | 00 00

      "return correct 'lengthBit'" in {
        tto.lengthBit should be (72)
      }

      "result should be correct" in {
        tto.result() should be (
          ByteString(
            0x00, 0x80, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00,
            0x00, 0x00
          ))
      }
    }

    "testing with an non-empty builder (70 bit, shift um 3)" should {
      val tto = BitStringBuilder.newBuilder(alignment = alignment)
      tto | 0x80.toByte
      tto << 64
      tto << 3

      // builder: 04 00 00 00 00 00 00 00 00 00
      // string:  04 00 00 00 | 00 00 00 00 | 00 00

      "return correct 'lengthBit'" in {
        tto.lengthBit should be (75)
      }

      "result should be correct" in {
        tto.result() should be (
          ByteString(
            0x04, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00,
            0x00, 0x00
          ))
      }
    }

    "testing with an non-empty builder (viele bits)" should {
      val tto = BitStringBuilder.newBuilder(alignment = alignment)
      tto | 0x01.toByte
      tto << 8
      tto | 0x02.toByte
      tto << 8
      tto | 0x03.toByte
      tto << 8
      tto | 0x04.toByte
      tto << 8
      tto | 0x05.toByte
      tto << 8
      tto | 0x06.toByte
      tto << 8
      tto | 0x07.toByte
      tto << 8
      tto | 0x08.toByte
      tto << 8

      // builder: 01 02 03 04 05 06 07 08 00
      // string:  00 01 02 03 04 05 06 07 08 00

      "return correct 'lengthBit'" in {
        tto.lengthBit should be (72)
      }

      "result should be correct" in {
        tto.result() should be (
          ByteString(
            0x00, 0x01, 0x02, 0x03,
            0x04, 0x05, 0x06, 0x07,
            0x08, 0x00
          ))
      }
    }

    "testing with an non-empty builder (viele bits, speziell für 16bit um rest-Bytes bei der Konvertierung herauszukitzeln, 8byte + 5 byte)" should {
      val tto = BitStringBuilder.newBuilder(alignment = alignment)
      tto | 0x01.toByte
      tto << 64
      tto | 0x02.toByte
      tto << 32

      // builder: 01 0000 0000 | 0000 0002 0000 0000
      // string:  0001 0000 0000 0000 0002 0000 0000

      "return correct 'lengthBit'" in {
        tto.lengthBit should be (104)
      }

      "result should be correct" in {
        tto.result() should be (
          ByteString(
            0x00, 0x01, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00,
            0x00, 0x02, 0x00, 0x00,
            0x00, 0x00
          ))
      }
    }

    "testing with an non-empty builder (viele bits, speziell für 16bit um rest-Bytes bei der Konvertierung herauszukitzeln, 8byte + 6 byte)" should {
      val tto = BitStringBuilder.newBuilder(alignment = alignment)
      tto | 0x01.toByte
      tto << 64
      tto | 0x02.toByte
      tto << 40

      // builder: 0100 0000 0000 | 0000 0200 0000 0000
      // string:  0100 0000 0000 | 0000 0200 0000 0000

      "return correct 'lengthBit'" in {
        tto.lengthBit should be (112)
      }

      "result should be correct" in {
        tto.result() should be (
          ByteString(
            0x01, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00,
            0x02, 0x00, 0x00, 0x00,
            0x00, 0x00
          ))
      }
    }

    "testing with an non-empty builder (viele bits, speziell für 16bit um rest-Bytes bei der Konvertierung herauszukitzeln, 8byte + 7 byte)" should {
      val tto = BitStringBuilder.newBuilder(alignment = alignment)
      tto | 0x01.toByte
      tto << 64
      tto | 0x02.toByte
      tto << 48

      // builder: 01 0000 0000 0000 | 0002 0000 0000 0000
      // string:  0001 0000 0000 0000 0002 0000 0000 0000

      "return correct 'lengthBit'" in {
        tto.lengthBit should be (120)
      }

      "result should be correct" in {
        tto.result() should be (
          ByteString(
            0x00, 0x01, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00,
            0x00, 0x02, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00
          ))
      }
    }

    "testing with an non-empty builder (viele bits, speziell für 16bit um rest-Bytes bei der Konvertierung herauszukitzeln, 8byte + 8 byte)" should {
      val tto = BitStringBuilder.newBuilder(alignment = alignment)
      tto | 0x01.toByte
      tto << 64
      tto | 0x02.toByte
      tto << 56

      // builder: 0100 0000 0000 | 0000 0200 0000 0000
      // string:  0100 0000 0000 | 0000 0200 0000 0000

      "return correct 'lengthBit'" in {
        tto.lengthBit should be (128)
      }

      "result should be correct" in {
        tto.result() should be (
          ByteString(
            0x01, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00,
            0x02, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00
          ))
      }
    }
  }

  "testing method '<<(Int)' (32bit aligned) (BIG_ENDIAN)" when {
    implicit val byteOrder: ByteOrder = ByteOrder.BIG_ENDIAN
    val alignment = BitStringAlignment._32Bit

    "testing with an empty builder" should {
      val tto = BitStringBuilder.newBuilder(alignment = alignment)
      tto << 7

      "return correct 'lengthBit'" in {
        tto.lengthBit should be (7)
      }

      "result should be correct" in {
        tto.result() should be (ByteString(0x00, 0x00, 0x00, 0x00))
      }
    }

    "testing with an non-empty builder" should {
      val tto = BitStringBuilder.newBuilder(alignment = alignment)
      tto | 0x80.toByte
      tto << 10

      "return correct 'lengthBit'" in {
        tto.lengthBit should be (18)
      }

      "result should be correct" in {
        tto.result() should be (ByteString(0x00, 0x02, 0x00, 0x00))
      }
    }

    "testing with an non-empty builder (über ne long Grenze hinweg)" should {
      val tto = BitStringBuilder.newBuilder(alignment = alignment)
      tto | 0x80.toByte
      tto << 64

      // builder: 80 | 00 00 00 | 00 00 00 00 00
      // string:  00 00 00 80 | 00 00 00 00 | 00 00 00 00

      "return correct 'lengthBit'" in {
        tto.lengthBit should be (72)
      }

      "result should be correct" in {
        tto.result() should be (
          ByteString(
            0x00, 0x00, 0x00, 0x80,
            0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00
          ))
      }
    }

    "testing with an non-empty builder (70 bit, shift um 3)" should {
      val tto = BitStringBuilder.newBuilder(alignment = alignment)
      tto | 0x80.toByte
      tto << 64
      tto << 3

      // builder: 04 00 | 00 00 00 00 | 00 00 00 00
      // string:  00 00 04 00 | 00 00 00 00 | 00 00 00 00

      "return correct 'lengthBit'" in {
        tto.lengthBit should be (75)
      }

      "result should be correct" in {
        tto.result() should be (
          ByteString(
            0x00, 0x00, 0x04, 0x00,
            0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00
          ))
      }
    }

    "testing with an non-empty builder (shift um 128)" should {
      val tto = BitStringBuilder.newBuilder(alignment = alignment)
      tto | 0x80.toByte
      tto << 128

      // builder: 04 00 | 00 00 00 00 | 00 00 00 00
      // string:  00 00 04 00 | 00 00 00 00 | 00 00 00 00

      "return correct 'lengthBit'" in {
        tto.lengthBit should be (136)
      }

      "result should be correct" in {
        tto.result() should be (
          ByteString(
            0x00, 0x00, 0x00, 0x80,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
          ))
      }
    }

    "testing with an non-empty builder (viele bits)" should {
      val tto = BitStringBuilder.newBuilder(alignment = alignment)
      tto | 0x01.toByte
      tto << 8
      tto | 0x02.toByte
      tto << 8
      tto | 0x03.toByte
      tto << 8
      tto | 0x04.toByte
      tto << 8
      tto | 0x05.toByte
      tto << 8
      tto | 0x06.toByte
      tto << 8
      tto | 0x07.toByte
      tto << 8
      tto | 0x08.toByte
      tto << 8

      // builder: 01 | 02 03 04 05 | 06 07 08 00
      // string:  00 00 00 01 | 02 03 04 05 | 06 07 08 00

      "return correct 'lengthBit'" in {
        tto.lengthBit should be (72)
      }

      "result should be correct" in {
        tto.result() should be (
          ByteString(
            0x00, 0x00, 0x00, 0x01,
            0x02, 0x03, 0x04, 0x05,
            0x06, 0x07, 0x08, 0x00
          ))
      }
    }
  }
}
