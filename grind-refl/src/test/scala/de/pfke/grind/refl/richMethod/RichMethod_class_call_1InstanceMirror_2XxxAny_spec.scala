package de.pfke.grind.refl.richMethod

import de.pfke.grind.core.refl.{RichMethod, RichRuntimeMirror}
import de.pfke.grind.core.refl.richMethod.mocks.{CaseClassMock2Args_wMethods, CaseClassMock2Args_wOverloadedMethods}

import scala.reflect.runtime.{universe => ru}

class RichMethod_class_call_1InstanceMirror_2XxxAny_spec
  extends RichMethodBaseSpec {
  "testing case class: 'CaseClassMock2Args_wMethods'" when {
    val clazz = classOf[CaseClassMock2Args_wMethods]
    val classSymbol = RichRuntimeMirror().getClassSymbol(clazz)
//    val objectSymbol = RichRuntimeMirror().getClassSymbol(clazz.getCanonicalName + "$")

    "call 'method1'" should {
      val method = RichMethod.apply(classSymbol, ru.TermName("method1")).head

      "should return correct, w/ correct args" in {
        method.call[String](
          RichRuntimeMirror().getInstanceMirror(CaseClassMock2Args_wMethods("", 0))
        ) should be ("method1")
      }

      "should throw an exception, w/ wrong generic" in {
        an[IllegalArgumentException] shouldBe thrownBy(
          method.call[Int](
            RichRuntimeMirror().getInstanceMirror(CaseClassMock2Args_wMethods("", 0))
          ))
      }

      "should throw an exception, w/ too much args" in {
        an[IllegalArgumentException] shouldBe thrownBy(
          method.call[Int](
            RichRuntimeMirror().getInstanceMirror(CaseClassMock2Args_wMethods("", 0)),
            "jkljlk"
          ))
      }
    }

    "call 'method2'" should {
      val method = RichMethod.apply(classSymbol, ru.TermName("method2")).head

      "should return correct, w/ correct args" in {
        method.call[String](
          RichRuntimeMirror().getInstanceMirror(CaseClassMock2Args_wMethods("", 0)),
          "hello",
          13
        ) should be ("method2: hello, 13")
      }

      "should throw an exception, w/ wrong generic" in {
        an[IllegalArgumentException] shouldBe thrownBy(
          method.call[Int](
            RichRuntimeMirror().getInstanceMirror(CaseClassMock2Args_wMethods("", 0))
          ))
      }

      "should throw an exception, w/ too few args" in {
        an[IllegalArgumentException] shouldBe thrownBy(
          method.call[Int](
            RichRuntimeMirror().getInstanceMirror(CaseClassMock2Args_wMethods("", 0)),
            "hello"
          ))
      }

      "should throw an exception, w/ too much args" in {
        an[IllegalArgumentException] shouldBe thrownBy(
          method.call[Int](
            RichRuntimeMirror().getInstanceMirror(CaseClassMock2Args_wMethods("", 0)),
            "hello",
            13,
            "jkljlk"
          ))
      }
    }
  }

  "testing case class: 'CaseClassMock2Args_wOverloadedMethods'" when {
    val clazz = classOf[CaseClassMock2Args_wOverloadedMethods]
    val classSymbol = RichRuntimeMirror().getClassSymbol(clazz)
//    val objectSymbol = RichRuntimeMirror().getClassSymbol(clazz.getCanonicalName + "$")

    "call 'method1'" should {
      val method = RichMethod.apply(classSymbol, ru.TermName("method1")).head

      "should return correct, w/ correct args" in {
        method.call[String](
          RichRuntimeMirror().getInstanceMirror(CaseClassMock2Args_wOverloadedMethods("", 0))
        ) should be ("method1")
      }

      "should throw an exception, w/ wrong generic" in {
        an[IllegalArgumentException] shouldBe thrownBy(
          method.call[Int](
            RichRuntimeMirror().getInstanceMirror(CaseClassMock2Args_wOverloadedMethods("", 0))
          ))
      }

      "should throw an exception, w/ too much args" in {
        an[IllegalArgumentException] shouldBe thrownBy(
          method.call[Int](
            RichRuntimeMirror().getInstanceMirror(CaseClassMock2Args_wOverloadedMethods("", 0)),
            "jkljlk"
          ))
      }
    }

    "call 'method2(String)'" should {
      val method = RichMethod
        .apply(classSymbol, ru.TermName("method2"))
        .find(_.methodSymbol.paramLists.head.lengthCompare(1) == 0)
        .get

      "should return correct, w/ correct args" in {
        method.call[String](
          RichRuntimeMirror().getInstanceMirror(CaseClassMock2Args_wOverloadedMethods("", 0)),
          "jkljkljlk"
        ) should be ("method2(String): jkljkljlk")
      }

      "should throw an exception, w/ wrong generic" in {
        an[IllegalArgumentException] shouldBe thrownBy(
          method.call[Int](
            RichRuntimeMirror().getInstanceMirror(CaseClassMock2Args_wOverloadedMethods("", 0)),
            "jkljkljlk"
          ))
      }

      "should throw an exception, w/ too few args" in {
        an[IllegalArgumentException] shouldBe thrownBy(
          method.call[Int](
            RichRuntimeMirror().getInstanceMirror(CaseClassMock2Args_wOverloadedMethods("", 0))
          ))
      }

      "should throw an exception, w/ too much args" in {
        an[IllegalArgumentException] shouldBe thrownBy(
          method.call[Int](
            RichRuntimeMirror().getInstanceMirror(CaseClassMock2Args_wOverloadedMethods("", 0)),
            "jkljlk",
            123
          ))
      }
    }

    "call 'method2(String, Int)'" should {
      val method = RichMethod
        .apply(classSymbol, ru.TermName("method2"))
        .find(_.methodSymbol.paramLists.head.lengthCompare(2) == 0)
        .get

      "should return correct, w/ correct args" in {
        method.call[String](
          RichRuntimeMirror().getInstanceMirror(CaseClassMock2Args_wOverloadedMethods("", 0)),
          "jkljkljlk",
          13
        ) should be ("method2(String, Int): jkljkljlk, 13")
      }

      "should throw an exception, w/ wrong generic" in {
        an[IllegalArgumentException] shouldBe thrownBy(
          method.call[Int](
            RichRuntimeMirror().getInstanceMirror(CaseClassMock2Args_wOverloadedMethods("", 0)),
            "jkljkljlk",
            15
          ))
      }

      "should throw an exception, w/ too few args" in {
        an[IllegalArgumentException] shouldBe thrownBy(
          method.call[Int](
            RichRuntimeMirror().getInstanceMirror(CaseClassMock2Args_wOverloadedMethods("", 0))
          ))
      }

      "should throw an exception, w/ too much args" in {
        an[IllegalArgumentException] shouldBe thrownBy(
          method.call[Int](
            RichRuntimeMirror().getInstanceMirror(CaseClassMock2Args_wOverloadedMethods("", 0)),
            "jkljlk",
            123,
            123
          ))
      }
    }

    "call 'method2(Int, String, Long)'" should {
      val method = RichMethod
        .apply(classSymbol, ru.TermName("method2"))
        .find(_.methodSymbol.paramLists.head.lengthCompare(3) == 0)
        .get

      "should return correct, w/ correct args" in {
        method.call[String](
          RichRuntimeMirror().getInstanceMirror(CaseClassMock2Args_wOverloadedMethods("", 0)),
          46.toByte,
          "jkljkljlk",
          13l
        ) should be ("method2(Byte, String, Long): 46, jkljkljlk, 13")
      }

      "should throw an exception, w/ wrong generic" in {
        an[IllegalArgumentException] shouldBe thrownBy(
          method.call[Int](
            RichRuntimeMirror().getInstanceMirror(CaseClassMock2Args_wOverloadedMethods("", 0)),
            46,
            "jkljkljlk",
            15l
          ))
      }

      "should throw an exception, w/ too few args" in {
        an[IllegalArgumentException] shouldBe thrownBy(
          method.call[Int](
            RichRuntimeMirror().getInstanceMirror(CaseClassMock2Args_wOverloadedMethods("", 0))
          ))
      }

      "should throw an exception, w/ too much args" in {
        an[IllegalArgumentException] shouldBe thrownBy(
          method.call[Int](
            RichRuntimeMirror().getInstanceMirror(CaseClassMock2Args_wOverloadedMethods("", 0)),
            46,
            "jkljlk",
            123l,
            123
          ))
      }
    }
  }

//  "testing case class: 'ClassMock2Args_wOverloadedMethods'" when {
//    val clazz = classOf[ClassMock2Args_wOverloadedMethods]
//    val classSymbol = RichRuntimeMirror().getClassSymbol(clazz)
//
//  }
}
