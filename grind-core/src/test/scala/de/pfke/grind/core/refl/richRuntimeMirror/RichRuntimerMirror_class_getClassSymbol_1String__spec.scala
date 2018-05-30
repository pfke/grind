package de.pfke.grind.core.refl.richRuntimeMirror

import de.pfke.grind.core.refl.RichRuntimeMirror
import org.scalatest.{Matchers, WordSpec}

class RichRuntimerMirror_class_getClassSymbol_1String__spec
  extends WordSpec
      with Matchers {
  val namespace = "de.pfke.grind.core.refl.richRuntimeMirror.mocks"
  val richRuntimeMirror = RichRuntimeMirror()

  "testing w/ non-existing class name" when {
    val className = "jibbetnich"

    "passing the compagnion class name (suffixed w/ '$')" should {
      val modClassName = buildModClassName(className)

      "should throw an exception" in {
        an[ScalaReflectionException] shouldBe thrownBy(richRuntimeMirror.getClassSymbol(modClassName))
      }
    }

    "passing the class name" should {
      val realClassName = buildRealClassName(className)

      "should throw an exception" in {
        an[ScalaReflectionException] shouldBe thrownBy(richRuntimeMirror.getClassSymbol(realClassName))
      }
    }
  }

  "testing case class: 'CaseClassMock0Args'" when {
    val className = "CaseClassMock0Args"

    "passing the compagnion class name (suffixed w/ '$')" should {
      val modClassName = buildModClassName(className)

      "should return an instances" in {
        richRuntimeMirror.getClassSymbol(modClassName) should not be null
      }

      "should return an instance w/ correct name" in {
        richRuntimeMirror.getClassSymbol(modClassName).fullName should be (modClassName)
      }
    }

    "passing the class name" should {
      val realClassName = buildRealClassName(className)

      "should return an instances" in {
        richRuntimeMirror.getClassSymbol(realClassName) should not be null
      }

      "should return an instance w/ correct name" in {
        richRuntimeMirror.getClassSymbol(realClassName).fullName should be (realClassName)
      }
    }
  }

  "testing class: 'ClassMock0Args'" when {
    val className = "ClassMock0Args"

    "passing the compagnion class name (suffixed w/ '$')" should {
      val modClassName = buildModClassName(className)

      "should throw an exception" in {
        an[ScalaReflectionException] shouldBe thrownBy(richRuntimeMirror.getClassSymbol(modClassName))
      }
    }

    "passing the class name" should {
      val realClassName = buildRealClassName(className)

      "should return an instances" in {
        richRuntimeMirror.getClassSymbol(realClassName) should not be null
      }

      "should return an instance w/ correct name" in {
        richRuntimeMirror.getClassSymbol(realClassName).fullName should be (realClassName)
      }
    }
  }

  "testing object: 'ObjectMock'" when {
    val className = "ObjectMock"

    "passing the compagnion class name (suffixed w/ '$')" should {
      val modClassName = buildModClassName(className)

      "should return an instances" in {
        richRuntimeMirror.getClassSymbol(modClassName) should not be null
      }

      "should return an instance w/ correct name" in {
        richRuntimeMirror.getClassSymbol(modClassName).fullName should be (modClassName)
      }
    }

    "passing the class name" should {
      val realClassName = buildRealClassName(className)

      "should return an instances" in {
        richRuntimeMirror.getClassSymbol(realClassName) should not be null
      }

      "should return an instance w/ correct name" in {
        richRuntimeMirror.getClassSymbol(realClassName).fullName should be (realClassName)
      }
    }
  }

  protected def buildModClassName(className: String) = s"${buildRealClassName(className)}$$"
  protected def buildRealClassName(className: String) = s"$namespace.$className"
}
