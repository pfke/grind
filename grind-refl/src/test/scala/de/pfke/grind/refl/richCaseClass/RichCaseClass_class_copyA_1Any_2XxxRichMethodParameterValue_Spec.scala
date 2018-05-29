package de.pfke.grind.refl.richCaseClass

import de.pfke.grind.core.refl.{MethodParameterValue, RichCaseClass}
import de.pfke.grind.core.refl.richCaseClass.mocks.{CaseClassMock3Args_0Defaults, CaseClassMock_wOption}
import org.scalatest.{Matchers, WordSpec}

class RichCaseClass_class_copyA_1Any_2XxxRichMethodParameterValue_Spec
  extends WordSpec
    with Matchers {
  "testing case class: 'CaseClassMock3Args_0Defaults'" when {
    lazy val tto = RichCaseClass[CaseClassMock3Args_0Defaults]
    val instance = CaseClassMock3Args_0Defaults("kölklö", arg2 = false, 456)

    "passing the correct params" should {
      "should return correct, when passing all names" in {
        tto.copy(instance,
          MethodParameterValue("arg1", "hhhh"),
          MethodParameterValue("arg2", true),
          MethodParameterValue("arg3", 45647)
        ) should be (CaseClassMock3Args_0Defaults("hhhh", arg2 = true, 45647))
      }

      "should return correct, when passing all names (incorrect order)" in {
        tto.copy(instance,
          MethodParameterValue("arg3", 4564),
          MethodParameterValue("arg1", "hhhdf"),
          MethodParameterValue("arg2", false)
        ) should be (CaseClassMock3Args_0Defaults("hhhdf", arg2 = false, 4564))
      }

      "should return correct, when passing too few names" in {
        tto.copy(instance,
          MethodParameterValue("arg3", 4564),
          MethodParameterValue("arg2", false)
        ) should be (CaseClassMock3Args_0Defaults("kölklö", arg2 = false, 4564))
      }
    }

    "passing the wrong params" should {
      "should throw an exception, when passing wrong param names" in {
        an[IllegalArgumentException] shouldBe thrownBy(
          tto.copy(instance,
            MethodParameterValue("arg31", 4564),
            MethodParameterValue("arg42", false)
          ))
      }

      "should throw an exception, when passing wrong param types" in {
        an[IllegalArgumentException] shouldBe thrownBy(
          tto.copy(instance,
            MethodParameterValue("arg3", "klöklökö"),
            MethodParameterValue("arg1", false)
          ))
      }

      "should throw an exception, when passing too much much" in {
        an[IllegalArgumentException] shouldBe thrownBy(
          tto.copy(instance,
            MethodParameterValue("arg3", 4564),
            MethodParameterValue("arg1", "hhhdf"),
            MethodParameterValue("arg4", "hhhdf"),
            MethodParameterValue("arg2", false)
          ))
      }
    }
  }

  "testing case class: 'CaseClassMock_wOption'" when {
    lazy val tto = RichCaseClass[CaseClassMock_wOption]
    val instance = CaseClassMock_wOption(Some("kölklö"), arg2 = 23, arg3 = Some(false))

    "passing the correct params" should {
      "should return correct, when passing all names" in {
        tto.copy(instance,
          MethodParameterValue("arg1", Some("hhhh")),
          MethodParameterValue("arg2", 132),
          MethodParameterValue("arg3", Some(true))
        ) should be (CaseClassMock_wOption(Some("hhhh"), arg2 = 132, arg3 = Some(true)))
      }

      //"should return correct, when passing all names (incorrect order)" in {
      //  tto.copy(instance,
      //    MethodParameterValue("arg3", 4564),
      //    MethodParameterValue("arg1", "hhhdf"),
      //    MethodParameterValue("arg2", false)
      //  ) should be (CaseClassMock3Args_0Defaults("hhhdf", arg2 = false, 4564))
      //}
      //
      //"should return correct, when passing too few names" in {
      //  tto.copy(instance,
      //    MethodParameterValue("arg3", 4564),
      //    MethodParameterValue("arg2", false)
      //  ) should be (CaseClassMock3Args_0Defaults("kölklö", arg2 = false, 4564))
      //}
    }

    "passing the wrong params" should {
      "should throw an exception, when passing wrong param names" in {
        an[IllegalArgumentException] shouldBe thrownBy(
          tto.copy(instance,
            MethodParameterValue("arg31", 4564),
            MethodParameterValue("arg42", false)
          ))
      }

      "should throw an exception, when passing wrong param types" in {
        an[IllegalArgumentException] shouldBe thrownBy(
          tto.copy(instance,
            MethodParameterValue("arg3", "klöklökö"),
            MethodParameterValue("arg1", false)
          ))
      }

      "should throw an exception, when passing too much much" in {
        an[IllegalArgumentException] shouldBe thrownBy(
          tto.copy(instance,
            MethodParameterValue("arg3", 4564),
            MethodParameterValue("arg1", "hhhdf"),
            MethodParameterValue("arg4", "hhhdf"),
            MethodParameterValue("arg2", false)
          ))
      }
    }
  }
}
