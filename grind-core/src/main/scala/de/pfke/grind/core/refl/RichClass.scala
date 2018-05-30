package de.pfke.grind.core.refl

import scala.reflect.ClassTag
import scala.reflect.runtime.{universe => ru}

object RichClass {
  /**
    * Create instance
    */
  def apply (
    clazz: Class[_]
  ): RichClass = apply(clazz.getCanonicalName)(clazz.getClassLoader)

  /**
    * Create instance
    */
  def apply[A] () (
    implicit
    classTag: ClassTag[A]
  ): RichClass = apply(classTag.runtimeClass)

  /**
    * Create instance
    */
  def apply (
    className: String
  ) (
    implicit
    classLoader: ClassLoader = getClass.getClassLoader
  ): RichClass = new RichClass(classSymbol = RichRuntimeMirror().getClassSymbol(className = className))
}

class RichClass (
  classSymbol: ru.ClassSymbol
) (
  implicit
  classLoader: ClassLoader = getClass.getClassLoader
) extends RichBaseClass (
  classSymbol = classSymbol
) {
  require(ClassOps.isClass(classSymbol), s"passed class: '${classSymbol.fullName}' is not a case class")

  // fields
  private val _richClassMirror = RichClassMirror(classSymbol = classSymbol)

  // requirements
  require(ctorRichMethodRefls.nonEmpty, s"could not find any apply method of case class ${classSymbol.typeSignature}")

  /**
    * Getter
    */
  def ctorRichMethodRefls: List[RichMethod] = _richClassMirror.ctorMethodRefls
  def ctorMethods: List[RichMethod] = ctorRichMethodRefls
  def ctorMethodParameters: List[List[RichMethodParameter]] = ctorRichMethodRefls.map(_.parameter)

  /**
    * Instantiate this class, using the passed args
    * @param args apply method arguments
    * @return
    */
  def instantiate[A] (
    args: Any*
  ) (
    implicit
    classTag: ClassTag[A],
    typeTag: ru.TypeTag[A]
  ): A = {
    require(classSymbol.selfType <:< typeTag.tpe, s"given generic is neither a super nor the same class of ${classSymbol.selfType}")

    findCtorMatchingTheseValueTypes(args:_*) match {
      case Some(t) => t.apply[A](args:_*)
      case None => throw new IllegalArgumentException(s"no apply method found, which matches the passed args: '${args.mkString(", ")}'")
    }
  }

  /**
    * Return the apply RichMethodRefl which contains the given name
    */
  def findCtorMatchingTheseNames (
    methodNames: String*
  ): Option[RichMethod] = {
    def hasAllParamNames (in: RichMethod): Boolean = methodNames.forall { i => in.parameter.exists(_.name == i) }

    ctorRichMethodRefls
      .find(hasAllParamNames)
  }

  /**
    * Return the apply RichMethodRefl which contains the given name
    */
  def findCtorMatchingTheseValueTypes (
    args: Any*
  ): Option[RichMethod] = findMethod_matching_methodName_and_paramTypes(methods = ctorRichMethodRefls, args = args)
}
