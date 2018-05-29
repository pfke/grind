package de.pfke.grind.core.data.byTypes.primitives

object IntegerOps {
  def toBool(in: Int): Boolean = if(in == 0) false else true
  def toBool(in: Short): Boolean = toBool(in.toInt)
}

object IntegerOpsIncludes
  extends IntegerOpsIncludes

trait IntegerOpsIncludes {
  implicit def toBool (in: Int): Boolean = IntegerOps.toBool(in = in)
  implicit def toBool (in: Short): Boolean = IntegerOps.toBool(in = in)
}
