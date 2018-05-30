package de.pfke.grind.core.refl

import java.nio.file.{Files, Path, Paths}

import scala.annotation.StaticAnnotation
import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.reflect.ClassTag
import scala.reflect.runtime.{universe => ru}
import scala.util.Try

case class ClassInfo[A](classSymbol: ru.ClassSymbol, tpe: ru.Type, clazz: Class[_], annotations: List[ru.Annotation])

object ClassOps {
  // fields
  private val _cache = new mutable.HashMap[String, ClassInfo[_]]()
  private val _runtimeMirrorCache = new mutable.HashMap[ClassLoader, ru.RuntimeMirror]()

  def create (
    forName: String
  )(
    implicit
    classLoader: ClassLoader
  ): ClassInfo[_] = _cache.getOrElseUpdate(forName, reflectInfo(forName))

  def defaultClassLoader: ClassLoader = Thread.currentThread().getContextClassLoader

  /**
    * Method to find all classes in the given classloader and within the given package
    */
  def findClasses(
    packageName: String = ""
  ) (
    implicit
    classLoader: ClassLoader = defaultClassLoader
  ): List[ClassInfo[_]] = {
    val resources = classLoader.getResources(packageName.replace('.', '/'))

    val files = resources.asScala.map { i => Paths.get(i.getFile) }
    val findClassesFn = findClasses_byPath(packageName = packageName)(_,_)

    def callFn (in: Path): List[ClassInfo[_]] = findClassesFn(in, Nil)

    val classes = files
      .toList
      .par
      .map(callFn)

    classes
      .flatten
      .toList
  }
  /**
    * Method to find all classes in the given classloader and within the given package
    */
  def findClasses_byAnnotation[A <: StaticAnnotation](
    packageName: String = ""
  )(
    implicit
    classLoader: ClassLoader = defaultClassLoader,
    classTag: ClassTag[A],
    typeTag: ru.TypeTag[A]
  ): List[ClassInfo[_]] = {
    def containsAnnot(in: ClassInfo[_]): Boolean = AnnotationOps.contains[A](in.annotations)

    findClasses(packageName = packageName)
      .par
      .filter(containsAnnot)
      .toList
  }

  /**
    * Method to find all classes in the given classloader and within the given package
    */
  def findClasses_byInheritance[A] (
    packageName: String = ""
  )(
    implicit
    classLoader: ClassLoader = defaultClassLoader,
    classTag: ClassTag[A],
    typeTag: ru.TypeTag[A]
  ): List[ClassInfo[_]] = findClasses_byInheritance(tpe = typeTag.tpe, packageName = packageName)

  /**
    * Method to find all classes in the given classloader and within the given package
    */
  def findClasses_byInheritance (
    tpe: ru.Type,
    packageName: String
  )(
    implicit
    classLoader: ClassLoader
  ): List[ClassInfo[_]] = {
    def isDerived(that: ClassInfo[_]): Boolean = {
      Try(that.tpe <:< tpe)
        .toOption match {
        case Some(x) => x
        case None => false
      }
    }

    findClasses(packageName = packageName)
      .filter(isDerived)
      .filterNot(_.tpe =:= tpe) // den angefragten Typ rausschmeissen
  }

  /**
    * Method to find all classes in the given classloader and within the given package
    */
  def findClasses_byInheritance_byAnnotation[A, B <: StaticAnnotation](
    packageName: String = ""
  )(
    implicit
    classLoader: ClassLoader = defaultClassLoader,
    classTag: ClassTag[A],
    typeTag: ru.TypeTag[A],
    annotClassTag: ClassTag[B],
    annotTypeTag: ru.TypeTag[B]
  ): List[ClassInfo[_]] = {
    findClasses_byInheritance(tpe = typeTag.tpe, packageName = packageName)
      .filter { i => AnnotationOps.contains[B](i.annotations) }
  }

  /**
    * Method to find all classes within the given dir
    */
  def findClasses_byPath (
    packageName: String
  ) (
    file: Path,
    inList: List[ClassInfo[_]] = Nil
  ) (
    implicit
    classLoader: ClassLoader = defaultClassLoader
  ): List[ClassInfo[_]] = {
    if (!Files.isDirectory(file)) return Nil
    if (!Files.exists(file)) return Nil

    def buildClassName(in: Path) = buildName(in.toString.dropRight(6))
    def buildDirName(in: Path) = buildName(in.toString + ".")
    def buildName(in: String) = s"$packageName$in"

    Files
      .list(file)
      .iterator().asScala.toList
      .par
      .flatMap {
        case t if Files.isDirectory(t) => findClasses_byPath(buildDirName(t))(t, inList = inList)
        case t if t.toString.contains("$$anonfun") => inList // führt zu einen Null-ptr mit match-Error bei classSymbol.selfType, deswegen annonfun rausfiltern
        case t if t.toString.endsWith(".class") && ClassOps.hasInfo(forName = buildClassName(t)) => inList.::(ClassOps.create(forName = buildClassName(t)))
        case _ => inList
      }
      .toList
  }

  /**
    * Test if the given type is a case class.
    */
  def isClass[T] (
    implicit
    classTag: ClassTag[T]
  ): Boolean = isClass(classTag.runtimeClass)

  /**
    * Test if the given type is a case class.
    */
  def isClass (
    clazz: Class[_]
  ): Boolean = isClass(RichRuntimeMirror(clazz).getClassSymbol(clazz.getName))

  /**
    * Test if the given type is a case class.
    */
  def isClass (
    className: String
  )(
    implicit
    classLoader: ClassLoader = getClass.getClassLoader
  ): Boolean = isClass(RichRuntimeMirror().getClassSymbol(className))

  /**
    * Test if the given type is a case class.
    */
  def isClass (
    clazzSymbol: ru.ClassSymbol
  ): Boolean = !clazzSymbol.isCaseClass

  /**
    * Test if the given type is a case class.
    */
  def isCaseClass[T] (
    implicit
    classTag: ClassTag[T]
  ): Boolean = isCaseClass(classTag.runtimeClass)

  /**
    * Test if the given type is a case class.
    */
  def isCaseClass (
    clazz: Class[_]
  ): Boolean = isCaseClass(RichRuntimeMirror(clazz).getClassSymbol(clazz.getName))

  /**
    * Test if the given type is a case class.
    */
  def isCaseClass(
    className: String
  )(
    implicit
    classLoader: ClassLoader = getClass.getClassLoader
  ): Boolean = isCaseClass(RichRuntimeMirror().getClassSymbol(className))

  /**
    * Test if the given type is a case class.
    */
  def isCaseClass(
    clazzSymbol: ru.ClassSymbol
  ): Boolean = clazzSymbol.isCaseClass

  /**
    * Return classymbols compagnion class name (the objects name)
    */
  def getClassName(
    classSymbol: ru.ClassSymbol
  ): String = getClassName(classSymbol.fullName)

  /**
    * Return classymbols compagnion class name (the objects name)
    */
  def getClassName(
    name: String
  ): String = if (name.endsWith("$")) name.substring(0, name.length - 1) else name

  /**
    * Return classymbols compagnion class name (the objects name)
    */
  def getCompagnionClassName(
    classSymbol: ru.ClassSymbol
  ): String = getCompagnionClassName(classSymbol.fullName)

  /**
    * Return classymbols compagnion class name (the objects name)
    */
  def getCompagnionClassName(
    name: String
  ): String = name + (if (name.endsWith("$")) "" else "$")

  def hasInfo (
    forName: String
  ) (
    implicit
    classLoader: ClassLoader
  ): Boolean = {
    Try(create(forName))
      .toOption
      .nonEmpty
  }

  def typeSignature (
    classSymbol: ru.ClassSymbol
  ): ru.Type = classSymbol.selfType

  private def reflectInfo(
    forName: String
  )(
    implicit
    classLoader: ClassLoader
  ): ClassInfo[_] = {
    val rm = _runtimeMirrorCache.getOrElseUpdate(classLoader, ru.runtimeMirror(classLoader))
    val clazz = try { // turning a FatalException into a NonFatal one, can be catched by Try()
      Class.forName(forName)
    } catch {
      case e: NoClassDefFoundError => throw new IllegalArgumentException(e)
    }
    val classSymbol = rm.classSymbol(clazz)

    ClassInfo(
      classSymbol = classSymbol,
      tpe = classSymbol.selfType,
      clazz = clazz,
      annotations = classSymbol.selfType.typeSymbol.annotations
    )
  }
}
