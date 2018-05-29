package de.pfke.grind.refl.richCaseClass

import de.pfke.grind.core.refl.RichCaseClass
import de.pfke.grind.core.refl.richCaseClass.mocks.{CaseClassMock3Args_0Defaults, ClassMock0Args}
import org.scalatest.{Matchers, WordSpec}

class RichCaseClass_obj_apply_1Class_Spec
  extends WordSpec
    with Matchers {
  "testing method 'apply(Class[_])'" when {
    "passing a class" should {
      "should throw an exception" in {
        an[IllegalArgumentException] shouldBe thrownBy(RichCaseClass(classOf[ClassMock0Args]))
      }
    }

    "passing a case class" should {
      val tto = RichCaseClass(classOf[CaseClassMock3Args_0Defaults])

      "should return an object" in {
        tto should not be null
      }

      "should return correct name" in {
        tto.className should be (classOf[CaseClassMock3Args_0Defaults].getCanonicalName)
      }
    }
  }
}
