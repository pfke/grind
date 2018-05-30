package de.pfke.grind.core.refl.richEnum

import de.pfke.grind.core.refl.EnumOps
import de.pfke.grind.core.refl.richEnum.mocks.Enum1Mock
import org.scalatest.{Matchers, WordSpec}

import scala.reflect.runtime.{universe => ru}

class EnumOps_getChildrenNames_1Type_Spec
  extends WordSpec
    with Matchers {
  "called with an enum'" when {
    "return correct childs" should {
      "size must match" in {
        EnumOps.getChildrenNames(ru.typeOf[Enum1Mock.Enum1Mock]).size should be(5)
      }

      "result must be correct" in {
        val namespace = Enum1Mock.getClass
          .getTypeName
          .dropRight(1)
        val ref = List(
          Enum1Mock._1stEnum1Mock,
          Enum1Mock._2ndEnum1Mock,
          Enum1Mock._3rdEnum1Mock,
          Enum1Mock._4thEnum1Mock,
          Enum1Mock._5thEnum1Mock
        )
          .map { i => s"$namespace.$i" }
          .sorted

        EnumOps.getChildrenNames(ru.typeOf[Enum1Mock.Enum1Mock]) should be(ref)
      }
    }
  }

  "called with an non-enum'" when {
    "called with a non-enum value" should {
      "should throw an expection" in {
        an[IllegalArgumentException] shouldBe thrownBy(EnumOps.getChildrenNames(ru.typeOf[String]))
      }
    }
  }

  "called with an enum value'" when {
    "called with a enum value" should {
      "should throw an expection" in {
        an[IllegalArgumentException] shouldBe thrownBy(EnumOps.getChildrenNames(ru.typeOf[Enum1Mock._3rdEnum1Mock.type]))
      }
    }
  }
}
