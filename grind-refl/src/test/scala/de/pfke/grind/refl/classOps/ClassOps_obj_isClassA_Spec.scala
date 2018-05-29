package de.pfke.grind.refl.classOps

import de.pfke.grind.core.refl.ClassOps
import de.pfke.grind.core.refl.richClass.mocks.{CaseClassMock3Args_0Defaults, ClassMock0Args}
import org.scalatest.{Matchers, WordSpec}

class ClassOps_obj_isClassA_Spec
  extends WordSpec
    with Matchers {
  "testing method 'isCaseClass[A]'" when {
    "passing a class" should {
      "should return false" in {
        ClassOps.isClass[ClassMock0Args] shouldBe (right = true)
      }
    }

    "passing a case class" should {
      "should return true" in {
        ClassOps.isClass[CaseClassMock3Args_0Defaults] shouldBe (right = false)
      }
    }
  }
}
