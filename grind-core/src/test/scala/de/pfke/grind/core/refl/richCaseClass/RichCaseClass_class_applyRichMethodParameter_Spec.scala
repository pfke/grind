package de.pfke.grind.core.refl.richCaseClass

import de.pfke.grind.core.refl.{RichMethodParameter, RichCaseClass}
import de.pfke.grind.core.refl.richCaseClass.mocks._
import org.scalatest.{Matchers, WordSpec}

import scala.reflect.runtime.{universe => ru}

class RichCaseClass_class_applyRichMethodParameter_Spec
  extends WordSpec
    with Matchers {
  "testing case class: 'CaseClassMock0Args'" when {
    "passing the class" should {
      lazy val tto = RichCaseClass[CaseClassMock0Args]

      "should return parameter for one apply method" in {
        tto.applyMethodParameters.size should be (1)
      }

      "apply #1 should have 0 args" in {
        tto.applyMethodParameters.head.size should be (0)
      }
    }
  }

  "testing case class: 'CaseClassMock2Args_wMethods'" when {
    lazy val tto = RichCaseClass[CaseClassMock2Args_wMethods]

    "passing the class" should {
      "should return parameter for one apply method" in {
        tto.applyMethodParameters.size should be (1)
      }

      "apply #1 should have 2 args" in {
        tto.applyMethodParameters.head.size should be (2)
      }

      "should return correct 1st param" in {
        mapParam(tto.applyMethodParameters.head.head) should be ((0, "arg1", ru.typeOf[String], None))
      }

      "should return correct 2nd param" in {
        mapParam(tto.applyMethodParameters.head(1)) should be ((1, "arg2", ru.typeOf[Int], None))
      }
    }
  }

  "testing case class: 'CaseClassMock2Args_woMethods'" when {
    lazy val tto = RichCaseClass[CaseClassMock2Args_woMethods]

    "passing the class" should {
      "should return parameter for one apply method" in {
        tto.applyMethodParameters.size should be (1)
      }

      "apply #1 should have 2 args" in {
        tto.applyMethodParameters.head.size should be (2)
      }

      "should return correct 1st param" in {
        mapParam(tto.applyMethodParameters.head.head) should be ((0, "arg1", ru.typeOf[Boolean], None))
      }

      "should return correct 2nd param" in {
        mapParam(tto.applyMethodParameters.head(1)) should be ((1, "arg2", ru.typeOf[Char], None))
      }
    }
  }

  "testing case class: 'CaseClassMock2Args_wOverloadedMethods'" when {
    lazy val tto = RichCaseClass[CaseClassMock2Args_wOverloadedMethods]

    "passing the class" should {
      "should return parameter for one apply method" in {
        tto.applyMethodParameters.size should be (1)
      }

      "apply #1 should have 2 args" in {
        tto.applyMethodParameters.head.size should be (2)
      }

      "should return correct 1st param" in {
        mapParam(tto.applyMethodParameters.head.head) should be ((0, "arg1", ru.typeOf[List[String]], None))
      }

      "should return correct 2nd param" in {
        mapParam(tto.applyMethodParameters.head(1)) should be ((1, "arg2", ru.typeOf[CaseClassMock0Args], None))
      }
    }
  }

  "testing case class: 'CaseClassMock3Args_0Defaults'" when {
    lazy val tto = RichCaseClass[CaseClassMock3Args_0Defaults]

    "passing the class" should {
      "should return parameter for one apply method" in {
        tto.applyMethodParameters.size should be (1)
      }

      "apply #1 should have 3 args" in {
        tto.applyMethodParameters.head.size should be (3)
      }

      "should return correct 1st param" in {
        mapParam(tto.applyMethodParameters.head.head) should be ((0, "arg1", ru.typeOf[String], None))
      }

      "should return correct 2nd param" in {
        mapParam(tto.applyMethodParameters.head(1)) should be ((1, "arg2", ru.typeOf[Boolean], None))
      }

      "should return correct 3rd param" in {
        mapParam(tto.applyMethodParameters.head(2)) should be ((2, "arg3", ru.typeOf[Int], None))
      }
    }
  }

  "testing case class: 'CaseClassMock3Args_1Default'" when {
    lazy val tto = RichCaseClass[CaseClassMock3Args_1Default]

    "passing the class" should {
      "should return parameter for one apply method" in {
        tto.applyMethodParameters.size should be (1)
      }

      "apply #1 should have 2 args" in {
        tto.applyMethodParameters.head.size should be (3)
      }

      "should return correct 1st param" in {
        mapParam(tto.applyMethodParameters.head.head) should be ((0, "arg1", ru.typeOf[String], None))
      }

      "should return correct 2nd param" in {
        mapParam(tto.applyMethodParameters.head(1)) should be ((1, "arg2", ru.typeOf[Boolean], None))
      }

      "should return correct 3rd param" in {
        mapParam(tto.applyMethodParameters.head(2)) should be ((2, "arg3", ru.typeOf[Int], Some(123)))
      }
    }
  }

  private def mapParam(
    reflParam: RichMethodParameter
  ): (Int, String, ru.Type, Option[Any]) = (reflParam.index, reflParam.name, reflParam.symbol.typeSignature, reflParam.defaultValue)
}
