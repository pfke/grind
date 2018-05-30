package de.pfke.grind.core.refl.genericOps

import de.pfke.grind.core.refl.GenericOps
import org.scalatest.{Matchers, WordSpecLike}

import scala.collection.mutable
import scala.reflect.runtime.{universe => ru}

class GenericOps_isComplex_spec
  extends WordSpecLike
      with Matchers {
  "using method isComplex(ru.Type)" when {
    "passing primitives" should {
      "Boolean should return false" in {
        GenericOps.isComplex(ru.typeOf[Boolean]) shouldBe (right = false)
      }

      "Byte should return false" in {
        GenericOps.isComplex(ru.typeOf[Byte]) shouldBe (right = false)
      }

      "Char should return false" in {
        GenericOps.isComplex(ru.typeOf[Char]) shouldBe (right = false)
      }

      "Double should return false" in {
        GenericOps.isComplex(ru.typeOf[Double]) shouldBe (right = false)
      }

      "Float should return false" in {
        GenericOps.isComplex(ru.typeOf[Float]) shouldBe (right = false)
      }

      "Int should return false" in {
        GenericOps.isComplex(ru.typeOf[Int]) shouldBe (right = false)
      }

      "Long should return false" in {
        GenericOps.isComplex(ru.typeOf[Long]) shouldBe (right = false)
      }

      "Short should return false" in {
        GenericOps.isComplex(ru.typeOf[Short]) shouldBe (right = false)
      }
    }

    "passing complex" should {
      "String should return true" in {
        GenericOps.isComplex(ru.typeOf[String]) shouldBe (right = false)
      }

      "Option should return true" in {
        GenericOps.isComplex(ru.typeOf[Option[String]]) shouldBe (right = true)
      }
    }

    "passing traits" should {
      "String should return true" in {
        GenericOps.isComplex(ru.typeOf[mutable.Buffer[Int]]) shouldBe (right = true)
      }
    }

    "passing abstract class" should {
      "String should return true" in {
        GenericOps.isComplex(ru.typeOf[mutable.AbstractBuffer[String]]) shouldBe (right = true)
      }
    }
  }
}
