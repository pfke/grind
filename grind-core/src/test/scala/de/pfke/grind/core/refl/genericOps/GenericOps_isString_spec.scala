package de.pfke.grind.core.refl.genericOps

import de.pfke.grind.core.refl.GenericOps
import org.scalatest.{Matchers, WordSpecLike}

import scala.collection.mutable
import scala.reflect.runtime.{universe => ru}

class GenericOps_isString_spec
  extends WordSpecLike
      with Matchers {
  "using method isString(ru.Type)" when {
    "passing primitives" should {
      "Boolean should return false" in {
        GenericOps.isString(ru.typeOf[Boolean]) shouldBe (right = false)
      }

      "Byte should return false" in {
        GenericOps.isString(ru.typeOf[Byte]) shouldBe (right = false)
      }

      "Char should return false" in {
        GenericOps.isString(ru.typeOf[Char]) shouldBe (right = false)
      }

      "Double should return false" in {
        GenericOps.isString(ru.typeOf[Double]) shouldBe (right = false)
      }

      "Float should return false" in {
        GenericOps.isString(ru.typeOf[Float]) shouldBe (right = false)
      }

      "Int should return false" in {
        GenericOps.isString(ru.typeOf[Int]) shouldBe (right = false)
      }

      "Long should return false" in {
        GenericOps.isString(ru.typeOf[Long]) shouldBe (right = false)
      }

      "Short should return false" in {
        GenericOps.isString(ru.typeOf[Short]) shouldBe (right = false)
      }
    }

    "passing complex" should {
      "Option should return false" in {
        GenericOps.isString(ru.typeOf[Option[String]]) shouldBe (right = false)
      }
    }

    "passing String" should {
      "should return true" in {
        GenericOps.isString(ru.typeOf[String]) shouldBe (right = true)
      }
    }

    "passing traits" should {
      "String should return false" in {
        GenericOps.isString(ru.typeOf[mutable.Buffer[Int]]) shouldBe (right = false)
      }
    }

    "passing abstract class" should {
      "String should return false" in {
        GenericOps.isString(ru.typeOf[mutable.AbstractBuffer[String]]) shouldBe (right = false)
      }
    }
  }
}
