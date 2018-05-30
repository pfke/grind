package de.pfke.grind.core.refl.richEnum

import de.pfke.grind.core.refl.EnumOps
import de.pfke.grind.core.refl.richEnum.mocks.Enum1Mock
import org.scalatest.{Matchers, WordSpec}

import scala.reflect.runtime.{universe => ru}

class EnumOps_isEnum_Spec
  extends WordSpec
    with Matchers {
  "Method 'isEnum(Type)'" when {
    "called with an enum value" should {
      "should return true" in {
        EnumOps.isEnum(ru.typeOf[Enum1Mock.Enum1Mock]) shouldBe (right = true)
      }
    }

    "called with a non-enum value" should {
      "should return false" in {
        EnumOps.isEnum(ru.typeOf[String]) shouldBe (right = false)
      }
    }

    "called with a enum value" should {
      "should return false" in {
        EnumOps.isEnum(ru.typeOf[Enum1Mock._3rdEnum1Mock.type]) shouldBe (right = false)
      }
    }
  }
}
