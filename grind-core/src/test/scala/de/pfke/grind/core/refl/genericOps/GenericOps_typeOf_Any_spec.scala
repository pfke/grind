package de.pfke.grind.core.refl.genericOps

import de.pfke.grind.core.refl.GenericOps
import org.scalatest.{Matchers, WordSpecLike}

import scala.reflect.runtime.{universe => ru}

class GenericOps_typeOf_Any_spec
  extends WordSpecLike
    with Matchers {
  "using method typeOf(Any)" when {
    "testing primitive: Boolean" should {
      "should be same type" in {
        GenericOps.toScalaType(GenericOps.typeOf(false)) should be (ru.typeOf[Boolean])
      }
    }

    "testing primitive: Byte" should {
      "should be same type" in {
        GenericOps.toScalaType(GenericOps.typeOf(12.toByte)) should be (ru.typeOf[Byte])
      }
    }

    "testing primitive: Char" should {
      "should be same type" in {
        GenericOps.toScalaType(GenericOps.typeOf(123.toChar)) should be (ru.typeOf[Char])
      }
    }

    "testing primitive: Double" should {
      "should be same type" in {
        GenericOps.toScalaType(GenericOps.typeOf(897789d)) should be (ru.typeOf[Double])
      }
    }

    "testing primitive: Float" should {
      "should be same type" in {
        GenericOps.toScalaType(GenericOps.typeOf(1231212f)) should be (ru.typeOf[Float])
      }
    }

    "testing primitive: Int" should {
      "should be same type" in {
        GenericOps.toScalaType(GenericOps.typeOf(908783)) should be (ru.typeOf[Int])
      }
    }

    "testing primitive: Long" should {
      "should be same type" in {
        GenericOps.toScalaType(GenericOps.typeOf(32908790l)) should be (ru.typeOf[Long])
      }
    }

    "testing primitive: Short" should {
      "should be same type" in {
        GenericOps.toScalaType(GenericOps.typeOf(34234.toShort)) should be (ru.typeOf[Short])
      }
    }

    "testing complex: String" should {
      "should be same type" in {
        GenericOps.toScalaType(GenericOps.typeOf("!jkjljkl")) should be(ru.typeOf[String])
      }
    }
  }
}
