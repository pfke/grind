package de.pfke.grind.refl

import scala.reflect.ClassTag
import scala.reflect.runtime.{universe => ru}

object RichRuntimeMirror {
  /**
    * Create instance with implicit .
    *
    * @param classLoader is the loader to obtain the runtime mirror from
    * @return
    */
  def apply () (
    implicit
    classLoader: ClassLoader = getClass.getClassLoader
  ): RichRuntimeMirror = new RichRuntimeMirror(ru.runtimeMirror(classLoader))

  /**
    * Create instance from class type.
    */
  def apply (
    clazz: Class[_]
  ): RichRuntimeMirror = apply()(clazz.getClassLoader)
}

class RichRuntimeMirror(
  val runtimeMirror: ru.Mirror
) {
  /**
    * Getter
    */
  def classLoader: ClassLoader = runtimeMirror.classLoader

  /**
    * Reflect a class mirror
    */
  def getClassMirror (
    classSymbol: ru.ClassSymbol
  ): ru.ClassMirror = runtimeMirror.reflectClass(classSymbol)

  /**
    * Reflect the class symbol from the full class name.
    *
    * @param className is the name of the class
    * @return reflected class symbol
    */
  def getClassSymbol (
    className: String
  ): ru.ClassSymbol = runtimeMirror.staticClass(className)

  /**
    * Reflect the class symbol from clazz.
    */
  def getClassSymbol (
    clazz: Class[_]
  ): ru.ClassSymbol = getClassSymbol(clazz.getCanonicalName)

  /**
    * Reflect the class symbol from clazz.
    */
  def getClassSymbol[A] () (
    implicit
    classTag: ClassTag[A]
  ): ru.ClassSymbol = getClassSymbol(classTag.runtimeClass)

  /**
    * Return the instance mirror
    */
  def getInstanceMirror (
    clazz: Class[_]
  ): ru.InstanceMirror = getInstanceMirror(clazz.getCanonicalName)

  /**
    * Return the instance mirror
    */
  def getInstanceMirror[A] (
    obj: A
  ) (
    implicit
    classTag: ClassTag[A],
    typeTag: ru.TypeTag[A]
  ): ru.InstanceMirror = runtimeMirror.reflect(obj)

  /**
    * Return the instance mirror
    */
  def getInstanceMirror (
    className: String
  ): ru.InstanceMirror = getInstanceMirror(runtimeMirror.staticClass(className))

  /**
    * Return the instance mirror
    */
  def getInstanceMirror (
    classSymbol: ru.ClassSymbol
  ): ru.InstanceMirror = {
    require(classSymbol.isCaseClass, s"given class ${classSymbol.fullName} is not a case class, so you cant create an instancemirror")

    val compagnion = classSymbol.companion.asModule
    val module = runtimeMirror.reflectModule(compagnion)
    val moduleInstance = module.instance

    runtimeMirror.reflect(moduleInstance)
  }
}
