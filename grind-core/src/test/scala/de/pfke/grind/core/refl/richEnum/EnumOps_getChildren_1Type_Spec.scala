package de.pfke.grind.core.refl.richEnum

import de.pfke.grind.core.refl.EnumOps
import de.pfke.grind.core.refl.richEnum.mocks.Enum1Mock
import org.scalatest.{Matchers, WordSpec}

import scala.reflect.runtime.{universe => ru}

class EnumOps_getChildren_1Type_Spec
  extends WordSpec
    with Matchers {
  "called with an enum'" when {
    "return correct childs" should {
      "size must match" in {
        EnumOps.getChildren(ru.typeOf[Enum1Mock.Enum1Mock]).size should be(5)
      }
    }
  }

  "called with an non-enum'" when {
    "called with a non-enum value" should {
      "should throw an expection" in {
        an[IllegalArgumentException] shouldBe thrownBy(EnumOps.getChildren(ru.typeOf[String]))
      }
    }
  }

  "called with an enum value'" when {
    "called with a enum value" should {
      "should throw an expection" in {
        an[IllegalArgumentException] shouldBe thrownBy(EnumOps.getChildren(ru.typeOf[Enum1Mock._3rdEnum1Mock.type]))
      }
    }
  }
}
