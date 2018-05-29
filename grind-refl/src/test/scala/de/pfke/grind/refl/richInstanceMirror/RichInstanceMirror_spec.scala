package de.pfke.grind.refl.richInstanceMirror

import de.pfke.grind.core.refl.RichInstanceMirror
import org.scalatest.{Matchers, WordSpec}

import scala.reflect.runtime.{universe => ru}

class RichInstanceMirror_spec
  extends WordSpec
    with Matchers {
  "Build a RichInstanceMirror from a case class" when {
    "testing method 'fullName'" should {
      "should return correct class name" in {
        RichInstanceMirror(classOf[CaseClassMock]).fullname should be (ru.typeOf[CaseClassMock].typeSymbol.asClass.fullName)
      }
    }

    "testing method 'call(String, Any*)'" should {
      "should return Some(object), when called with correct params" in {
        RichInstanceMirror(classOf[CaseClassMock]).call("apply", "heinze", 145) should be (Some(CaseClassMock("heinze", 145)))
      }

      "should return None, when called with wrong params" in {
        RichInstanceMirror(classOf[CaseClassMock]).call("apply", 156, "heinze") should be (None)
      }
    }
  }

  "Build a RichInstanceMirror from a class" when {
    "testing result" should {
      "throw an exception" in {
        an[IllegalArgumentException] shouldBe thrownBy(RichInstanceMirror(classOf[ClassMock]))
      }
    }
  }
}
