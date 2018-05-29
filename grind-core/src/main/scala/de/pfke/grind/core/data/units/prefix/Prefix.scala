package de.pfke.grind.core.data.units.prefix

object Prefix {
  case object No extends Prefix(base = 2, exp = 0, symbol = "", name = "")
}

abstract class Prefix (
  base: Double,
  exp: Double,
  symbol: String,
  name: String
) {
  override def toString: String = symbol
  def toValue: Double = math.pow(base, exp)
}
