package de.pfke.grind.core.refl.classOps

import de.pfke.grind.core.refl.ClassOps
import de.pfke.grind.core.refl.richClass.mocks.{CaseClassMock3Args_0Defaults, ClassMock0Args}
import org.scalatest.{Matchers, WordSpec}

class ClassOps_obj_isCaseClass_1String_Spec
  extends WordSpec
    with Matchers {
  "testing method 'isCaseClass(String)'" when {
    "passing a class" should {
      "should return false" in {
        ClassOps.isCaseClass(classOf[ClassMock0Args].getCanonicalName) shouldBe (right = false)
      }
    }

    "passing a case class" should {
      "should return true" in {
        ClassOps.isCaseClass(classOf[CaseClassMock3Args_0Defaults].getCanonicalName) shouldBe (right = true)
      }
    }

    "passing an object" should {
      "should return true" in {
        ClassOps.isCaseClass("de.pfke.grind.core.refl.richCaseClass.mocks.ObjectMock") shouldBe (right = false)
      }
    }
  }
}
