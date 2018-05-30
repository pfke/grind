package de.pfke.grind.core.refl.richRuntimeMirror

import de.pfke.grind.core.refl.RichRuntimeMirror
import org.scalatest.{Matchers, WordSpec}

class RichRuntimerMirror_class_getInstanceMirror_1String_spec
  extends WordSpec
      with Matchers {
  val namespace = "de.pfke.grind.core.refl.richRuntimeMirror.mocks"
  val richRuntimeMirror = RichRuntimeMirror()

  "testing w/ non-existing class name" when {
    val className = "jibbetnich"

    "passing the compagnion class name (suffixed w/ '$')" should {
      val modClassName = buildModClassName(className)

      "should throw an exception" in {
        an[ScalaReflectionException] shouldBe thrownBy(richRuntimeMirror.getInstanceMirror(modClassName))
      }
    }

    "passing the class name" should {
      val realClassName = buildRealClassName(className)

      "should throw an exception" in {
        an[ScalaReflectionException] shouldBe thrownBy(richRuntimeMirror.getInstanceMirror(realClassName))
      }
    }
  }

  "testing case class: 'CaseClassMock0Args'" when {
    val className = "CaseClassMock0Args"

    "passing the compagnion class name (suffixed w/ '$')" should {
      val modClassName = buildModClassName(className)

      "should throw an exception" in {
        an[IllegalArgumentException] shouldBe thrownBy(richRuntimeMirror.getInstanceMirror(modClassName))
      }
    }

    "passing the class name" should {
      val realClassName = buildRealClassName(className)

      "should return an instances" in {
        richRuntimeMirror.getInstanceMirror(realClassName) should not be null
      }

      "should return an instance w/ correct name" in {
        richRuntimeMirror.getInstanceMirror(realClassName).symbol.fullName should be (realClassName)
      }
    }
  }

  "testing class: 'ClassMock0Args'" when {
    val className = "ClassMock0Args"

    "passing the compagnion class name (suffixed w/ '$')" should {
      val modClassName = buildModClassName(className)

      "should throw an exception" in {
        an[ScalaReflectionException] shouldBe thrownBy(richRuntimeMirror.getInstanceMirror(modClassName))
      }
    }

    "passing the class name" should {
      val realClassName = buildRealClassName(className)

      "should throw an exception" in {
        an[IllegalArgumentException] shouldBe thrownBy(richRuntimeMirror.getInstanceMirror(realClassName))
      }
    }
  }

  "testing object: 'ObjectMock'" when {
    val className = "ObjectMock"

    "passing the compagnion class name (suffixed w/ '$')" should {
      val modClassName = buildModClassName(className)

      "should throw an exception" in {
        an[IllegalArgumentException] shouldBe thrownBy(richRuntimeMirror.getInstanceMirror(modClassName))
      }
    }

    "passing the class name" should {
      val realClassName = buildRealClassName(className)

      "should throw an exception" in {
        an[IllegalArgumentException] shouldBe thrownBy(richRuntimeMirror.getInstanceMirror(realClassName))
      }
    }
  }

  protected def buildModClassName(className: String) = s"${buildRealClassName(className)}$$"
  protected def buildRealClassName(className: String) = s"$namespace.$className"
}
