package de.pfke.grind.core.refl

import scala.reflect.ClassTag
import scala.reflect.runtime.{universe => ru}

abstract class RichBaseClass (
  classSymbol: ru.ClassSymbol
) (
  implicit
  classLoader: ClassLoader
) {
  /**
    * Getter
    */
  def className: String = ClassOps.getClassName(classSymbol)
  def typeSignature: ru.Type = ClassOps.typeSignature(classSymbol)

  /**
    * Call an instance method (instance given)
    *
    * @param instance object
    * @param methodName name of the function
    * @param args args to the method
    * @param classTag is the type of the method return value
    * @param typeTag is the type of the method return value
    * @tparam A is the type of the method return value
    * @return method return value
    */
  def call[A] (
    instance: Any,
    methodName: String,
    args: Any*
  ) (
    implicit
    classTag: ClassTag[A],
    typeTag: ru.TypeTag[A]
  ): A = {
    call[A](
      instanceMirror = RichRuntimeMirror(instance.getClass).getInstanceMirror(instance),
      methodName = methodName,
      args = args:_*
    )
  }

  /**
    * Call an instance method (instance mirror given)
    *
    * @param instanceMirror ...
    * @param methodName name of the function
    * @param args args to the method
    * @param classTag is the type of the method return value
    * @param typeTag is the type of the method return value
    * @tparam A is the type of the method return value
    * @return method return value
    */
  def call[A] (
    instanceMirror: ru.InstanceMirror,
    methodName: String,
    args: Any*
  ) (
    implicit
    classTag: ClassTag[A],
    typeTag: ru.TypeTag[A]
  ): A = {
    val richMethodReflOpt = RichMethod(classSymbol, ru.TermName(methodName))
      .find(_.matchesParameter(args))

    require(richMethodReflOpt.nonEmpty, s"[$className] no method with the passed parameter types+order found (passed: $args)")
    val richMethodRefl = richMethodReflOpt.get

    val methodReturnType = richMethodRefl
      .methodSymbol
      .returnType

    require(methodReturnType <:< typeTag.tpe, s"[$className] return type of method $methodName ($methodReturnType) is different to passed generic (${typeTag.tpe})")

    richMethodRefl.call[A](instanceMirror, args:_*)
  }

  /**
    * Findet die Methode mit angegebenem Namen und den vorhandenen Paremetern
    */
  def findMethod_matching_methodName_and_paramNames (
    useCompanion: Boolean,
    methodName: String,
    paramNames: Seq[String]
  ): Option[RichMethod] = {
    findMethod_matching_methodName_and_paramNames(
      useCompanion = useCompanion,
      methodName = RichMethod.toTermName(methodName),
      paramNames = paramNames
    )
  }

  /**
    * Findet die Methode mit angegebenem Namen und den vorhandenen Paremetern
    */
  def findMethod_matching_methodName_and_paramNames (
    useCompanion: Boolean,
    methodName: ru.TermName,
    paramNames: Seq[String]
  ): Option[RichMethod] = {
    findMethod_matching_methodName_and_paramNames(
      symbol = if (useCompanion) classSymbol.companion else classSymbol,
      methodName = methodName,
      paramNames = paramNames
    )
  }

  /**
    * Findet die Methode mit angegebenem Namen und den vorhandenen Paremetern
    */
  def findMethod_matching_methodName_and_paramNames (
    symbol: ru.Symbol,
    methodName: ru.TermName,
    paramNames: Seq[String]
  ): Option[RichMethod] = {
    def hasAllParamNames (in: RichMethod): Boolean = paramNames.forall { i => in.parameter.exists(_.name == i) }

    RichMethod(symbol, methodName)
      .find(hasAllParamNames)
  }

  /**
    * Return the apply RichMethodRefl which contains the given name
    */
  def findMethod_matching_methodName_and_paramTypes (
    useCompanion: Boolean,
    methodName: String,
    args: Seq[Any]
  ): Option[RichMethod] = {
    findMethod_matching_methodName_and_paramTypes(
      useCompanion = useCompanion,
      methodName = RichMethod.toTermName(methodName),
      args = args
    )
  }

  /**
    * Return the apply RichMethodRefl which contains the given name
    */
  def findMethod_matching_methodName_and_paramTypes (
    useCompanion: Boolean,
    methodName: ru.TermName,
    args: Seq[Any]
  ): Option[RichMethod] = {
    findMethod_matching_methodName_and_paramTypes(
      methods = RichMethod(symbol = if (useCompanion) classSymbol.companion else classSymbol, methodName = methodName),
      args = args
    )
  }

  /**
    * Return the apply RichMethodRefl which contains the given name
    */
  protected def findMethod_matching_methodName_and_paramTypes (
    methods: List[RichMethod],
    args: Seq[Any]
  ): Option[RichMethod] = {
    case class EnrichedArg(tpe: ru.Type, clazz: Class[_])

    val enrichedArgs = args.map { i => EnrichedArg(GenericOps.toScalaType(i), GenericOps.toScalaClass(i)) }

    def mapParamToSimple(in: RichMethodParameter) = EnrichedArg(PrimitiveOps.toScalaType(in.typeSignature), in.clazz)
    def isAssignableFrom(_1: Class[_], _2: Class[_]) = _1.isAssignableFrom(_2)
    def isTypeFrom(_1: ru.Type, _2: ru.Type) = _1 <:< _2

    def hasAllParamTypes (in: RichMethod): Boolean = {
      if (args.lengthCompare(in.parameter.size) > 0)
        return false

      val paramsAsTuple = in.parameter.map(mapParamToSimple)
      val paddedArgs = enrichedArgs ++ paramsAsTuple.drop(enrichedArgs.size)

      val classCheck = paramsAsTuple.zipWithIndex.map { i => isAssignableFrom(i._1.clazz, paddedArgs(i._2).clazz) }
      val typeCheck = paramsAsTuple.zipWithIndex.map { i => isTypeFrom(i._1.tpe, paddedArgs(i._2).tpe) }

      val res = !typeCheck
        .zip(classCheck)
        .forall { i => i._1 || i._2 }

      !res
    }

    methods
      .find(hasAllParamTypes)
  }
}
