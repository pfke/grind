package de.pfke.grind.core.refl

import scala.reflect.ClassTag
import scala.reflect.runtime.{universe => ru}
import scala.util.Try

object RichCaseClass {
  /**
    * Create instance
    */
  def apply (
    clazz: Class[_]
  ): RichCaseClass = apply(clazz.getCanonicalName)(clazz.getClassLoader)

  /**
    * Create instance
    */
  def apply[A] () (
    implicit
    classTag: ClassTag[A]
  ): RichCaseClass = apply(classTag.runtimeClass)

  /**
    * Create instance
    */
  def apply (
    className: String
  ) (
    implicit
    classLoader: ClassLoader = getClass.getClassLoader
  ): RichCaseClass = new RichCaseClass(classSymbol = RichRuntimeMirror().getClassSymbol(className = className))
}

class RichCaseClass (
  classSymbol: ru.ClassSymbol
) (
  implicit
  classLoader: ClassLoader = getClass.getClassLoader
) extends RichBaseClass (
  classSymbol = classSymbol
) {
  require(ClassOps.isCaseClass(classSymbol), s"passed class: '${classSymbol.fullName}' is not a case class")

  // fields
  private val _applyMethods = RichMethod(classSymbol.companion, RichMethod.TERMNAME_APPLY)
  private val _compagnionClassName = ClassOps.getCompagnionClassName(classSymbol)
  private val _richInstanceMirror = RichInstanceMirror(classSymbol = classSymbol)

  // requirements
  require(applyMethods.nonEmpty, s"could not find any apply method of case class ${classSymbol.typeSignature}")

  def applyMethods: List[RichMethod] = _applyMethods
  def applyMethodParameters: List[List[RichMethodParameter]] = applyMethods.map(_.parameter)

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
    require(classSymbol.selfType <:< typeTag.tpe, s"given generic is neither a super nor the same class (${classSymbol.selfType} <:< ${typeTag.tpe})")

    findApplyMethod_matching_paramTypes(args:_*) match {
      case Some(t) => t.apply[A](args:_*)
      case None => throw new IllegalArgumentException(s"no apply method found, which matches the passed args: '${args.mkString(", ")}'")
    }
  }

  /**
    * Call an instance method (try to instanciate case clase without any arg)
    */
  def call[A] (
    methodName: String,
    args: Any*
  ) (
    implicit
    classTag: ClassTag[A],
    typeTag: ru.TypeTag[A]
  ): A = {
    val instanceOpt = Try(instantiate[Any]()).toOption
    require(instanceOpt.nonEmpty, s"the case class $className needs apply arguments to instantiate -> could not call method $methodName without instance")

    call[A](
      instanceMirror = RichRuntimeMirror(classTag.runtimeClass).getInstanceMirror(instanceOpt.get),
      methodName = methodName,
      args = args:_*
    )
  }

  /**
    * Erstellt eine Kopie des mitgelieferten Objectes (<code>that</code>) und überschreibt dabei
    * die als <code>args</code> mitgelieferten Arguments/Parameter.
    *
    * @param that ist die zu kopierende Instanz
    * @param args sind die zu ersetzenden Parameter-Werte (also nicht mitzukopieren)
    * @param classTag ist der ClassTag zu dem zu kopierendem Object
    * @param typeTag ist der TypeTag zu dem zu kopierendem Object
    * @tparam A ist der Typ des zu kopierenden Objectes. Muss derselbe Typ sein wie unser <code>classSymbol</code>
    * @return Instanz des neuen Objectes
    */
  def copy[A] (
    that: A,
    args: MethodParameterValue*
  ) (
    implicit
    classTag: ClassTag[A],
    typeTag: ru.TypeTag[A]
  ): A = {
    case class ParamAndValueOpt(param: RichMethodParameter, value: Option[MethodParameterValue])
    case class ParamAndValue(param: RichMethodParameter, value: MethodParameterValue, idx: Int)

    // prüfen, ob der Type der unsrige ist
    require(typeTag.tpe =:= classSymbol.selfType, s"passed generic is of type '${typeTag.tpe}', but has to be of '${classSymbol.selfType}'")

    // prüfen, ob alle mitgegebenen Parameter unterschiedliche Namen haben
    val names = args.map(_.name)
    require(names.distinct.lengthCompare(args.size) == 0, s"There are duplicate names in passed args (${names.mkString(", ")})")

    // finden der Apply-Methode mit den meisten Argumenten (denn wir wollen ja ein komplett kopiertes Object)
    val applyMethod = applyMethods.minBy(_.parameter.size)
    // prüfen, alle übergebenen Parameter auch in der Apply-Methode genannt sind
    require(names.forall { i => applyMethod.parameter.exists { c => c.name == i } }, s"one or more args passed which cannot be found in that apply method: '$applyMethod'")

    def buildMPV(methodName: String) = MethodParameterValue(methodName, call[Any](that, methodName))
    def mergeParamValue(in: RichMethodParameter): ParamAndValueOpt = ParamAndValueOpt(in, args.find(_.name == in.name))
    def enrichWithValues: PartialFunction[(ParamAndValueOpt, Int), ParamAndValue] = {
      case (ParamAndValueOpt(t, Some(v)), i) => ParamAndValue(t, v, i)
      case (ParamAndValueOpt(t, None),    i) => ParamAndValue(t, buildMPV(t.name), i)
    }

    val mergedArgs = applyMethod
      .parameter                  // get parameter from apply method: List[MethodParameter]
      .map(mergeParamValue)       // merge param + param value: List[ParamAndValueOpt]
      .zipWithIndex               // prefix with index: List[(ParamAndValueOpt, Int)]
      .collect(enrichWithValues)  // get parameter values (from old entity, or - in one case - the passed value): List[(ParamAndValue, Int)]

    val applyArgs = mergedArgs
      .sortBy(_.idx)              // sort by param idx
      .map(_.value.value)         // get values only

    instantiate[A](applyArgs:_*)
  }

  /**
    * Return the apply RichMethodRefl which contains the given name
    */
  def findApplyMethod_matching_paramNames (
    paramNames: String*
  ): Option[RichMethod] = findMethod_matching_methodName_and_paramNames(useCompanion = true, RichMethod.TERMNAME_APPLY, paramNames)

  /**
    * Return the apply RichMethodRefl which contains the given name
    */
  def findApplyMethod_matching_paramTypes (
    args: Any*
  ): Option[RichMethod] = findMethod_matching_methodName_and_paramTypes(useCompanion = true, RichMethod.TERMNAME_APPLY, args)
}
