package de.pfke.grind.core.refl.richCaseClass

import de.pfke.grind.core.refl.RichCaseClass
import de.pfke.grind.core.refl.richCaseClass.mocks._
import org.scalatest.{Matchers, WordSpec}

class RichCaseClass_class_callA_1String_2xxxAny_Spec
  extends WordSpec
    with Matchers {
  "testing case class: 'CaseClassMock0Args'" when {
    lazy val tto = RichCaseClass[CaseClassMock0Args]

    "call method 'method1'" should {
      "should throw an exception, because the method does not exist" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.call[String](methodName = "method1"))
      }
    }
  }

  "testing case class: 'CaseClassMock2Args_wMethods'" when {
    lazy val tto = RichCaseClass[CaseClassMock2Args_wMethods]

    "call method 'method1'" should {
      "soll eine Ex werfen, da die CaseClass Args zum instanziieren benötigt" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.call[String](methodName = "method1", "klköl"))
      }
    }
  }

  "testing case class: 'CaseClassMock2Args_woMethods'" when {
    lazy val tto = RichCaseClass[CaseClassMock2Args_woMethods]

    "call method 'method1'" should {
      "soll eine Ex werfen, da die CaseClass Args zum instanziieren benötigt" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.call[String](methodName = "method1", "klköl"))
      }
    }
  }

  "testing case class: 'CaseClassMock2Args_wOverloadedMethods'" when {
    lazy val tto = RichCaseClass[CaseClassMock2Args_wOverloadedMethods]

    "call method 'method1'" should {
      "soll eine Ex werfen, da die CaseClass Args zum instanziieren benötigt" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.call[String](methodName = "method1", "klköl"))
      }
    }
  }

  "testing case class: 'CaseClassMock2Args_wOverloadedMethods_allDefaults'" when {
    lazy val tto = RichCaseClass[CaseClassMock2Args_wOverloadedMethods_allDefaults]

    "call method 'method1'" should {
      "should return correct result" in {
        tto.call[String](methodName = "method1") should be ("method1")
      }

      "soll eine Ex werfen, wenn Argumente mitgegeben werden (ist eine argumentenlose Methode)" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.call[String](methodName = "method1", 123))
      }
    }

    "call method 'method2(String)'" should {
      "should return correct result, when passing all args" in {
        tto.call[String](methodName = "method2", "sohi") should be ("method2(String): sohi")
      }

      "soll eine Ex werfen, wenn das Argument vom falschem Typ ist" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.call[String](methodName = "method1", 123))
      }
    }

    "call method 'method2(String, Int)'" should {
      "should return correct result, when passing all args" in {
        tto.call[String](methodName = "method2", "sohi", 13) should be ("method2(String, Int): sohi, 13")
      }

      "should throw an exception, when passing args in wrong order" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.call[String](methodName = "method2", 13, "sohi"))
      }

      "soll eine Ex werfen, wenn zu viele Argumente mitgegeben werden" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.call[String](methodName = "method1", 123, 123, false))
      }
    }

    "call method 'method2(Byte, String, Long)'" should {
      "should return correct result, when passing all args" in {
        tto.call[String](methodName = "method2", 54.toByte, "sohi", 13654l) should be ("method2(Byte, String, Long): 54, sohi, 13654")
      }

      "should throw an exception, when passing args in wrong order" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.call[String](methodName = "method2", 456456l, 13.toByte, "sohi"))
      }
    }
  }
}
