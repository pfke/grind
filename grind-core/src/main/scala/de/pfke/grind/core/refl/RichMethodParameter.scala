package de.pfke.grind.core.refl

import de.pfke.grind.core.data.byTypes.complex.OptionOps

import scala.annotation.StaticAnnotation
import scala.reflect.ClassTag
import scala.reflect.runtime.{universe => ru}

/**
  * Method parameter (got by reflection).
  *
  * @param index calling position
  * @param symbol parmater symbol
  */
case class RichMethodParameter (
  index: Int,
  symbol: ru.Symbol,
  defaultValue: Option[Any] = None
) {
  lazy val clazz: Class[_] = Class.forName(typeSignature.typeSymbol.asClass.fullName)

  /**
    * Return field annotations
    */
  def annotations: List[ru.Annotation] = symbol.annotations

  /**
    * Return param name
    */
  def name: String = symbol.name.toString

  /**
    * Return param type
    */
  def typeSignature: ru.Type = symbol.typeSignature
}

object RichMethodParameterOps {
  /**
    * Returns the wanted annotation
    */
  def getAnnot[A <: StaticAnnotation] (
    param: RichMethodParameter
  ) (
    implicit
    classTag: ClassTag[A],
    typeTag: ru.TypeTag[A],
    classLoader: ClassLoader
  ): Option[A] = AnnotationOps.instantiate[A](param.annotations)

  def getAnnot[A <: StaticAnnotation] (
    in: List[RichMethodParameter]
  ) (
    implicit
    classTag: ClassTag[A],
    typeTag: ru.TypeTag[A],
    classLoader: ClassLoader
  ): List[(A, RichMethodParameter)] = {
    in
      .map { i => OptionOps.execAndLift(getAnnot[A](i), { ii: A => (ii, i) }) } // wir wollen Some(injectLength, FieldDescr)
      .filter { _.nonEmpty }
      .map { _.get }
  }

  /**
    * Returns true, if the given field descr has the wanted annot
    */
  def hasAnnot[A <: StaticAnnotation] (
    param: RichMethodParameter
  ) (
    implicit
    typeTag: ru.TypeTag[A]
  ): Boolean = AnnotationOps.has[A](param.annotations)

  /**
    * Reflect apply param (symbol, index, default value)
    */
  def getMethodParameter (
    methodSymbol: ru.MethodSymbol
  ) (
    implicit
    classLoader: ClassLoader = getClass.getClassLoader
  ): List[RichMethodParameter] = {
    def getModule(in: ru.Symbol): Option[ru.ModuleSymbol] = if (in.isModule) Some(in.asModule) else None

    val compagnionModuleSymbolOpt = getModule(methodSymbol.owner)
      .orElse(getModule(methodSymbol.owner.companion))
      .orElse(getModule(methodSymbol.owner.companion.companion))

    compagnionModuleSymbolOpt match {
      case Some(t) => reflectMethodParameter_wDefaults(compagnionModuleSymbol = t.asModule, richRuntimeMirror = RichRuntimeMirror(), methodSymbol = methodSymbol)
      case None => reflectMethodParameter_woDefaults(methodSymbol = methodSymbol)
    }
  }

  /**
    * Reflect apply param (symbol, index, default value = None)
    */
  private def reflectMethodParameter_woDefaults (
    methodSymbol: ru.MethodSymbol
  ): List[RichMethodParameter] = {
    def reflectParam(pair: (ru.Symbol, Int)): RichMethodParameter = RichMethodParameter(index = pair._2, symbol = pair._1, defaultValue = None)

    methodSymbol
      .paramLists   // parameter-Symbole besorgen
      .flatten      // eine Ebene draus machen
      .zipWithIndex // Index (Parameter-Position) erstellen
      .map(reflectParam)
  }

  /**
    * Reflect apply param (symbol, index, default value = Some(...))
    */
  private def reflectMethodParameter_wDefaults (
    compagnionModuleSymbol: ru.ModuleSymbol,
    richRuntimeMirror: RichRuntimeMirror,
    methodSymbol: ru.MethodSymbol
  ): List[RichMethodParameter] = {
    val compagnionModuleMirror = richRuntimeMirror.runtimeMirror.reflectModule(compagnionModuleSymbol)
    val compagnionInstance = compagnionModuleMirror.instance
    val compagnionInstanceMirror = richRuntimeMirror.runtimeMirror.reflect(compagnionInstance)
    val compagnionTypeSignature = compagnionInstanceMirror.symbol.typeSignature

    val methodName = if (methodSymbol.isConstructor) "$lessinit$greater" else methodSymbol.name

    def getDefaultMMethod(idx: Int): ru.Symbol = compagnionTypeSignature.member(ru.TermName(s"$methodName$$default$$${idx + 1}"))
    def callMethod(methodSymbol: ru.MethodSymbol): Any = compagnionInstanceMirror.reflectMethod(methodSymbol)()
    def applyMethod(methodSymbol: ru.Symbol): Option[Any] = {
      if (methodSymbol.isMethod && (methodSymbol != ru.NoSymbol))
        Some(callMethod(methodSymbol.asMethod))
      else
        None
    }
    def reflectParamFromSymbol(paramSymbol: ru.Symbol, idx: Int, methodSymbol: ru.Symbol): RichMethodParameter = RichMethodParameter(index = idx, symbol = paramSymbol, defaultValue = applyMethod(methodSymbol))
    def reflectParam(pair: (ru.Symbol, Int)): RichMethodParameter = reflectParamFromSymbol(pair._1, pair._2, getDefaultMMethod(pair._2))

    val result = methodSymbol
      .paramLists   // parameter-Symbole besorgen
      .flatten      // eine Ebene draus machen
      .zipWithIndex // Index (Parameter-Position) erstellen
      .map(reflectParam)

    result
  }
}

object RichMethodParameterOpsIncludes
  extends RichMethodParameterOpsIncludes

trait RichMethodParameterOpsIncludes {
  implicit class RichMethodParameterOpsIncludes_from_singleParam (
    in: RichMethodParameter
  ) {
    def getAnnot[A <: StaticAnnotation] (
      implicit
      classTag: ClassTag[A],
      typeTag: ru.TypeTag[A],
      classLoader: ClassLoader = ClassOps.defaultClassLoader
    ): Option[A] = RichMethodParameterOps.getAnnot[A](in)

    def hasAnnot[A <: StaticAnnotation] (
      implicit
      typeTag: ru.TypeTag[A]
    ): Boolean = RichMethodParameterOps.hasAnnot[A](in)
  }

  implicit class RichMethodParameterOpsIncludes_from_paramList (
    in: List[RichMethodParameter]
  ) {
    def getAnnot[A <: StaticAnnotation] (
      in: List[RichMethodParameter]
    ) (
      implicit
      classTag: ClassTag[A],
      typeTag: ru.TypeTag[A],
      classLoader: ClassLoader = ClassOps.defaultClassLoader
    ): List[(A, RichMethodParameter)] = RichMethodParameterOps.getAnnot[A](in)
  }
}
