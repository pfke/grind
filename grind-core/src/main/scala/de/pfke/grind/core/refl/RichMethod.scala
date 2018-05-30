package de.pfke.grind.core.refl

import scala.reflect.ClassTag
import scala.reflect.runtime.{universe => ru}

/**
  * Parameter (used to pass arguments to instantiate a class).
  */
case class MethodParameterValue(
  name: String,
  value: Any
)

object RichMethod {
  val TERMNAME_APPLY: ru.TermName = ru.TermName("apply")
  val TERMNAME_CTOR: ru.TermName = ru.termNames.CONSTRUCTOR

  /**
    * Create new object
    */
  def apply[A] (
    methodName: ru.TermName
  )(
    implicit
    classLoader: ClassLoader,
    classTag: ClassTag[A]
  ): List[RichMethod] = apply(getMethodSymbol(symbol = RichRuntimeMirror().getClassSymbol[A](), methodName = methodName))

  /**
    * Create new object
    */
  def apply (
    tpe: ru.Type,
    methodName: ru.TermName
  )(
    implicit
    classLoader: ClassLoader
  ): List[RichMethod] = apply(symbol = tpe.typeSymbol, methodName = methodName)

  /**
    * Create new object
    */
  def apply (
    className: String,
    methodName: ru.TermName
  )(
    implicit
    classLoader: ClassLoader = getClass.getClassLoader
  ): List[RichMethod] = apply(getMethodSymbol(symbol = RichRuntimeMirror().getClassSymbol(className), methodName = methodName))

  /**
    * Create new object
    */
  def apply (
    symbol: ru.Symbol,
    methodName: ru.TermName
  ): List[RichMethod] = apply(getMethodSymbol(symbol = symbol, methodName = methodName))

  /**
    * Create new object
    */
  def apply (
    methodSymbol: ru.MethodSymbol
  ): RichMethod = new RichMethod(methodSymbol)

  /**
    * Create new object
    */
  def apply (
    methodSymbols: List[ru.MethodSymbol]
  ): List[RichMethod] = methodSymbols.map(apply)

  /**
    * Return method mirror for the given class symbol and the given method name.
    */
  def getMethodSymbol(
    symbol: ru.Symbol,
    methodName: ru.TermName
  ): List[ru.MethodSymbol] = {
    val possibleMethodDef = symbol
      .info
      .member(methodName)

    if (!possibleMethodDef.isTerm)
      return List.empty

    val alternatives = possibleMethodDef
      .alternatives
      .filter(_.owner.isClass)

    alternatives
      .filter(_.isMethod)
      .map(_.asMethod)
  }

  /**
    * Konvertiert einen String in einen TermName -> scala.reflect zeuch.
    */
  def toTermName (
    name: String
  ): ru.TermName = ru.TermName(name)
}

class RichMethod (
  val methodSymbol: ru.MethodSymbol
) (
  implicit
  classLoader: ClassLoader = getClass.getClassLoader
) {
  // fields
  private lazy val _richRuntimeMirror = RichRuntimeMirror()(classLoader = classLoader)
  private lazy val _parameter = RichMethodParameterOps.getMethodParameter(methodSymbol = methodSymbol)

  // properties
  def parameter: List[RichMethodParameter] = _parameter

  /**
    * Call the method
    */
  def apply[A] (
    args: Any*
  ) (
    implicit
    classTag: ClassTag[A],
    typeType: ru.TypeTag[A]
  ): A = {
    // merge with default params
    val defaultValues = _parameter
      .drop(args.size)
      .filter(_.defaultValue.nonEmpty)
      .map(_.defaultValue.get)
    val argsToPass = args ++: defaultValues

    require(argsToPass.lengthCompare(methodSymbol.paramLists.head.size) == 0, s"too few/much parameter given (given=${argsToPass.size}, wanted=${methodSymbol.paramLists.head.size})")
    require(methodSymbol.owner.asClass.isCaseClass || methodSymbol.owner.asClass.isClass, "methods owner is neither a case class nor a class, so please provide an instance mirror")

    val methodMirror = methodSymbol match {
      case t if t.isConstructor => reflectCtorMethodMirror()
      case t if t.name == RichMethod.TERMNAME_APPLY => reflectApplyMethodMirror() // für jede compagnion methode geeignet, nicht nur für apply

      case _ => throw new IllegalArgumentException("u want me to call an instance method - impossible, please provide an instance mirror")
    }

    methodMirror
      .apply(argsToPass:_*)
      .asInstanceOf[A]
  }

  /**
    * Call the instance method
    */
  def call[A] (
    instanceMirror: ru.InstanceMirror,
    args: Any*
  ) (
    implicit
    classTag: ClassTag[A],
    typeTag: ru.TypeTag[A]
  ): A = {
    val argsToPass = if (args.nonEmpty) args else getParameterDefaultValues(_parameter)

    require(methodSymbol.paramLists.isEmpty || argsToPass.lengthCompare(methodSymbol.paramLists.head.size) == 0, s"too few/much parameter given (given=${argsToPass.size}, wanted=${methodSymbol.paramLists.head.size})")
    require(methodSymbol.owner.asClass.isCaseClass || methodSymbol.owner.asClass.isClass, "methods owner is neither a case class nor a class, so please provide an instance mirror")
    require(methodSymbol.returnType <:< typeTag.tpe, s"given generic is neither a super nor the same class of the return type ${methodSymbol.returnType}")

    instanceMirror
      .reflectMethod(methodSymbol)
      .apply(argsToPass:_*)
      .asInstanceOf[A]
  }

  /**
    * Returns true, is the given parameter matches our parameters (even in correct order)
    */
  def matchesParameter (
    args: Seq[Any]
  ): Boolean = {
    val argsWithIdx = args
      .map(RichInstanceMirror(_))
      .map(_.asTpe)
      .zipWithIndex

    val paramsWithIdx = _parameter
      .map(_.typeSignature)
      .zipWithIndex

    if (argsWithIdx.lengthCompare(paramsWithIdx.size) != 0)
      return false

    def mapTypes(in: Seq[(ru.Type, Int)]) = (GenericOps.toScalaType(in.head._1), GenericOps.toScalaType(in(1)._1))

    val result = (argsWithIdx ++ paramsWithIdx) // merge two the lists: List(ru.Type, Int)
      .groupBy(_._2)                            // groupd by idx: Map[Int, ArrayBuffer[(ru.Type, Int)]]
      .values                                   // drop idx: List[ArrayBuffer[(ru.Type, Int)]
      .map(mapTypes)                            // map to List[(ru.Type, ru.Type)]
      .collect { case (t1,t2) => t1 =:= t2 }
      .exists { i => !i }

    !result
  }

  /**
    * Return the default values from the reflected parameter.
    * Returned list is empty unless ALL params has a default value.
    */
  private def getParameterDefaultValues(
    params: Seq[RichMethodParameter]
  ): Seq[Any] = {
    if (params.exists(_.defaultValue.isEmpty)) {
      return Seq.empty
    }

    params
      .sortBy(_.index)
      .map(_.defaultValue.get)
  }

  /**
    * Reflect method mirror for apply method
    */
  private def reflectApplyMethodMirror[A] () (
    implicit
    classTag: ClassTag[A],
    typeTag: ru.TypeTag[A]
  ): ru.MethodMirror = {
    val classSymbol = methodSymbol.owner.asClass
    val richInstanceMirror = RichInstanceMirror(classSymbol)
    val methodMirror = richInstanceMirror.getMethodMirror(methodSymbol)

    methodMirror
  }

  /**
    * Reflect method mirror for ctor
    */
  private def reflectCtorMethodMirror[A] () (
    implicit
    classTag: ClassTag[A],
    typeTag: ru.TypeTag[A]
  ): ru.MethodMirror = {
    require(methodSymbol.isConstructor, s"you wanted me to call a ctor, but the given method symbol is not a ctor ($methodSymbol)")
    require(typeTag.tpe <:< methodSymbol.returnType, s"passed generic type ($typeTag) is no assignable to ($methodSymbol)")

    val classSymbol = _richRuntimeMirror.getClassSymbol(methodSymbol.returnType.typeSymbol.fullName)
    val classMirror = _richRuntimeMirror.runtimeMirror.reflectClass(classSymbol)
    val methodMirror = classMirror.reflectConstructor(methodSymbol)

    methodMirror
  }
}
