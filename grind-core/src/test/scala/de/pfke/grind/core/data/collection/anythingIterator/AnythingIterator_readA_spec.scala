package de.pfke.grind.core.data.collection.anythingIterator

import java.nio.ByteOrder

import akka.util.ByteString
import de.pfke.grind.core.data.collection.AnythingIterator
import org.scalatest.{Matchers, WordSpecLike}

class AnythingIterator_readA_spec
  extends WordSpecLike
    with Matchers {
  implicit val byteOrder: ByteOrder = ByteOrder.BIG_ENDIAN

  "calling w/ type Boolean" should {
    "should throw an exception on an empty iter" in {
      an[IllegalArgumentException] shouldBe thrownBy(AnythingIterator.empty.read[Boolean]())
    }

    "should read correct on iter w/ 1 byte in it" in {
      AnythingIterator(ByteString(0x01)).read[Boolean]() shouldBe (right = true)
    }

    "should read correct on iter w/ >1 byte in it" in {
      AnythingIterator(ByteString(0x00, 0x03, 0x41)).read[Boolean]() shouldBe (right = false)
    }
  }

  "calling w/ type Byte" should {
    "should throw an exception on an empty iter" in {
      an[IllegalArgumentException] shouldBe thrownBy(AnythingIterator.empty.read[Byte]())
    }

    "should read correct on iter w/ 1 byte in it" in {
      AnythingIterator(ByteString(0x01)).read[Byte]() shouldBe 0x01
    }

    "should read correct on iter w/ >1 byte in it" in {
      AnythingIterator(ByteString(0x70, 0x03, 0x41)).read[Byte]() shouldBe 0x70
    }
  }

  "calling w/ type Char" should {
    "should throw an exception on an empty iter" in {
      an[IllegalArgumentException] shouldBe thrownBy(AnythingIterator.empty.read[Char]())
    }

    "should throw an exception on iter w/ 1 byte in it" in {
      an[IllegalArgumentException] shouldBe thrownBy(AnythingIterator(ByteString(0x01)).read[Char]())
    }

    "should read correct on iter w/ 2 byte in it" in {
      AnythingIterator(ByteString(0x91, 0x21)).read[Char]() shouldBe 0x9121
    }

    "should read correct on iter w/ >2 byte in it" in {
      AnythingIterator(ByteString(0x70, 0x03, 0x41).iterator).read[Char]() shouldBe 0x7003
    }
  }

  "calling w/ type Double" should {
    "should throw an exception on an empty iter" in {
      an[IllegalArgumentException] shouldBe thrownBy(AnythingIterator.empty.read[Double]())
    }

    "should throw an exception on iter w/ 1 byte in it" in {
      an[IllegalArgumentException] shouldBe thrownBy(AnythingIterator(ByteString(0x01)).read[Double]())
    }

    "should read correct on iter w/ 8 byte in it" in {
      AnythingIterator(ByteString(0x91, 0x21, 0x54, 0x12, 0x21, 0x54, 0x12, 0x21, 0x54, 0x12)).read[Double]() should be (-3.657394190189916E-226)
    }

    "should read correct on iter w/ >8 byte in it" in {
      AnythingIterator(ByteString(0x91, 0x21, 0x54, 0x12, 0x21, 0x54, 0x12, 0x21, 0x54, 0x12, 0x70, 0x03, 0x41)).read[Double]() should be (-3.657394190189916E-226)
    }
  }

  "calling w/ type Float" should {
    "should throw an exception on an empty iter" in {
      an[IllegalArgumentException] shouldBe thrownBy(AnythingIterator.empty.read[Float]())
    }

    "should throw an exception on iter w/ 1 byte in it" in {
      an[IllegalArgumentException] shouldBe thrownBy(AnythingIterator(ByteString(0x01)).read[Float]())
    }

    "should read correct on iter w/ 4 byte in it" in {
      AnythingIterator(ByteString(0x91, 0x21, 0x54, 0x12)).read[Float]() should be (-1.2726567E-28.toFloat)
    }

    "should read correct on iter w/ >4 byte in it" in {
      AnythingIterator(ByteString(0x91, 0x21, 0x54, 0x12, 0x21, 0x54, 0x12, 0x21, 0x54, 0x12, 0x70, 0x03, 0x41)).read[Float]() should be (-1.2726567E-28.toFloat)
    }
  }

  "calling w/ type Int" should {
    "should throw an exception on an empty iter" in {
      an[IllegalArgumentException] shouldBe thrownBy(AnythingIterator.empty.read[Int]())
    }

    "should throw an exception on iter w/ 1 byte in it" in {
      an[IllegalArgumentException] shouldBe thrownBy(AnythingIterator(ByteString(0x01)).read[Int]())
    }

    "should read correct on iter w/ 4 byte in it" in {
      AnythingIterator(ByteString(0x91, 0x21, 0x54, 0x12)).read[Int]() should be (0x91215412)
    }

    "should read correct on iter w/ >4 byte in it" in {
      AnythingIterator(ByteString(0x91, 0x21, 0x54, 0x12, 0x21, 0x54, 0x12, 0x21, 0x54, 0x12, 0x70, 0x03, 0x41)).read[Int]() should be (0x91215412)
    }
  }

  "calling w/ type Long" should {
    "should throw an exception on an empty iter" in {
      an[IllegalArgumentException] shouldBe thrownBy(AnythingIterator.empty.read[Long]())
    }

    "should throw an exception on iter w/ 1 byte in it" in {
      an[IllegalArgumentException] shouldBe thrownBy(AnythingIterator(ByteString(0x01)).read[Long]())
    }

    "should read correct on iter w/ 8 byte in it" in {
      AnythingIterator(ByteString(0x91, 0x21, 0x54, 0x12, 0x21, 0x54, 0x12, 0x21, 0x54, 0x12)).read[Long]() should be (0x9121541221541221l)
    }

    "should read correct on iter w/ >8 byte in it" in {
      AnythingIterator(ByteString(0x91, 0x21, 0x54, 0x12, 0x21, 0x54, 0x12, 0x21, 0x54, 0x12, 0x70, 0x03, 0x41)).read[Long]() should be (0x9121541221541221l)
    }
  }

  "calling w/ type Short" should {
    "should throw an exception on an empty iter" in {
      an[IllegalArgumentException] shouldBe thrownBy(AnythingIterator.empty.read[Short]())
    }

    "should throw an exception on iter w/ 1 byte in it" in {
      an[IllegalArgumentException] shouldBe thrownBy(AnythingIterator(ByteString(0x01)).read[Short]())
    }

    "should read correct on iter w/ 2 byte in it" in {
      AnythingIterator(ByteString(0x91, 0x21)).read[Short]() should be (0x9121.toShort)
    }

    "should read correct on iter w/ >2 byte in it" in {
      AnythingIterator(ByteString(0x91, 0x21, 0x54, 0x12, 0x21, 0x54, 0x12, 0x21, 0x54, 0x12, 0x70, 0x03, 0x41)).read[Short]() should be (0x9121.toShort)
    }
  }
}
