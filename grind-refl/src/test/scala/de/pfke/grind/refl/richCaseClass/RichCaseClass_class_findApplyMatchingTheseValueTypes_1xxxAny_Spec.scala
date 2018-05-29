package de.pfke.grind.refl.richCaseClass

import de.pfke.grind.core.refl.RichCaseClass
import de.pfke.grind.core.refl.richCaseClass.mocks.{CaseClassMock3Args_0Defaults, CaseClassMock_wOption}
import org.scalatest.{Matchers, WordSpec}

class RichCaseClass_class_findApplyMatchingTheseValueTypes_1xxxAny_Spec
  extends WordSpec
    with Matchers {
  "testing case class: 'CaseClassMock2Args_wMethods'" when {
    lazy val tto = RichCaseClass[CaseClassMock3Args_0Defaults]

    "passing the correct params" should {
      "should return the apply method, when passing all correct type values" in {
        tto.findApplyMethod_matching_paramTypes("arg1", false, 1254) should not be None
      }

      "should return the apply method, when passing too few names" in {
        tto.findApplyMethod_matching_paramTypes("arg1fre") should not be None
      }
    }

    "passing the wrong params" should {
      "should throw an exception, when passing wrong names" in {
        tto.findApplyMethod_matching_paramTypes(4645l, "arg2lkö") should be (None)
      }

      "should throw an exception, when passing too much names" in {
        tto.findApplyMethod_matching_paramTypes("arg1", true, 12353, 546f) should be (None)
      }
    }
  }

  "testing case class: 'CaseClassMock_wOption'" when {
    lazy val tto = RichCaseClass[CaseClassMock_wOption]

    "passing the correct params" should {
      "should return the apply method, when passing all correct type values" in {
        tto.findApplyMethod_matching_paramTypes(Some("arg1"), 1254, Some(false)) should not be None
      }

      "should return the apply method, when passing too few names" in {
        tto.findApplyMethod_matching_paramTypes(Some("arg1fre")) should not be None
      }
    }

    "passing the wrong params" should {
      "should throw an exception, when passing wrong names" in {
        tto.findApplyMethod_matching_paramTypes(4645l, Some("arg2lkö")) should be (None)
      }

      "should throw an exception, when passing too much names" in {
        tto.findApplyMethod_matching_paramTypes(Some("arg1"), 12353, Some(true), 546f) should be (None)
      }
    }
  }
}
