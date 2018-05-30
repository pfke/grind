package de.pfke.grind.core.refl.richClass

import de.pfke.grind.core.refl.RichClass
import de.pfke.grind.core.refl.richClass.mocks.ClassMock2Args_wOverloadedMethods
import org.scalatest.{Matchers, WordSpec}

class ClassRefl_class_findCtorMatchingTheseNames_1xxxString_Spec
  extends WordSpec
    with Matchers {
  "testing case class: 'ClassMock2Args_wOverloadedMethods'" when {
    lazy val tto = RichClass[ClassMock2Args_wOverloadedMethods]

    "passing the correct params" should {
      "should return the apply method, when passing all names" in {
        tto.findCtorMatchingTheseNames("arg1", "arg2") should be(Some(tto.ctorMethods.head))
      }

      "should return the apply method, when passing too few names" in {
        tto.findCtorMatchingTheseNames("arg1") should be (Some(tto.ctorMethods.head))
      }
    }

    "passing the wrong params" should {
      "should throw an exception, when passing wrong names" in {
        tto.findCtorMatchingTheseNames("arg1", "arg2lkö") should be (None)
      }

      "should throw an exception, when passing too much names" in {
        tto.findCtorMatchingTheseNames("arg1", "arg2", "arg3") should be (None)
      }
    }
  }
}
