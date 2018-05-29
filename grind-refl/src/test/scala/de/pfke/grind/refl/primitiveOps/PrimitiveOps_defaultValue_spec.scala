package de.pfke.grind.refl.primitiveOps

import de.pfke.grind.core.refl.{GenericOps, PrimitiveOps}
import org.scalatest.{Matchers, WordSpecLike}

import scala.reflect.ClassTag
import scala.reflect.runtime.{universe => ru}

class PrimitiveOps_defaultValue_spec
  extends WordSpecLike
    with Matchers {
  "testing method 'defaultValue'" when {
    "using Boolean type" should {
      check[Boolean] (false)
    }

    "using Byte type" should {
      check[Byte] (0.toByte)
    }

    "using Char type" should {
      check[Char] (0.toChar)
    }

    "using Double type" should {
      check[Double] (0d)
    }

    "using Float type" should {
      check[Float] (0f)
    }

    "using Int type" should {
      check[Int] (0)
    }

    "using Long type" should {
      check[Long] (0l)
    }

    "using Short type" should {
      check[Short] (0.toShort)
    }

    "using string type" should {
      "throw an exception" in {
        an[IllegalArgumentException] shouldBe thrownBy(PrimitiveOps.defaultValue(ru.typeOf[String]))
      }
    }
  }

  private def check[A] (
    expectedVal: A
  ) (
    implicit
    classTag: ClassTag[A],
    typeTag: ru.TypeTag[A]
  ): Unit = {
    withClue(s"type: ${classTag.runtimeClass}") {
      "return correct type" in {
        PrimitiveOps.toScalaType(
          GenericOps
            .typeOf(
              PrimitiveOps.defaultValue(typeTag.tpe)
            )) should be (typeTag.tpe)
      }

      "return correct value" in {
        PrimitiveOps
          .defaultValue(typeTag.tpe).asInstanceOf[A] should be (expectedVal)
      }
    }
  }
}
