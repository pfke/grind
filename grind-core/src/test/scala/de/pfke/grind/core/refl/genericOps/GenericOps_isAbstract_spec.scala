package de.pfke.grind.core.refl.genericOps

import de.pfke.grind.core.refl.GenericOps
import org.scalatest.{Matchers, WordSpecLike}

import scala.collection.mutable
import scala.reflect.runtime.{universe => ru}

class GenericOps_isAbstract_spec
  extends WordSpecLike
      with Matchers {
  "using method isAbstract(ru.Type)" when {
    "passing primitives" should {
      "Boolean should return true" in {
        GenericOps.isAbstract(ru.typeOf[Boolean]) shouldBe (right = true)
      }

      "Byte should return true" in {
        GenericOps.isAbstract(ru.typeOf[Byte]) shouldBe (right = true)
      }

      "Char should return true" in {
        GenericOps.isAbstract(ru.typeOf[Char]) shouldBe (right = true)
      }

      "Double should return true" in {
        GenericOps.isAbstract(ru.typeOf[Double]) shouldBe (right = true)
      }

      "Float should return true" in {
        GenericOps.isAbstract(ru.typeOf[Float]) shouldBe (right = true)
      }

      "Int should return true" in {
        GenericOps.isAbstract(ru.typeOf[Int]) shouldBe (right = true)
      }

      "Long should return true" in {
        GenericOps.isAbstract(ru.typeOf[Long]) shouldBe (right = true)
      }

      "Short should return true" in {
        GenericOps.isAbstract(ru.typeOf[Short]) shouldBe (right = true)
      }
    }

    "passing complex" should {
      "String should return false" in {
        GenericOps.isAbstract(ru.typeOf[String]) shouldBe (right = false)
      }

      "Option should return true" in {
        GenericOps.isAbstract(ru.typeOf[Option[String]]) shouldBe (right = true)
      }
    }

    "passing traits" should {
      "String should return true" in {
        GenericOps.isAbstract(ru.typeOf[mutable.Buffer[Int]]) shouldBe (right = true)
      }
    }

    "passing abstract class" should {
      "String should return true" in {
        GenericOps.isAbstract(ru.typeOf[mutable.AbstractBuffer[String]]) shouldBe (right = true)
      }
    }
  }
}
