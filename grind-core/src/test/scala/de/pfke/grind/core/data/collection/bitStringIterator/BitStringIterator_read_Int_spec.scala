package de.pfke.grind.core.data.collection.bitStringIterator

import java.nio.ByteOrder

import akka.util.ByteString
import de.pfke.grind.core.data.collection.{BitStringAlignment, BitStringIterator}
import org.scalatest.{Matchers, WordSpecLike}

class BitStringIterator_read_Int_spec
  extends WordSpecLike
    with Matchers {
  "testing method 'read(Int)' with 8 bit aligned" when {
    implicit val byteOrder: ByteOrder = ByteOrder.BIG_ENDIAN

    val inData = ByteString(
      0x81, 0x02, 0x03, 0x04,
      0x05, 0x06, 0x07, 0x08,
      0x89, 0x0a, 0x0b, 0x0c,
      0x0d, 0x0e, 0x0f, 0x11,
      0x11, 0x12, 0x13, 0x15
    )

    "read out 8bit-wise" should {
      val tto = BitStringIterator(inData, BitStringAlignment._8Bit)

      "should read correct" in {
        withClue(s" 1. chunk") { tto.read(8) should be(0x81) }
        withClue(s" 2. chunk") { tto.read(8) should be(0x02) }
        withClue(s" 3. chunk") { tto.read(8) should be(0x03) }
        withClue(s" 4. chunk") { tto.read(8) should be(0x04) }
        withClue(s" 5. chunk") { tto.read(8) should be(0x05) }
        withClue(s" 6. chunk") { tto.read(8) should be(0x06) }
        withClue(s" 7. chunk") { tto.read(8) should be(0x07) }
        withClue(s" 8. chunk") { tto.read(8) should be(0x08) }
        withClue(s" 9. chunk") { tto.read(8) should be(0x89) }
        withClue(s"10. chunk") { tto.read(8) should be(0x0a) }
        withClue(s"11. chunk") { tto.read(8) should be(0x0b) }
        withClue(s"12. chunk") { tto.read(8) should be(0x0c) }
        withClue(s"13. chunk") { tto.read(8) should be(0x0d) }
        withClue(s"14. chunk") { tto.read(8) should be(0x0e) }
        withClue(s"15. chunk") { tto.read(8) should be(0x0f) }
        withClue(s"16. chunk") { tto.read(8) should be(0x11) }
        withClue(s"17. chunk") { tto.read(8) should be(0x11) }
        withClue(s"18. chunk") { tto.read(8) should be(0x12) }
        withClue(s"19. chunk") { tto.read(8) should be(0x13) }
        withClue(s"20. chunk") { tto.read(8) should be(0x15) }
      }
    }

    "read out 16bit-wise" should {
      val tto = BitStringIterator(inData, BitStringAlignment._8Bit)

      "should read correct" in {
        withClue(s" 1. chunk") { tto.read(16) should be(0x8102) }
        withClue(s" 2. chunk") { tto.read(16) should be(0x0304) }
        withClue(s" 3. chunk") { tto.read(16) should be(0x0506) }
        withClue(s" 4. chunk") { tto.read(16) should be(0x0708) }
        withClue(s" 5. chunk") { tto.read(16) should be(0x890a) }
        withClue(s" 6. chunk") { tto.read(16) should be(0x0b0c) }
        withClue(s" 7. chunk") { tto.read(16) should be(0x0d0e) }
        withClue(s" 8. chunk") { tto.read(16) should be(0x0f11) }
        withClue(s" 9. chunk") { tto.read(16) should be(0x1112) }
        withClue(s"10. chunk") { tto.read(16) should be(0x1315) }
      }
    }

    "read out 32bit-wise" should {
      val tto = BitStringIterator(inData, BitStringAlignment._8Bit)
      "should read correct" in {
        withClue(s" 1. chunk") { tto.read(32) should be(0x81020304l) }
        withClue(s" 2. chunk") { tto.read(32) should be(0x05060708l) }
        withClue(s" 3. chunk") { tto.read(32) should be(0x890a0b0cl) }
        withClue(s" 4. chunk") { tto.read(32) should be(0x0d0e0f11l) }
        withClue(s" 5. chunk") { tto.read(32) should be(0x11121315l) }
      }
    }
  }

  "testing method 'read(Int)' with 16 bit aligned" when {
    implicit val byteOrder: ByteOrder = ByteOrder.BIG_ENDIAN

    val inData = ByteString(
      0x81, 0x02,
      0x03, 0x04,
      0x05, 0x06,
      0x07, 0x08,
      0x89, 0x0a,
      0x0b, 0x0c,
      0x0d, 0x0e,
      0x0f, 0x11,
      0x11, 0x12,
      0x13, 0x15
    )

    "read out 8bit-wise" should {
      val tto = BitStringIterator(inData, BitStringAlignment._16Bit)

      "should read correct" in {
        withClue(s" 1. byte") { tto.read(8) should be(0x81) }
        withClue(s" 2. byte") { tto.read(8) should be(0x02) }
        withClue(s" 3. byte") { tto.read(8) should be(0x03) }
        withClue(s" 4. byte") { tto.read(8) should be(0x04) }
        withClue(s" 5. byte") { tto.read(8) should be(0x05) }
        withClue(s" 6. byte") { tto.read(8) should be(0x06) }
        withClue(s" 7. byte") { tto.read(8) should be(0x07) }
        withClue(s" 8. byte") { tto.read(8) should be(0x08) }
        withClue(s" 9. byte") { tto.read(8) should be(0x89) }
        withClue(s"10. byte") { tto.read(8) should be(0x0a) }
        withClue(s"11. byte") { tto.read(8) should be(0x0b) }
        withClue(s"12. byte") { tto.read(8) should be(0x0c) }
        withClue(s"13. byte") { tto.read(8) should be(0x0d) }
        withClue(s"14. byte") { tto.read(8) should be(0x0e) }
        withClue(s"15. byte") { tto.read(8) should be(0x0f) }
        withClue(s"16. byte") { tto.read(8) should be(0x11) }
        withClue(s"17. byte") { tto.read(8) should be(0x11) }
        withClue(s"18. byte") { tto.read(8) should be(0x12) }
        withClue(s"19. byte") { tto.read(8) should be(0x13) }
        withClue(s"20. byte") { tto.read(8) should be(0x15) }
      }
    }

    "read out 16bit-wise" should {
      val tto = BitStringIterator(inData, BitStringAlignment._16Bit)

      "should read correct" in {
        withClue(s" 1. chunk") { tto.read(16) should be(0x8102) }
        withClue(s" 2. chunk") { tto.read(16) should be(0x0304) }
        withClue(s" 3. chunk") { tto.read(16) should be(0x0506) }
        withClue(s" 4. chunk") { tto.read(16) should be(0x0708) }
        withClue(s" 5. chunk") { tto.read(16) should be(0x890a) }
        withClue(s" 6. chunk") { tto.read(16) should be(0x0b0c) }
        withClue(s" 7. chunk") { tto.read(16) should be(0x0d0e) }
        withClue(s" 8. chunk") { tto.read(16) should be(0x0f11) }
        withClue(s" 9. chunk") { tto.read(16) should be(0x1112) }
        withClue(s"10. chunk") { tto.read(16) should be(0x1315) }
      }
    }

    "read out 32bit-wise" should {
      val tto = BitStringIterator(inData, BitStringAlignment._16Bit)

      "should read correct" in {
        withClue(s" 1. chunk") { tto.read(32) should be(0x81020304l) }
        withClue(s" 2. chunk") { tto.read(32) should be(0x05060708l) }
        withClue(s" 3. chunk") { tto.read(32) should be(0x890a0b0cl) }
        withClue(s" 4. chunk") { tto.read(32) should be(0x0d0e0f11l) }
        withClue(s" 5. chunk") { tto.read(32) should be(0x11121315l) }
      }
    }
  }

  "testing method 'read(Int)' with 32 bit aligned" when {
    implicit val byteOrder: ByteOrder = ByteOrder.BIG_ENDIAN

    val inData = ByteString(
      0x81, 0x02, 0x03, 0x04,
      0x05, 0x06, 0x07, 0x08,
      0x89, 0x0a, 0x0b, 0x0c,
      0x0d, 0x0e, 0x0f, 0x11,
      0x11, 0x12, 0x13, 0x15
    )

    "read out 8bit-wise" should {
      val tto = BitStringIterator(inData, BitStringAlignment._32Bit)

      "should read correct" in {
        withClue(s" 1. byte") { tto.read(8) should be (0x81) }
        withClue(s" 2. byte") { tto.read(8) should be (0x02) }
        withClue(s" 3. byte") { tto.read(8) should be (0x03) }
        withClue(s" 4. byte") { tto.read(8) should be (0x04) }
        withClue(s" 5. byte") { tto.read(8) should be (0x05) }
        withClue(s" 6. byte") { tto.read(8) should be (0x06) }
        withClue(s" 7. byte") { tto.read(8) should be (0x07) }
        withClue(s" 8. byte") { tto.read(8) should be (0x08) }
        withClue(s" 9. byte") { tto.read(8) should be (0x89) }
        withClue(s"10. byte") { tto.read(8) should be (0x0a) }
        withClue(s"11. byte") { tto.read(8) should be (0x0b) }
        withClue(s"12. byte") { tto.read(8) should be (0x0c) }
        withClue(s"13. byte") { tto.read(8) should be (0x0d) }
        withClue(s"14. byte") { tto.read(8) should be (0x0e) }
        withClue(s"15. byte") { tto.read(8) should be (0x0f) }
        withClue(s"16. byte") { tto.read(8) should be (0x11) }
        withClue(s"17. byte") { tto.read(8) should be (0x11) }
        withClue(s"18. byte") { tto.read(8) should be (0x12) }
        withClue(s"19. byte") { tto.read(8) should be (0x13) }
        withClue(s"20. byte") { tto.read(8) should be (0x15) }
      }
    }

    "read out 16bit-wise" should {
      val tto = BitStringIterator(inData, BitStringAlignment._32Bit)

      "should read correct" in {
        withClue(s" 1. short") { tto.read(16) should be (0x8102) }
        withClue(s" 2. short") { tto.read(16) should be (0x0304) }
        withClue(s" 3. short") { tto.read(16) should be (0x0506) }
        withClue(s" 4. short") { tto.read(16) should be (0x0708) }
        withClue(s" 5. short") { tto.read(16) should be (0x890a) }
        withClue(s" 6. short") { tto.read(16) should be (0x0b0c) }
        withClue(s" 7. short") { tto.read(16) should be (0x0d0e) }
        withClue(s" 8. short") { tto.read(16) should be (0x0f11) }
        withClue(s" 9. short") { tto.read(16) should be (0x1112) }
        withClue(s"10. short") { tto.read(16) should be (0x1315) }
      }
    }

    "read out 32bit-wise" should {
      val tto = BitStringIterator(inData, BitStringAlignment._32Bit)

      "should read correct" in {
        withClue(s" 1. int") { tto.read(32) should be (0x81020304l) }
        withClue(s" 2. int") { tto.read(32) should be (0x05060708l) }
        withClue(s" 3. int") { tto.read(32) should be (0x890a0b0cl) }
        withClue(s" 4. int") { tto.read(32) should be (0x0d0e0f11l) }
        withClue(s" 5. int") { tto.read(32) should be (0x11121315l) }
      }
    }

    "read out 7bit-wise" should {
      val tto = BitStringIterator(inData, BitStringAlignment._32Bit)

      // 0x81, 0x02, 0x03, 0x04
      // 1000 0001 0000 0010 0000 0011 0000 0100
      //
      // 0x05, 0x06, 0x07, 0x08
      // 0000 0101 0000 0110 0000 0111 0000 1000
      //
      // 0x89, 0x0a, 0x0b, 0x0c,
      // 1000 1001 0000 1010 0000 1011 0000 1100
      //
      // 0x0d, 0x0e, 0x0f, 0x11
      // 0000 1101 0000 1110 0000 1111 0001 0001
      //
      // 0x11, 0x12, 0x13, 0x15
      // 0001 0001 0001 0010 0001 0011 0001 0101
      //
      // Zusammen
      // 8Bit:
      //   bin   1000 0001 0000 0010 0000 0011 0000 0100 0000 0101 0000 0110 0000 0111 0000 1000 1000 1001 0000 1010 0000 1011 0000 1100 0000 1101 0000 1110 0000 1111 0001 0001 0001 0001 0001 0010 0001 0011 0001 0101
      //   hex
      // 7Bit:
      //   bin   1000000 1000000 1000000 0110000 0100000 0010100 0001100 0000111 0000100 0100010 0100001 0100000 1011000 0110000 0011010 0001110 0000111 1000100 0100010 0010001 0010000 1001100 010101
      //   hex      0x40    0x40    0s40    0x30    0x20    0x14    0x0c    0x07    0x04    0x22    0x21    0x20    0x58    0x30    0x1a    0x0e    0x07    0x44    0x22    0x11    0x10    0x4c   0x15
      //   no.         1       2       3       4       5       6       7       8       9      10      11      12      13      14      15      16      17      18      19      20      21      22     23

      "should read correct" in {
        withClue(s" 1. 7bit") { tto.read(7) should be (0x40) }
        withClue(s" 2. 7bit") { tto.read(7) should be (0x40) }
        withClue(s" 3. 7bit") { tto.read(7) should be (0x40) }
        withClue(s" 4. 7bit") { tto.read(7) should be (0x30) }
        withClue(s" 5. 7bit") { tto.read(7) should be (0x20) }
        withClue(s" 6. 7bit") { tto.read(7) should be (0x14) }
        withClue(s" 7. 7bit") { tto.read(7) should be (0x0c) }
        withClue(s" 8. 7bit") { tto.read(7) should be (0x07) }
        withClue(s" 9. 7bit") { tto.read(7) should be (0x04) }
        withClue(s"10. 7bit") { tto.read(7) should be (0x22) }
        withClue(s"11. 7bit") { tto.read(7) should be (0x21) }
        withClue(s"12. 7bit") { tto.read(7) should be (0x20) }
        withClue(s"13. 7bit") { tto.read(7) should be (0x58) }
        withClue(s"14. 7bit") { tto.read(7) should be (0x30) }
        withClue(s"15. 7bit") { tto.read(7) should be (0x1a) }
        withClue(s"16. 7bit") { tto.read(7) should be (0x0e) }
        withClue(s"17. 7bit") { tto.read(7) should be (0x07) }
        withClue(s"18. 7bit") { tto.read(7) should be (0x44) }
        withClue(s"19. 7bit") { tto.read(7) should be (0x22) }
        withClue(s"20. 7bit") { tto.read(7) should be (0x11) }
        withClue(s"21. 7bit") { tto.read(7) should be (0x10) }
        withClue(s"22. 7bit") { tto.read(7) should be (0x4c) }
        withClue(s"23. 6bit") { tto.read(6) should be (0x15) }
      }
    }
  }
}
