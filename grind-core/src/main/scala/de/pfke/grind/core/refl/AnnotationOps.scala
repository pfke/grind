package de.pfke.grind.core.refl

import scala.annotation.StaticAnnotation
import scala.reflect.ClassTag
import scala.reflect.runtime.{universe => ru}

object AnnotationOps {
  /**
    * Returns true if the list contains the given annotation
    *
    * @param annots list of annotations
    * @param typeTag type tp find
    * @tparam A type to return
    * @return option of an instantiated annotation
    */
  def contains[A] (
    annots: List[ru.Annotation]
  ) (
    implicit
    typeTag: ru.TypeTag[A]
  ): Boolean = {
    find[A](annots) match {
      case Some(_) => true
      case None    => false
    }
  }

  /**
    * Find annotation of the given type within the list.
    *
    * @param annots list of annotations
    * @param typeTag type tp find
    * @tparam A type to return
    * @return option of the annotation
    */
  def find[A] (
    annots: List[ru.Annotation]
  ) (
    implicit
    typeTag: ru.TypeTag[A]
  ): Option[ru.Annotation] = annots.find { a => a.tree.tpe =:= typeTag.tpe }

  /**
    * Get the class file annotation of the given class type.
    * An exception is thrown if the given type is not annotated.
    *
    * @param tpe type information of the class
    * @param annotTypeTag type information of the annotation
    * @tparam A is the type of the annotation to find
    * @return instance of the annotation
    */
  def get[A <: StaticAnnotation] (
    tpe: ru.Type
  ) (
    implicit
    annotClassTag: ClassTag[A],
    annotTypeTag: ru.TypeTag[A],
    classLoader: ClassLoader
  ): A = {
    find[A](tpe.typeSymbol.asClass.annotations) match {
      case Some(x) => instantiate[A](x)
      case None    => throw new IllegalArgumentException(s"$tpe has no annotation of type ${annotTypeTag.tpe}")
    }
  }

  /**
    * Get the class file annotation of the given class type.
    * An exception is thrown if the given type is not annotated.
    *
    * @param annotTypeTag type information of the annotation
    * @param classTypeTag type information of the class
    * @tparam A is the type of the annotation to find
    * @tparam V is the type of the annotated class
    * @return instance of the annotation
    */
  def get[A <: StaticAnnotation, V] () (
    implicit
    annotClassTag: ClassTag[A],
    annotTypeTag: ru.TypeTag[A],
    classTypeTag: ru.TypeTag[V],
    classLoader: ClassLoader
  ): A = get[A](classTypeTag.tpe)

  /**
    * Get the class file annotation of the given class type.
    * An exception is thrown if the given type is not annotated.
    */
  def get[A <: StaticAnnotation] (
    in: List[ru.Annotation]
  ) (
    implicit
    classLoader: ClassLoader,
    classTag: ClassTag[A],
    typeTag: ru.TypeTag[A],
  ): Option[A] = instantiate[A](annots = in)

  /**
    * Returns true, if the given field descr has the wanted annot
    */
  def has[A <: StaticAnnotation](
    in: List[ru.Annotation]
  ) (
    implicit
    typeTag: ru.TypeTag[A]
  ): Boolean = contains[A](in)

  /**
    * Find annotation of the given type within the list and instantiate.
    *
    * @param annots list of annotations
    * @param typeTag type tp find
    * @tparam A type to return
    * @return option of an instantiated annotation
    */
  def instantiate[A] (
    annots: List[ru.Annotation]
  ) (
    implicit
    classLoader: ClassLoader,
    classTag: ClassTag[A],
    typeTag: ru.TypeTag[A]
  ): Option[A] = {
    find[A](annots) match {
      case Some(x) => Some(instantiate[A](x))
      case None    => None
    }
  }

  /**
    * Instantiate the given annotation
    *
    * @param annot the AnnotationInfo
    * @param annotTypeTag annotation type
    * @tparam A generic type
    * @return instantiated
    */
  def instantiate[A] (
    annot: ru.Annotation
  ) (
    implicit
    classLoader: ClassLoader,
    annotClassTag: ClassTag[A],
    annotTypeTag: ru.TypeTag[A]
  ): A = { // import scala.reflect.runtime.universe._: sorgt dafür, dass die haessliche 'abstract type pattern reflect.runtime.universe.AssignOrNamedArg is unchecked since it is eliminated by erasure' wegkommt
    import scala.reflect.runtime.universe._

    require(annot.tree.tpe =:= annotTypeTag.tpe, "passed argument does not match generic annotation type")

    val annotType = annotTypeTag.tpe                                                                // get the expected annotation type to match
    val args = annot
      .tree.children.tail                                                                 // retrieve the args. These are returned as a list of Tree.
      .collect{                                                                           // convert list of Tree to list of argument values
      case ru.Literal(ru.Constant(m)) => m
    }

    //if (args.size != annot.tree.children.tail.size) {
    //  val r1 = args
    //  val r2 = annot.tree.children
    //
    //
    //  println()
    //}

    // TODO: merge w/ default args
    val w1 = RichRuntimeMirror()(classLoader)
    val w2 = RichInstanceMirror(annotType.typeSymbol.asClass)
    val w3 = w2.applyMethodRefls
    val w4 = new RichCaseClass(annotType.typeSymbol.asClass)
    val w5 = w4.instantiate[A](args:_*)
    w5


    //require(args.size == annot.tree.children.tail.size, s"wanted me to instantiate the annotation '${annot.tree.tpe}', but ALL arguments needs to be constant values (no enums either)")
    //
    //val runtimeMirror = ru.runtimeMirror(classLoader)                                     // get runtimeMirror
    //val classSymbol = annotType.typeSymbol.asClass                                        // get ClassSymbol for the annotation class
    //val classMirror = runtimeMirror.reflectClass(classSymbol)                             // get classMirror for the class
    //val constructorMethodSymbol = annotType.decl(ru.termNames.CONSTRUCTOR).asMethod       // get MethodSymbol for the constructor method
    //val constructorMethodMirror = classMirror.reflectConstructor(constructorMethodSymbol) // get constructorMethodMirror for the method
    //val instance = constructorMethodMirror(args: _*).asInstanceOf[A]                      // use the constructor to instance the annotation class.  Pass in the arguments.
    //
    //instance
  }

  /**
    * Return true if one of the given annotations is derived from the wanted type.
    *
    * @param annots list of annotations
    * @param typeTag type info of the wanted super class
    * @tparam A generic info
    * @return true if on of the annotations has A as its super class
    */
  def isDerivedFrom[A] (
    annots: List[ru.Annotation]
  ) (
    implicit
    typeTag: ru.TypeTag[A]
  ): Boolean = {
    annots
      .flatMap(_.tree.tpe.baseClasses) // get all base classes of this annotation
      .distinct                        // remove duplicates
      .exists(GenericOps.equals[A])    // exists searched type?
  }
}

object AnnotationOpsIncludes
  extends AnnotationOpsIncludes

trait AnnotationOpsIncludes {
  implicit class AnnotationOpsIncludes_from_annot (
    in: ru.Annotation
  ) {
    def instantiateAnnot[A] (
      implicit
      classLoader: ClassLoader,
      classTag: ClassTag[A],
      typeTag: ru.TypeTag[A]
    ): A = AnnotationOps.instantiate[A](in)
  }

  implicit class AnnotationOpsIncludes_from_annotList (
    in: List[ru.Annotation]
  ) {
    def containsAnnot[A] (
      implicit
      typeTag: ru.TypeTag[A]
    ): Boolean = AnnotationOps.contains[A](in)

    def findAnnot[A] (
      implicit
      typeTag: ru.TypeTag[A]
    ): Option[ru.Annotation] = AnnotationOps.find[A](in)

    def getAnnot[A <: StaticAnnotation] (
      implicit
      classLoader: ClassLoader,
      classTag: ClassTag[A],
      typeTag: ru.TypeTag[A],
    ): Option[A] = AnnotationOps.get[A](in)

    def hasAnnot[A <: StaticAnnotation] (
      implicit
      typeTag: ru.TypeTag[A]
    ): Boolean = AnnotationOps.contains[A](in)

    def hasAnnotDerivedFrom[A] (
      implicit
      typeTag: ru.TypeTag[A]
    ): Boolean = AnnotationOps.isDerivedFrom[A](in)

    def instantiateAnnot[A]  (
      implicit
      classLoader: ClassLoader,
      classTag: ClassTag[A],
      typeTag: ru.TypeTag[A]
    ): Option[A] = AnnotationOps.instantiate[A](in)
  }
}