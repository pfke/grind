package de.pfke.grind.core.refl.richCaseClass

import de.pfke.grind.core.refl.RichCaseClass
import de.pfke.grind.core.refl.richCaseClass.mocks.{CaseClassMock2Args_wMethods, CaseClassMock2Args_wOverloadedMethods_allDefaults}
import org.scalatest.{Matchers, WordSpec}

class RichCaseClass_class_findApplyMatchingTheseNames_1xxxString_Spec
  extends WordSpec
    with Matchers {
  "testing case class: 'CaseClassMock2Args_wMethods'" when {
    lazy val tto = RichCaseClass[CaseClassMock2Args_wMethods]

    "passing the correct params" should {
      "should return the apply method, when passing all names" in {
        tto.findApplyMethod_matching_paramNames("arg1", "arg2") should not be None
      }

      "should return the apply method, when passing too few names" in {
        tto.findApplyMethod_matching_paramNames("arg1") should not be None
      }
    }

    "passing the wrong params" should {
      "should throw an exception, when passing wrong names" in {
        tto.findApplyMethod_matching_paramNames("arg1", "arg2lkö") should be (None)
      }

      "should throw an exception, when passing too much names" in {
        tto.findApplyMethod_matching_paramNames("arg1", "arg2", "arg3") should be (None)
      }
    }
  }

  "testing case class: 'CaseClassMock2Args_wOverloadedMethods_allDefaults'" when {
    lazy val tto = RichCaseClass[CaseClassMock2Args_wOverloadedMethods_allDefaults]

    "passing the correct params" should {
      "should return the apply method, when passing all names" in {
        tto.findApplyMethod_matching_paramNames("arg1", "arg2") should not be None
      }

      "should return the apply method, when passing too few names" in {
        tto.findApplyMethod_matching_paramNames("arg1") should not be None
      }
    }

    "passing the wrong params" should {
      "should throw an exception, when passing wrong names" in {
        tto.findApplyMethod_matching_paramNames("arg1", "arg2lkö") should be (None)
      }

      "should throw an exception, when passing too much names" in {
        tto.findApplyMethod_matching_paramNames("arg1", "arg2", "arg3") should be (None)
      }
    }
  }
}
