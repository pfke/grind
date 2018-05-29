package de.pfke.grind.refl.richCaseClass

import de.pfke.grind.core.refl.RichCaseClass
import de.pfke.grind.core.refl.richCaseClass.mocks.{CaseClassMock0Args, CaseClassMock2Args_wMethods, CaseClassMock2Args_woMethods, CaseClassMock3Args_1Default}
import org.scalatest.{Matchers, WordSpec}

class RichCaseClass_class_applyA_1xxxAny_Spec
  extends WordSpec
    with Matchers {
  "testing case class: 'CaseClassMock0Args'" when {
    "passing the class" should {
      lazy val tto = RichCaseClass[CaseClassMock0Args]

      "should instantiate, when passing all args" in {
        tto.instantiate[CaseClassMock0Args]() should be (CaseClassMock0Args())
      }

      "should throw an exception, when to much args" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.instantiate[CaseClassMock0Args](456, false))
      }
    }
  }

  "testing case class: 'CaseClassMock2Args_wMethods'" when {
    lazy val tto = RichCaseClass[CaseClassMock2Args_wMethods]

    "call method 'apply'" should {
      "should instantiate, when passing all args" in {
        tto.instantiate[CaseClassMock2Args_wMethods]("sdvdvds", 123) should be (CaseClassMock2Args_wMethods("sdvdvds", 123))
      }

      "should throw an exception, when passing incompatible generic" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.instantiate[String]("54656"))
      }

      "should throw an exception, when to few args" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.instantiate[CaseClassMock2Args_wMethods]("54656"))
      }

      "should throw an exception, when passing wrong arg types" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.instantiate[CaseClassMock2Args_wMethods]("54656", "sdvdvds"))
      }

      "should throw an exception, when to much args" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.instantiate[CaseClassMock2Args_wMethods]("54656", 456, false))
      }
    }
  }

  "testing case class: 'CaseClassMock2Args_woMethods'" when {
    lazy val tto = RichCaseClass[CaseClassMock2Args_woMethods]

    "call method 'apply'" should {
      "should instantiate, when passing all args" in {
        tto.instantiate[CaseClassMock2Args_woMethods](false, 123.toChar) should be (CaseClassMock2Args_woMethods(arg1 = false, 123.toChar))
      }

      "should throw an exception, when passing incompatible generic" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.instantiate[String](false, 123.toChar))
      }

      "should throw an exception, when to few args" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.instantiate[CaseClassMock2Args_woMethods](false))
      }

      "should throw an exception, when passing wrong arg types" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.instantiate[CaseClassMock2Args_wMethods]("54656", "sdvdvds"))
      }

      "should throw an exception, when to much args" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.instantiate[CaseClassMock2Args_woMethods](false, 123.toChar, false))
      }
    }
  }

  "testing case class: 'CaseClassMock3Args_1Default'" when {
    lazy val tto = RichCaseClass[CaseClassMock3Args_1Default]

    "call method 'apply'" should {
      "should instantiate, when passing all args" in {
        tto.instantiate[CaseClassMock3Args_1Default]("asd", false, 1233) should be (CaseClassMock3Args_1Default("asd", arg2 = false, 1233))
      }

      "should throw an exception, when passing incompatible generic" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.instantiate[String](false, 123.toChar))
      }

      "should instantiate, when to few args" in {
        tto.instantiate[CaseClassMock3Args_1Default]("asd", true) should be (CaseClassMock3Args_1Default("asd", arg2 = true))
      }

      "should throw an exception, when passing wrong arg types" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.instantiate[CaseClassMock2Args_wMethods]("54656", "sdvdvds"))
      }

      "should throw an exception, when to much args" in {
        an[IllegalArgumentException] shouldBe thrownBy(tto.instantiate[CaseClassMock2Args_woMethods](false, 123.toChar, false))
      }
    }
  }
}
