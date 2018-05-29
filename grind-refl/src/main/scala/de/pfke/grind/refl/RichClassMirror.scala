package de.pfke.grind.refl

import scala.reflect.runtime.{universe => ru}

object RichClassMirror {
  /**
    * Create instance.
    */
  def apply (
    className: String
  ): RichClassMirror = apply(richRuntimeMirror = RichRuntimeMirror(), className = className)

  /**
    * Create instance.
    */
  def apply (
    clazz: Class[_]
  ): RichClassMirror = apply(richRuntimeMirror = RichRuntimeMirror(clazz), className = clazz.getCanonicalName)

  /**
    * Create instance.
    */
  def apply (
    classSymbol: ru.ClassSymbol
  ) (
    implicit
    classLoader: ClassLoader = getClass.getClassLoader
  ): RichClassMirror = apply(richRuntimeMirror = RichRuntimeMirror(), classSymbol = classSymbol)

  /**
    * Create instance.
    */
  def apply (
    value: Any
  ): RichClassMirror = apply(RichRuntimeMirror(value.getClass), value.getClass.getCanonicalName)

  /**
    * Create instance.
    */
  def apply (
    richRuntimeMirror: RichRuntimeMirror,
    classSymbol: ru.ClassSymbol
  ): RichClassMirror = new RichClassMirror(richRuntimeMirror = richRuntimeMirror, classMirror = richRuntimeMirror.getClassMirror(classSymbol))

  /**
    * Create instance.
    */
  def apply (
    richRuntimeMirror: RichRuntimeMirror,
    className: String
  ): RichClassMirror = new RichClassMirror(richRuntimeMirror = richRuntimeMirror, classMirror = richRuntimeMirror.getClassMirror(richRuntimeMirror.getClassSymbol(className)))
}

class RichClassMirror (
  val richRuntimeMirror: RichRuntimeMirror,
  val classMirror: ru.ClassMirror
) {
  // fields
  private lazy val _ctorMethodRefls = RichMethod(classMirror.symbol, RichMethod.TERMNAME_CTOR)

  // getter
  def ctorMethodRefls: List[RichMethod] = _ctorMethodRefls
}
