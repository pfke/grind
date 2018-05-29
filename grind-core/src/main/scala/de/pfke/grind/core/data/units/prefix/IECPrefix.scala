package de.pfke.grind.core.data.units.prefix

object IECPrefix {
  case object Kibi extends IECPrefix(1, "Ki", "kibi")
  case object Mebi extends IECPrefix(2, "Mi", "mebi")
  case object Gibi extends IECPrefix(3, "Gi", "gibi")
  case object Tebi extends IECPrefix(4, "Ti", "tebi")
  case object Pebi extends IECPrefix(5, "Pi", "pebi")
  case object Exbi extends IECPrefix(6, "Ei", "exbi")
  case object Zebi extends IECPrefix(7, "Zi", "zebi")
  case object Yobi extends IECPrefix(8, "Yi", "yobi")
}

sealed abstract class IECPrefix(
  val exp: Int,
  symbol: String,
  name: String
)
  extends Prefix (
    base = math.pow(2, 10),
    exp = exp,
    symbol = symbol,
    name = name
  )
