package de.pfke.grind.core.data.collection.anythingIterator

import java.nio.ByteOrder

import akka.util.ByteString
import de.pfke.grind.core.data.collection.{AnythingIterator, BitStringAlignment}
import de.pfke.grind.core.data.length.digital.{BitLength, ByteLength}
import org.scalatest.{Matchers, WordSpecLike}

class AnythingIteratorSpec
  extends WordSpecLike
    with Matchers {
  "testing exception on method 'read(DigitalLength)' (with BitLength)" when {
    implicit val byteOrder = ByteOrder.BIG_ENDIAN

    "passing an empty iterator" should {
      val tto = AnythingIterator(ByteString().iterator)

      "should throw an exception on Boolean" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Boolean](BitLength(1 * 8)))
      }

      "should throw an exception on Byte" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Byte](BitLength(1 * 8)))
      }

      "should throw an exception on Char" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Char](BitLength(2 * 8)))
      }

      "should throw an exception on Double" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Double](BitLength(8 * 8)))
      }

      "should throw an exception on Float" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Float](BitLength(4 * 8)))
      }

      "should throw an exception on Int" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Int](BitLength(4 * 8)))
      }

      "should throw an exception on Long" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Long](BitLength(8 * 8)))
      }

      "should throw an exception on Short" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Short](BitLength(2 * 8)))
      }
    }

    "Iterator leer lesen" should {
      val tto = AnythingIterator(ByteString(1).iterator)

      "should throw an exception on Char" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Char](BitLength(2 * 8)))
      }

      "should throw an exception on Double" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Double](BitLength(8 * 8)))
      }

      "should throw an exception on Float" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Float](BitLength(4 * 8)))
      }

      "should throw an exception on Int" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Int](BitLength(4 * 8)))
      }

      "should throw an exception on Long" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Long](BitLength(8 * 8)))
      }

      "should throw an exception on Short" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Short](BitLength(2 * 8)))
      }
    }
  }

  "testing exception on method 'read(DigitalLength)' (with matching ByteLength)" when {
    implicit val byteOrder = ByteOrder.BIG_ENDIAN

    "passing an empty iterator" should {
      val tto = AnythingIterator(ByteString().iterator)

      "should throw an exception on Boolean" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Boolean](ByteLength(1)))
      }

      "should throw an exception on Byte" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Byte](ByteLength(1)))
      }

      "should throw an exception on Char" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Char](ByteLength(2)))
      }

      "should throw an exception on Double" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Double](ByteLength(8)))
      }

      "should throw an exception on Float" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Float](ByteLength(4)))
      }

      "should throw an exception on Int" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Int](ByteLength(4)))
      }

      "should throw an exception on Long" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Long](ByteLength(8)))
      }

      "should throw an exception on Short" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Short](ByteLength(2)))
      }
    }

    "Iterator leer lesen" should {
      val tto = AnythingIterator(ByteString(1).iterator)

      "should throw an exception on Char" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Char](ByteLength(2)))
      }

      "should throw an exception on Double" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Double](ByteLength(8)))
      }

      "should throw an exception on Float" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Float](ByteLength(4)))
      }

      "should throw an exception on Int" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Int](ByteLength(4)))
      }

      "should throw an exception on Long" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Long](ByteLength(8)))
      }

      "should throw an exception on Short" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Short](ByteLength(2)))
      }
    }
  }

  "testing exception on method 'read(DigitalLength)' (with non-matching ByteLength)" when {
    implicit val byteOrder = ByteOrder.BIG_ENDIAN

    "passing an empty iterator" should {
      val tto = AnythingIterator(ByteString().iterator)

      "should throw an exception on Boolean" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Boolean](ByteLength(2)))
      }

      "should throw an exception on Byte" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Byte](ByteLength(2)))
      }

      "should throw an exception on Char" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Char](ByteLength(7)))
      }

      "should throw an exception on Double" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Double](ByteLength(4)))
      }

      "should throw an exception on Float" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Float](ByteLength(8)))
      }

      "should throw an exception on Int" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Int](ByteLength(1)))
      }

      "should throw an exception on Long" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Long](ByteLength(3)))
      }

      "should throw an exception on Short" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Short](ByteLength(1)))
      }
    }

    "Iterator leer lesen" should {
      val tto = AnythingIterator(ByteString(1).iterator)

      "should throw an exception on Char" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Char](ByteLength(4)))
      }

      "should throw an exception on Double" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Double](ByteLength(6)))
      }

      "should throw an exception on Float" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Float](ByteLength(41)))
      }

      "should throw an exception on Int" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Int](ByteLength(43)))
      }

      "should throw an exception on Long" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Long](ByteLength(3)))
      }

      "should throw an exception on Short" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.read[Short](ByteLength(7)))
      }
    }
  }

  "testing read on certain types" when {
    implicit val byteOrder = ByteOrder.BIG_ENDIAN

    "type Boolean" should {
      "should read correct on method 'read(BitLength)' (matching length, check result)" in {
        AnythingIterator(ByteString(0x01, 0x03, 0x45)).iterator(BitStringAlignment._8Bit).read[Boolean](BitLength(8)) shouldBe (right= true)
      }

      "should read correct on method 'read(BitLength)' (matching length, check iter)" in {
        val iter = AnythingIterator(ByteString(0x01, 0x03, 0x45)).iterator(BitStringAlignment._8Bit)
        iter.read[Boolean](BitLength(8))
        iter.len should be(ByteLength(2))
      }

      "should read correct on method 'read(ByteLength)' (matching length)" in {
        AnythingIterator(ByteString(0x01, 0x03, 0x45).iterator).read[Boolean](ByteLength(1)) shouldBe (right= true)
      }

      "should read correct on method 'read(ByteLength)' (non-matching length, check result)" in {
        AnythingIterator(ByteString(0x01, 0x03, 0x45).iterator).read[Boolean](ByteLength(2)) shouldBe (right= true)
      }

      "should read correct on method 'read(ByteLength)' (non-matching length, check iter)" in {
        val iter = AnythingIterator(ByteString(0x01, 0x03, 0x45).iterator)
        iter.read[Boolean](ByteLength(2))
        iter.len should be(ByteLength(1))
      }
    }

    "type Byte" should {
      "should read correct on method 'read(BitLength)' (matching length, check result)" in {
        AnythingIterator(ByteString(0x01, 0x03, 0x45)).iterator(BitStringAlignment._8Bit).read[Byte](BitLength(8)) should be (0x01)
      }

      "should read correct on method 'read(BitLength)' (matching length, check iter)" in {
        val iter = AnythingIterator(ByteString(0x01, 0x03, 0x45)).iterator(BitStringAlignment._8Bit)
        iter.read[Byte](BitLength(7))
        iter.len should be(ByteLength(2))
      }

      "should read correct on method 'read(ByteLength)' (matching length)" in {
        AnythingIterator(ByteString(0x88, 0x03, 0x45).iterator).read[Byte](ByteLength(1)) should be (0x88.toByte)
      }

      "should read correct on method 'read(ByteLength)' (non-matching length, check result)" in {
        AnythingIterator(ByteString(0x01, 0x03, 0x45).iterator).read[Byte](ByteLength(2)) should be (0x01)
      }

      "should read correct on method 'read(ByteLength)' (non-matching length, check iter)" in {
        val iter = AnythingIterator(ByteString(0x01, 0x03, 0x45).iterator)
        iter.read[Byte](ByteLength(2))
        iter.len should be(ByteLength(1))
      }
    }

    "type Char" should {
      "should read correct on method 'read(BitLength)' (matching length, check result)" in {
        AnythingIterator(ByteString(0x01, 0x03, 0x45)).iterator(BitStringAlignment._16Bit).read[Char](BitLength(16)) should be (0x0103)
      }

      "should read correct on method 'read(BitLength)' (matching length, check iter)" in {
        val iter = AnythingIterator(ByteString(0x01, 0x03, 0x45)).iterator(BitStringAlignment._16Bit)
        iter.read[Char](BitLength(16))
        iter.len should be(ByteLength(1))
      }

      "should read correct on method 'read(ByteLength)' (matching length)" in {
        AnythingIterator(ByteString(0x88, 0x03, 0x45).iterator).read[Char](ByteLength(2)) should be (0x8803.toChar)
      }

      "should read correct on method 'read(ByteLength)' (non-matching length, check result)" in {
        AnythingIterator(ByteString(0x01, 0x03, 0x45).iterator).read[Char](ByteLength(1)) should be (0x01.toChar)
      }

      "should read correct on method 'read(ByteLength)' (non-matching length, check iter)" in {
        val iter = AnythingIterator(ByteString(0x01, 0x03, 0x45).iterator)
        iter.read[Char](ByteLength(1))
        iter.len should be(ByteLength(2))
      }
    }

    "type Short" should {
      "should read correct on method 'read(BitLength)' (matching length, check result)" in {
        AnythingIterator(ByteString(0x01, 0x03, 0x45)).iterator(BitStringAlignment._16Bit).read[Short](BitLength(16)) should be (0x0103)
      }

      "should read correct on method 'read(BitLength)' (matching length, check iter)" in {
        val iter = AnythingIterator(ByteString(0x01, 0x03, 0x45)).iterator(BitStringAlignment._16Bit)
        iter.read[Short](BitLength(16))
        iter.len should be(ByteLength(1))
      }

      "should read correct on method 'read(ByteLength)' (matching length)" in {
        AnythingIterator(ByteString(0x88, 0x03, 0x45).iterator).read[Short](ByteLength(2)) should be (0x8803.toShort)
      }

      "should read correct on method 'read(ByteLength)' (non-matching length, check result)" in {
        AnythingIterator(ByteString(0x01, 0x03, 0x45).iterator).read[Short](ByteLength(1)) should be (0x01.toShort)
      }

      "should read correct on method 'read(ByteLength)' (non-matching length, check iter)" in {
        val iter = AnythingIterator(ByteString(0x01, 0x03, 0x45).iterator)
        iter.read[Short](ByteLength(1))
        iter.len should be(ByteLength(2))
      }
    }

    "type Int" should {
      "should read correct on method 'read(BitLength)' (matching length, check result)" in {
        AnythingIterator(ByteString(0x01, 0x03, 0x45, 0x45, 0x21).iterator).read[Int](BitLength(32)) should be (0x01034545)
      }

      "should read correct on method 'read(BitLength)' (matching length, check iter)" in {
        val iter = AnythingIterator(ByteString(0x00, 0x02, 0x01, 0x03, 0x45).iterator)
        iter.read[Int](BitLength(32))
        iter.len should be(ByteLength(1))
      }

      "should read correct on method 'read(ByteLength)' (matching length)" in {
        AnythingIterator(ByteString(0x88, 0x03, 0x45, 0x03, 0x45).iterator).read[Int](ByteLength(4)) should be (0x88034503)
      }

      "should read correct on method 'read(ByteLength)' (non-matching length, check result)" in {
        AnythingIterator(ByteString(0x01, 0x03, 0x45).iterator).read[Int](ByteLength(1)) should be (0x01)
      }

      "should read correct on method 'read(ByteLength)' (non-matching length, check iter)" in {
        val iter = AnythingIterator(ByteString(0x01, 0x03, 0x45, 0x01, 0x03, 0x45).iterator)
        iter.read[Int](ByteLength(1))
        iter.len should be(ByteLength(5))
      }
    }

    "type Float" should {
      "should read correct on method 'read(BitLength)' (matching length, check result)" in {
        AnythingIterator(ByteString(0x01, 0x03, 0x45, 0x45, 0x21).iterator).read[Float](BitLength(32)) should be (0x01034545.toFloat)
      }

      "should read correct on method 'read(BitLength)' (matching length, check iter)" in {
        val iter = AnythingIterator(ByteString(0x00, 0x02, 0x01, 0x03, 0x45).iterator)
        iter.read[Float](BitLength(32))
        iter.len should be(ByteLength(1))
      }

      "should read correct on method 'read(ByteLength)' (matching length)" in {
        AnythingIterator(ByteString(0x88, 0x03, 0x45, 0x03, 0x45).iterator).read[Float](ByteLength(4)) should be (-3.95025E-34.toFloat)
      }

      "should read correct on method 'read(ByteLength)' (non-matching length, check result)" in {
        AnythingIterator(ByteString(0x01, 0x03, 0x45).iterator).read[Float](ByteLength(1)) should be (0x01)
      }

      "should read correct on method 'read(ByteLength)' (non-matching length, check iter)" in {
        val iter = AnythingIterator(ByteString(0x01, 0x03, 0x45, 0x01, 0x03, 0x45).iterator)
        iter.read[Float](ByteLength(1))
        iter.len should be(ByteLength(5))
      }
    }

    "type Long" should {
      "should read correct on method 'read(BitLength)' (matching length, check result)" in {
        AnythingIterator(ByteString(0x01, 0x03, 0x45, 0x45, 0x21, 0x21, 0x54, 0x12, 0x21, 0x54).iterator).read[Long](BitLength(64)) should be (0x0103454521215412l)
      }

      "should read correct on method 'read(BitLength)' (matching length, check iter)" in {
        val iter = AnythingIterator(ByteString(0x00, 0x02, 0x01, 0x03, 0x45, 0x21, 0x54, 0x12, 0x21, 0x54, 0x21, 0x54, 0x12, 0x21, 0x54).iterator)
        iter.read[Long](BitLength(64))
        iter.len should be(ByteLength(7))
      }

      "should read correct on method 'read(ByteLength)' (matching length)" in {
        AnythingIterator(ByteString(0x88, 0x03, 0x45, 0x03, 0x45, 0x21, 0x54, 0x12, 0x21, 0x54).iterator).read[Long](ByteLength(8)) should be (0x8803450345215412l)
      }

      "should read correct on method 'read(ByteLength)' (matching length #2)" in {
        AnythingIterator(ByteString(0x88, 0x03, 0x45, 0x03, 0x45, 0x21, 0x54, 0x12, 0x21, 0x54).iterator).read[Long](ByteLength(4)) should be (0x88034503.toLong)
      }

      "should read correct on method 'read(ByteLength)' (non-matching length, check result)" in {
        AnythingIterator(ByteString(0x01, 0x03, 0x45, 0x21, 0x54, 0x12, 0x21, 0x54).iterator).read[Long](ByteLength(1)) should be (0x01)
      }

      "should read correct on method 'read(ByteLength)' (non-matching length, check iter)" in {
        val iter = AnythingIterator(ByteString(0x01, 0x03, 0x45, 0x01, 0x03, 0x45, 0x21, 0x54, 0x12, 0x21, 0x54).iterator)
        iter.read[Long](ByteLength(1))
        iter.len should be(ByteLength(10))
      }
    }

    "type Double" should {
      "should read correct on method 'read(BitLength)' (matching length, check result)" in {
        AnythingIterator(ByteString(0x01, 0x03, 0x45, 0x45, 0x21, 0x21, 0x54, 0x12, 0x21, 0x54).iterator).read[Double](BitLength(64)) should be (7.2978182178952208E16)
      }

      "should read correct on method 'read(BitLength)' (matching length, check iter)" in {
        val iter = AnythingIterator(ByteString(0x00, 0x02, 0x01, 0x03, 0x45, 0x21, 0x54, 0x12, 0x21, 0x54, 0x21, 0x54, 0x12, 0x21, 0x54).iterator)
        iter.read[Double](BitLength(64))
        iter.len should be(ByteLength(7))
      }

      "should read correct on method 'read(ByteLength)' (matching length)" in {
        AnythingIterator(ByteString(0x88, 0x03, 0x45, 0x03, 0x45, 0x21, 0x54, 0x12, 0x21, 0x54).iterator).read[Double](ByteLength(8)) should be (-4.559384020459212E-270)
      }

      "should read correct on method 'read(ByteLength)' (matching length #2)" in {
        AnythingIterator(ByteString(0x88, 0x03, 0x45, 0x03, 0x45, 0x21, 0x54, 0x12, 0x21, 0x54).iterator).read[Double](ByteLength(4)) should be (0x88034503.toDouble)
      }

      "should read correct on method 'read(ByteLength)' (non-matching length, check result)" in {
        AnythingIterator(ByteString(0x01, 0x03, 0x45, 0x21, 0x54, 0x12, 0x21, 0x54).iterator).read[Double](ByteLength(1)) should be (1d)
      }

      "should read correct on method 'read(ByteLength)' (non-matching length, check iter)" in {
        val iter = AnythingIterator(ByteString(0x01, 0x03, 0x45, 0x01, 0x03, 0x45, 0x21, 0x54, 0x12, 0x21, 0x54).iterator)
        iter.read[Double](ByteLength(1))
        iter.len should be(ByteLength(10))
      }
    }
  }
}
