package de.pfke.grind.refl.richEnum

import de.pfke.grind.core.refl.EnumOps
import de.pfke.grind.core.refl.richEnum.mocks.{Enum1Mock, EnumWDupIDsMock}
import org.scalatest.{Matchers, WordSpec}

import scala.reflect.runtime.{universe => ru}

class EnumOps_hasDuplicateIds_1Type_Spec
  extends WordSpec
    with Matchers {
  "called with an enum w/o dup ids" when {
    "return correct childs" should {
      "result must be match" in {
        EnumOps.hasDuplicateIds(ru.typeOf[Enum1Mock.Enum1Mock]) shouldBe (right = false)
      }
    }
  }

  "called with an enum w/ dup ids" when {
    "return correct childs" should {
      "result must be match" in {
        EnumOps.hasDuplicateIds(ru.typeOf[EnumWDupIDsMock.EnumWDupIDsMock]) shouldBe (right = true)
      }
    }
  }

  "called with an non-enum'" when {
    "called with a non-enum value" should {
      "should throw an expection" in {
        an[IllegalArgumentException] shouldBe thrownBy(EnumOps.hasDuplicateIds(ru.typeOf[String]))
      }
    }
  }

  "called with an enum value'" when {
    "called with a enum value" should {
      "should throw an expection" in {
        an[IllegalArgumentException] shouldBe thrownBy(EnumOps.hasDuplicateIds(ru.typeOf[Enum1Mock._3rdEnum1Mock.type]))
      }
    }
  }
}
