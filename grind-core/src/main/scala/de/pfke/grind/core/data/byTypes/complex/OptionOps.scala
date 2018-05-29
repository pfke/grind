package de.pfke.grind.core.data.byTypes.complex

object OptionOps {
  def execAndLift [A, B] (
    in: Option[A],
    op: (A) => B
  ): Option[B] = {
    in match {
      case Some(x) => lift(op(x))
      case None => None
    }
  }

  def execOrDefault [A, B] (
    in: Option[A],
    op: (A) => B,
    default: B
  ): B = {
    in match {
      case Some(x) => op(x)
      case None => default
    }
  }

  def execOrThrow [A, B] (
    in: Option[A],
    op: (A) => B,
    exp: Exception
  ): B = {
    in match {
      case Some(x) => op(x)
      case None => throw exp
    }
  }

  def execOrNothing [A] (
    in: Option[A],
    op: (A) => Unit
  ): Unit = {
    in match {
      case Some(x) => op(x)
      case None =>
    }
  }

  def lift [A] (in: A): Option[A] = Some(in)

  def orDefault (in: Option[String]): String = orDefault(default = "???")(in = in)
  def orDefault [A] (
    default: A
  ) (
    in: Option[A]
  ): A = in.getOrElse(default)

  def orThrow [A] (
    in: Option[A],
    exp: Exception
  ): A = {
    in match {
      case Some(x) => x
      case None => throw exp
    }
  }
}

object OptionOpsIncludes
  extends OptionOpsIncludes

trait OptionOpsIncludes {
  implicit def lift [A] (in: A): Option[A] = Option(in)

  implicit class OptionOpsImplicits_fromOptGeneric[A] (
    in: Option[A]
  ) {
    def execAndLift [B] (op: (A) => B): Option[B] = OptionOps.execAndLift(in = in, op = op)
    def execOrDefault [B] (op: (A) => B, default: B): B = OptionOps.execOrDefault(in = in, op = op, default = default)
    def execOrThrow [B] (op: (A) => B, exp: Exception): B = OptionOps.execOrThrow(in = in, op = op, exp = exp)
    def execOrNothing (op: (A) => Unit): Unit = OptionOps.execOrNothing(in = in, op = op)

    def orDefault (default: A): A = OptionOps.orDefault(default = default)(in = in)

    def orThrow (exp: Exception): A = OptionOps.orThrow(in = in, exp = exp)
  }

  implicit class OptionOpsImplicits_fromOptString (
    in: Option[String]
  ) {
    def orDefault: String =  OptionOps.orDefault(in = in)
  }
}
