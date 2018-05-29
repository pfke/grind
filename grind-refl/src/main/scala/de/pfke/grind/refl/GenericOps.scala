package de.pfke.grind.refl

import java.util.UUID

import scala.reflect.ClassTag
import scala.reflect.runtime.{universe => ru}

object GenericOps {
  case class TypeInfo[A](classTag: ClassTag[A], typeTag: ru.TypeTag[A])

  /**
    * Return default values for the given types
    * @param tpe type to get values for
    * @return
    */
  def defaultValue(
    tpe: ru.Type
  ): Any = {
    tpe match {
      case t if PrimitiveOps.isPrimitive(t) => PrimitiveOps.defaultValue(t)

      case t if t =:= ru.typeOf[String] => ""
      case t if t <:< ru.typeOf[Option[_]] => None
      case t if t =:= ru.typeOf[UUID] => UUID.randomUUID()

      case _ => throw new IllegalArgumentException(s"could not get default arg for type $tpe")
    }
  }

  /**
    * Returns true if the symbol is the same class like the generic type parameter.
    *
    * @param symbol to check
    * @param typeTag type info
    * @tparam A type to match
    * @return true is symbol is the same class like A
    */
  def equals[A] (
    symbol: ru.Symbol
  ) (
    implicit
    typeTag: ru.TypeTag[A]
  ): Boolean = symbol.asType.toType =:= typeTag.tpe

  /**
    * Return type info for given type
    *
    * @param classTag is the classtag info
    * @param typeTag is the typetag info
    * @tparam A is the object to generate type info for
    * @return a tuple of classtag and typetag
    */
  def getTypeInfo[A] (
    implicit
    classTag: ClassTag[A],
    typeTag: ru.TypeTag[A]
  ): TypeInfo[A] = TypeInfo(classTag, typeTag)

  /**
    * Returns true, if the type is a trait or abstract (i.e. an abstract class, an abstract method, value or type member)?
    */
  def isAbstract (in: ru.Type): Boolean = in.typeSymbol.isAbstract
  def isAbstract [A] (
    implicit
    typeTag: ru.TypeTag[A]
  ): Boolean = isAbstract(typeTag.tpe)
  def isAbstract (in: Any): Boolean = isAbstract(typeOf(in))

  /**
    * Return true if the passed type is complex
    */
  def isComplex (in: ru.Type): Boolean = !isPrimitive(in) && !isString(in)
  def isComplex[A] (
    implicit
    typeTag: ru.TypeTag[A]
  ): Boolean = isComplex(typeTag.tpe)
  def isComplex (in: Any): Boolean = isComplex(typeOf(in))

  /**
    * Returns true if the given squeezle is an enum.
    *
    * @return true or false
    */
  def isEnum (in: ru.Type): Boolean = {
    in
      .baseClasses
      .exists(_.fullName == classOf[Enumeration#Value].getCanonicalName)
  }

  /**
    * Return true if the passed type is a list
    */
  def isList (in: ru.Type): Boolean = in <:< ru.typeOf[List[_]]
  def isList [A] (
    implicit
    typeTag: ru.TypeTag[A]
  ): Boolean = isList(typeTag.tpe)
  def isList (in: Any): Boolean = isList(typeOf(in))

  /**
    * Return true if the passed type is a string
    */
  def isPrimitive (in: ru.Type): Boolean = PrimitiveOps.isPrimitive(in)
  def isPrimitive [A] (
    implicit
    typeTag: ru.TypeTag[A]
  ): Boolean = PrimitiveOps.isPrimitive(typeTag.tpe)
  def isPrimitive (in: Any): Boolean = isPrimitive(typeOf(in))

  /**
    * Return true if the passed type is a string
    */
  def isString (in: ru.Type): Boolean = toScalaType(in) =:= ru.typeOf[String]
  def isString[A] (
    implicit
    typeTag: ru.TypeTag[A]
  ): Boolean = isString(typeTag.tpe)
  def isString (in: Any): Boolean = isString(typeOf(in))

  /**
    * Return the type of the in value
    */
  def typeOf (
    in: Any
  ): ru.Type = typeOf(in.getClass)

  /**
    * Return the type of the in class
    */
  def typeOf (
    in: Class[_]
  ): ru.Type = {
    val clazz = in
    val classLoader = clazz.getClassLoader
    val classSymbol = ru
      .runtimeMirror(classLoader)
      .classSymbol(clazz)

    classSymbol
      .selfType
  }

  /**
    * Return type signature of the given value
    */
  def typeOf[A] () (
    implicit
    classTag: ClassTag[A],
    typeTag: ru.TypeTag[A]
  ): ru.Type = typeTag.tpe

  /**
    * Get a type and unify to common type.
    * Used to be able to compare e.g. java.lang.Integer == scala.Int
    */
  def toScalaType (in: ru.Type): ru.Type = {
    in match {
      case t if PrimitiveOps.isPrimitive(t) => PrimitiveOps.toScalaType(in)
      case t if t =:= ru.typeOf[String] => ru.typeOf[String]

      case t => t
    }
  }

  /**
    * Get a type and unify to common type.
    * Used to be able to compare e.g. java.lang.Integer == scala.Int
    */
  def toScalaType (in: Any): ru.Type = toScalaType(typeOf(in))

  /**
    * Get a type and unify to common type.
    * Used to be able to compare e.g. java.lang.Integer == scala.Int
    */
  def toScalaClass (in: Any): Class[_] = toScalaClass(in.getClass)

  /**
    * Get a type and unify to common type.
    * Used to be able to compare e.g. java.lang.Integer == scala.Int
    */
  def toScalaClass (in: Class[_]): Class[_] = {
    in match {
      case t if PrimitiveOps.isPrimitive(t) => PrimitiveOps.toScalaClass(in)

      case t => t
    }
  }
}

object GenericOpsIncludes
  extends GenericOpsIncludes

trait GenericOpsIncludes {
  implicit class GenericOpsImplicits_from_object (
    in: Any
  ) {
    /**
      * return the type of the input
      */
    def getType: ru.Type = GenericOps.typeOf(in)
  }

  implicit class GenericOpsImplicits_from_ruType (
    in: ru.Type
  ) {
    /**
      * Returns true, if the type is a trait or abstract
      */
    def isAbstract: Boolean = GenericOps.isAbstract(in)

    /**
      * Return true if the passed type is complex
      */
    def isComplex: Boolean = GenericOps.isComplex(in)

    /**
      * Returns true if the given squeezle is an enum.
      *
      * @return true or false
      */
    def isEnum: Boolean = GenericOps.isEnum(in)

    /**
      * Return true if the passed type is a list
      */
    def isList: Boolean = GenericOps.isList(in)

    /**
      * Return true if the passed type is a string
      */
    def isPrimitive: Boolean = GenericOps.isPrimitive(in)

    /**
      * Return true if the passed type is a string
      */
    def isString: Boolean = GenericOps.isString(in)

    /**
      * Return all sub types for this complex class
      */
//TODO    def subFields: Boolean = GenericOps.subFields(in)

    /**
      * Get a type and unify to common type.
      * Used to be able to compare e.g. java.lang.Integer == scala.Int
      */
    def toScalaType: ru.Type = GenericOps.toScalaType(in)
  }

  implicit class GenericOpsImplicits_from_ruTypeTag[A] (
    in: ru.TypeTag[A]
  ) extends GenericOpsImplicits_from_ruType(in.tpe)
}