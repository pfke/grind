package de.pfke.grind.core.refl.richMethod

import de.pfke.grind.core.refl.{RichMethod, RichRuntimeMirror}
import de.pfke.grind.core.refl.richMethod.mocks._

import scala.reflect.runtime.{universe => ru}

class RichMethod_obj_apply_1Symbol_2RuTermName_spec
  extends RichMethodBaseSpec {
  "testing case class: 'CaseClassMock0Args'" when {
    val clazz = classOf[CaseClassMock0Args]
    val classSymbol = RichRuntimeMirror().getClassSymbol(clazz)
    val objectSymbol = RichRuntimeMirror().getClassSymbol(clazz.getCanonicalName + "$")

    "passing the class" should {
      "should return 0 instances ('apply')" in {
        RichMethod.apply(classSymbol, RichMethod.TERMNAME_APPLY).size should be (0)
      }

      "should return 1 instances ('ctor')" in {
        RichMethod.apply(classSymbol, RichMethod.TERMNAME_CTOR).size should be (1)
      }

      "should return 0 instance, w/ non-existing method" in {
        RichMethod.apply(classSymbol, ru.TermName("applssy")).size should be (0)
      }
    }

    "passing the object" should {
      "should return 1 instances ('apply')" in {
        RichMethod.apply(objectSymbol, RichMethod.TERMNAME_APPLY).size should be (1)
      }

      "should return 1 instances ('ctor')" in {
        RichMethod.apply(objectSymbol, RichMethod.TERMNAME_CTOR).size should be (1)
      }

      "should return 0 instance, w/ non-existing method" in {
        RichMethod.apply(objectSymbol, ru.TermName("applssy")).size should be (0)
      }
    }

    "passing the compagnion object" should {
      val compagnionSymbol = classSymbol.companion

      "should return 1 instances ('apply')" in {
        RichMethod.apply(compagnionSymbol, RichMethod.TERMNAME_APPLY).size should be (1)
      }

      "should return 1 instances ('ctor')" in {
        RichMethod.apply(compagnionSymbol, RichMethod.TERMNAME_CTOR).size should be (1)
      }

      "should return 0 instance, w/ non-existing method" in {
        RichMethod.apply(compagnionSymbol, ru.TermName("applssy")).size should be (0)
      }
    }
  }

  "testing case class: 'CaseClassMock2Args_wMethods'" when {
    val clazz = classOf[CaseClassMock2Args_wMethods]

    "passing the class" should {
      val classSymbol = RichRuntimeMirror().getClassSymbol(clazz)

      "should return 0 instances ('apply')" in {
        RichMethod.apply(classSymbol, RichMethod.TERMNAME_APPLY).size should be (0)
      }

      "should return 1 instances ('ctor')" in {
        RichMethod.apply(classSymbol, RichMethod.TERMNAME_CTOR).size should be (1)
      }

      "should return 0 instance, w/ non-existing method" in {
        RichMethod.apply(classSymbol, ru.TermName("applssy")).size should be (0)
      }

      "should return 1 instance, w/ method 'method1'" in {
        RichMethod.apply(classSymbol, ru.TermName("method1")).size should be (1)
      }

      "should return 1 instance, w/ method 'method2'" in {
        RichMethod.apply(classSymbol, ru.TermName("method2")).size should be (1)
      }
    }

    "passing the object" should {
      val classSymbol = RichRuntimeMirror().getClassSymbol(clazz.getCanonicalName + "$")

      "should return 2 instances ('apply')" in {
        RichMethod.apply(classSymbol, RichMethod.TERMNAME_APPLY).size should be (2)
      }

      "should return 1 instances ('ctor')" in {
        RichMethod.apply(classSymbol, RichMethod.TERMNAME_CTOR).size should be (1)
      }

      "should return 0 instance, w/ non-existing method" in {
        RichMethod.apply(classSymbol, ru.TermName("applssy")).size should be (0)
      }

      "should return 0 instance, w/ method 'method1'" in {
        RichMethod.apply(classSymbol, ru.TermName("method1")).size should be (0)
      }

      "should return 0 instance, w/ method 'method2'" in {
        RichMethod.apply(classSymbol, ru.TermName("method2")).size should be (0)
      }
    }
  }

  "testing case class: 'CaseClassMock2Args_woMethods'" when {
    val clazz = classOf[CaseClassMock2Args_woMethods]

    "passing the class" should {
      val classSymbol = RichRuntimeMirror().getClassSymbol(clazz)

      "should return 0 instances ('apply')" in {
        RichMethod.apply(classSymbol, RichMethod.TERMNAME_APPLY).size should be (0)
      }

      "should return 1 instances ('ctor')" in {
        RichMethod.apply(classSymbol, RichMethod.TERMNAME_CTOR).size should be (1)
      }

      "should return 0 instance, w/ non-existing method" in {
        RichMethod.apply(classSymbol, ru.TermName("applssy")).size should be (0)
      }
    }

    "passing the object" should {
      val classSymbol = RichRuntimeMirror().getClassSymbol(clazz.getCanonicalName + "$")

      "should return 2 instances ('apply')" in {
        RichMethod.apply(classSymbol, RichMethod.TERMNAME_APPLY).size should be (2)
      }

      "should return 1 instances ('ctor')" in {
        RichMethod.apply(classSymbol, RichMethod.TERMNAME_CTOR).size should be (1)
      }

      "should return 0 instance, w/ non-existing method" in {
        RichMethod.apply(classSymbol, ru.TermName("applssy")).size should be (0)
      }
    }
  }

  "testing case class: 'CaseClassMock2Args_wOverloadedMethods'" when {
    val clazz = classOf[CaseClassMock2Args_wOverloadedMethods]

    "passing the class" should {
      val classSymbol = RichRuntimeMirror().getClassSymbol(clazz)

      "should return 0 instances ('apply')" in {
        RichMethod.apply(classSymbol, RichMethod.TERMNAME_APPLY).size should be (0)
      }

      "should return 1 instances ('ctor')" in {
        RichMethod.apply(classSymbol, RichMethod.TERMNAME_CTOR).size should be (1)
      }

      "should return 0 instance, w/ non-existing method" in {
        RichMethod.apply(classSymbol, ru.TermName("applssy")).size should be (0)
      }

      "should return 1 instance, w/ method 'method1'" in {
        RichMethod.apply(classSymbol, ru.TermName("method1")).size should be (1)
      }

      "should return 3 instance, w/ method 'method2'" in {
        RichMethod.apply(classSymbol, ru.TermName("method2")).size should be (3)
      }
    }

    "passing the object" should {
      val classSymbol = RichRuntimeMirror().getClassSymbol(clazz.getCanonicalName + "$")

      "should return 2 instances ('apply')" in {
        RichMethod.apply(classSymbol, RichMethod.TERMNAME_APPLY).size should be (2)
      }

      "should return 1 instances ('ctor')" in {
        RichMethod.apply(classSymbol, RichMethod.TERMNAME_CTOR).size should be (1)
      }

      "should return 0 instance, w/ non-existing method" in {
        RichMethod.apply(classSymbol, ru.TermName("applssy")).size should be (0)
      }

      "should return 0 instance, w/ method 'method1'" in {
        RichMethod.apply(classSymbol, ru.TermName("method1")).size should be (0)
      }

      "should return 0 instance, w/ method 'method2'" in {
        RichMethod.apply(classSymbol, ru.TermName("method2")).size should be (0)
      }
    }
  }

  "testing case class: 'CaseClassMock3Args_0Defaults'" when {
    val clazz = classOf[CaseClassMock3Args_0Defaults]

    "passing the class" should {
      val classSymbol = RichRuntimeMirror().getClassSymbol(clazz)

      "should return 0 instances ('apply')" in {
        RichMethod.apply(classSymbol, RichMethod.TERMNAME_APPLY).size should be (0)
      }

      "should return 1 instances ('ctor')" in {
        RichMethod.apply(classSymbol, RichMethod.TERMNAME_CTOR).size should be (1)
      }

      "should return 0 instance, w/ non-existing method" in {
        RichMethod.apply(classSymbol, ru.TermName("applssy")).size should be (0)
      }

      "should return 0 instance, w/ method 'method1'" in {
        RichMethod.apply(classSymbol, ru.TermName("method1")).size should be (0)
      }
    }

    "passing the object" should {
      val classSymbol = RichRuntimeMirror().getClassSymbol(clazz.getCanonicalName + "$")

      "should return 2 instances ('apply')" in {
        RichMethod.apply(classSymbol, RichMethod.TERMNAME_APPLY).size should be (2)
      }

      "should return 1 instances ('ctor')" in {
        RichMethod.apply(classSymbol, RichMethod.TERMNAME_CTOR).size should be (1)
      }

      "should return 0 instance, w/ non-existing method" in {
        RichMethod.apply(classSymbol, ru.TermName("applssy")).size should be (0)
      }

      "should return 0 instance, w/ method 'method1'" in {
        RichMethod.apply(classSymbol, ru.TermName("method1")).size should be (0)
      }
    }
  }

  "testing case class: 'CaseClassMock3Args_1Default'" when {
    val clazz = classOf[CaseClassMock3Args_1Default]

    "passing the class" should {
      val classSymbol = RichRuntimeMirror().getClassSymbol(clazz)

      "should return 0 instances ('apply')" in {
        RichMethod.apply(classSymbol, RichMethod.TERMNAME_APPLY).size should be (0)
      }

      "should return 1 instances ('ctor')" in {
        RichMethod.apply(classSymbol, RichMethod.TERMNAME_CTOR).size should be (1)
      }

      "should return 0 instance, w/ non-existing method" in {
        RichMethod.apply(classSymbol, ru.TermName("applssy")).size should be (0)
      }

      "should return 0 instance, w/ method 'method1'" in {
        RichMethod.apply(classSymbol, ru.TermName("method1")).size should be (0)
      }
    }

    "passing the object" should {
      val classSymbol = RichRuntimeMirror().getClassSymbol(clazz.getCanonicalName + "$")

      "should return 2 instances ('apply')" in {
        RichMethod.apply(classSymbol, RichMethod.TERMNAME_APPLY).size should be (2)
      }

      "should return 1 instances ('ctor')" in {
        RichMethod.apply(classSymbol, RichMethod.TERMNAME_CTOR).size should be (1)
      }

      "should return 0 instance, w/ non-existing method" in {
        RichMethod.apply(classSymbol, ru.TermName("applssy")).size should be (0)
      }

      "should return 0 instance, w/ method 'method1'" in {
        RichMethod.apply(classSymbol, ru.TermName("method1")).size should be (0)
      }
    }
  }

  "testing case class: 'CaseClassMock3Args_2Defaults'" when {
    val clazz = classOf[CaseClassMock3Args_2Defaults]

    "passing the class" should {
      val classSymbol = RichRuntimeMirror().getClassSymbol(clazz)

      "should return 0 instances ('apply')" in {
        RichMethod.apply(classSymbol, RichMethod.TERMNAME_APPLY).size should be (0)
      }

      "should return 1 instances ('ctor')" in {
        RichMethod.apply(classSymbol, RichMethod.TERMNAME_CTOR).size should be (1)
      }

      "should return 0 instance, w/ non-existing method" in {
        RichMethod.apply(classSymbol, ru.TermName("applssy")).size should be (0)
      }

      "should return 0 instance, w/ method 'method1'" in {
        RichMethod.apply(classSymbol, ru.TermName("method1")).size should be (0)
      }
    }

    "passing the object" should {
      val classSymbol = RichRuntimeMirror().getClassSymbol(clazz.getCanonicalName + "$")

      "should return 2 instances ('apply')" in {
        RichMethod.apply(classSymbol, RichMethod.TERMNAME_APPLY).size should be (2)
      }

      "should return 1 instances ('ctor')" in {
        RichMethod.apply(classSymbol, RichMethod.TERMNAME_CTOR).size should be (1)
      }

      "should return 0 instance, w/ non-existing method" in {
        RichMethod.apply(classSymbol, ru.TermName("applssy")).size should be (0)
      }

      "should return 0 instance, w/ method 'method1'" in {
        RichMethod.apply(classSymbol, ru.TermName("method1")).size should be (0)
      }
    }
  }

  "testing case class: 'CaseClassMock3Args_allDefaults'" when {
    val clazz = classOf[CaseClassMock3Args_allDefaults]

    "passing the class" should {
      val classSymbol = RichRuntimeMirror().getClassSymbol(clazz)

      "should return 0 instances ('apply')" in {
        RichMethod.apply(classSymbol, RichMethod.TERMNAME_APPLY).size should be (0)
      }

      "should return 1 instances ('ctor')" in {
        RichMethod.apply(classSymbol, RichMethod.TERMNAME_CTOR).size should be (1)
      }

      "should return 0 instance, w/ non-existing method" in {
        RichMethod.apply(classSymbol, ru.TermName("applssy")).size should be (0)
      }

      "should return 0 instance, w/ method 'method1'" in {
        RichMethod.apply(classSymbol, ru.TermName("method1")).size should be (0)
      }
    }

    "passing the object" should {
      val classSymbol = RichRuntimeMirror().getClassSymbol(clazz.getCanonicalName + "$")

      "should return 2 instances ('apply')" in {
        RichMethod.apply(classSymbol, RichMethod.TERMNAME_APPLY).size should be (2)
      }

      "should return 1 instances ('ctor')" in {
        RichMethod.apply(classSymbol, RichMethod.TERMNAME_CTOR).size should be (1)
      }

      "should return 0 instance, w/ non-existing method" in {
        RichMethod.apply(classSymbol, ru.TermName("applssy")).size should be (0)
      }

      "should return 0 instance, w/ method 'method1'" in {
        RichMethod.apply(classSymbol, ru.TermName("method1")).size should be (0)
      }
    }
  }

  "testing class: 'ClassMock0Args'" when {
    val clazz = classOf[ClassMock0Args]

    "passing the class" should {
      val classSymbol = RichRuntimeMirror().getClassSymbol(clazz)

      "should return 0 instances ('apply')" in {
        RichMethod.apply(classSymbol, RichMethod.TERMNAME_APPLY).size should be (0)
      }

      "should return 1 instances ('ctor')" in {
        RichMethod.apply(classSymbol, RichMethod.TERMNAME_CTOR).size should be (1)
      }

      "should return 0 instance, w/ non-existing method" in {
        RichMethod.apply(classSymbol, ru.TermName("applssy")).size should be (0)
      }

      "should return 0 instance, w/ method 'method1'" in {
        RichMethod.apply(classSymbol, ru.TermName("method1")).size should be (0)
      }
    }
  }
}
