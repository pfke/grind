package de.pfke.grind.refl.richClass

import de.pfke.grind.core.refl.RichClass
import de.pfke.grind.core.refl.richClass.mocks.ClassMock2Args_wOverloadedMethods
import org.scalatest.{Matchers, WordSpec}

class ClassRefl_class_findCtorMatchingTheseValueTypes_1xxxAny_Spec
  extends WordSpec
    with Matchers {
  "testing case class: 'ClassMock2Args_wOverloadedMethods'" when {
    lazy val tto = RichClass[ClassMock2Args_wOverloadedMethods]

    "passing the correct params" should {
      "should return the ctor method, when passing all correct type values" in {
        tto.findCtorMatchingTheseValueTypes("arg1", 1254) should be(Some(tto.ctorMethods.head))
      }

      "should return the ctor method, when passing too few names" in {
        tto.findCtorMatchingTheseValueTypes("arg1fre") should be (Some(tto.ctorMethods.head))
      }
    }

    "passing the wrong params" should {
      "should throw an exception, when passing wrong names" in {
        tto.findCtorMatchingTheseValueTypes(4645l, "arg2lkö") should be (None)
      }

      "should throw an exception, when passing too much names" in {
        tto.findCtorMatchingTheseValueTypes("arg1", true, 12353, 546f) should be (None)
      }
    }
  }
}
