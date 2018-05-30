package de.pfke.grind.core.refl

import scala.reflect.runtime.{universe => ru}
import scala.util.Try

trait RichInstanceMirrorIncludes {
  implicit class RichInstanceMirrorFromAny(
    data: Any
  ) {
    // fields
    private lazy val _richInstanceMirror = RichInstanceMirror(data)

    def asTpe: ru.Type = _richInstanceMirror.asTpe

    def toScalaType: ru.Type = GenericOps.toScalaType(asTpe)
  }
}

object RichInstanceMirror {
  /**
    * Create instance.
    */
  def apply (
    className: String
  ) (
    implicit
    classLoader: ClassLoader = getClass.getClassLoader
  ): RichInstanceMirror = apply(richRuntimeMirror = RichRuntimeMirror(), className = className)

  /**
    * Create instance.
    */
  def apply(
    clazz: Class[_]
  ): RichInstanceMirror = apply(className = clazz.getCanonicalName)(clazz.getClassLoader)

  /**
    * Create instance.
    */
  def apply(
    classSymbol: ru.ClassSymbol
  ): RichInstanceMirror = apply(className = classSymbol.fullName)

  /**
    * Create instance.
    */
  def apply(
    value: Any
  ): RichInstanceMirror = apply(RichRuntimeMirror(value.getClass), value)

  /**
    * Create instance.
    */
  def apply(
    richRuntimeMirror: RichRuntimeMirror,
    className: String
  ): RichInstanceMirror = apply(richRuntimeMirror = richRuntimeMirror, instanceMirror = richRuntimeMirror.getInstanceMirror(className))


  /**
    * Create instance.
    */
  def apply(
    richRuntimeMirror: RichRuntimeMirror,
    value: Any
  ): RichInstanceMirror = apply(richRuntimeMirror = richRuntimeMirror, instanceMirror = richRuntimeMirror.getInstanceMirror(value))

  /**
    * Create instance.
    */
  def apply(
    richRuntimeMirror: RichRuntimeMirror,
    instanceMirror: ru.InstanceMirror
  ): RichInstanceMirror = new RichInstanceMirror(richRuntimeMirror = richRuntimeMirror, instanceMirror = instanceMirror)
}

class RichInstanceMirror (
  val richRuntimeMirror: RichRuntimeMirror,
  val instanceMirror: ru.InstanceMirror
) {
  // fields
  private lazy val _applyMethodRefls = RichMethod(instanceMirror.symbol, RichMethod.TERMNAME_APPLY)

  /**
    * Getter
    */
  def applyMethodRefls: List[RichMethod] = _applyMethodRefls

  def asType: ru.TypeSymbol = symbol.asType
  def asTpe: ru.Type = asType.toType
  def fullname: String = instanceMirror.symbol.fullName
  def symbol: ru.ClassSymbol = instanceMirror.symbol
  def typeSignature: ru.Type = instanceMirror.symbol.typeSignature

  /**
    * Call the method
    */
  def call(
    methodName: String,
    args: Any*
  ): Option[Any] = {
    getMethodMirror("apply") match {
      case Some(t) => Try(t.apply(args:_*)).toOption
      case None => throw new IllegalArgumentException("no apply method found")
    }
  }

  /**
    * Return method mirror for the given instance and the given method name.
    */
  def getMethodMirror(
    methodName: String
  ): Option[ru.MethodMirror] = getMethodMirror(methodSymbol = getMethodSymbol(methodName))

  /**
    * Return method mirror for the given instance and the given method name.
    */
  def getMethodMirror(
    methodSymbol: Option[ru.MethodSymbol]
  ): Option[ru.MethodMirror] = {
    methodSymbol match {
      case Some(x) => Some(getMethodMirror(x))
      case None => None
    }
  }

  /**
    * Return method mirror for the given instance and the given method name.
    */
  def getMethodMirror(
    methodSymbol: ru.MethodSymbol
  ): ru.MethodMirror = instanceMirror.reflectMethod(methodSymbol)

  /**
    * Return method mirror for the given instance and the given method name.
    */
  def getMethodSymbol(
    methodName: String
  ): Option[ru.MethodSymbol] = {
    val typeSignature = instanceMirror.symbol.typeSignature
    val methodDef = typeSignature.member(ru.TermName(methodName))

    if (methodDef == ru.NoSymbol) None else Some(methodDef.asMethod)
  }
}
