package de.pfke.grind.core.data.units.prefix

object SIPrefix {
  case object Yotta extends SIPrefix( 24,  "Y", "yotta")
  case object Zetta extends SIPrefix( 21,  "Z", "zetta")
  case object Exa   extends SIPrefix( 18,  "E", "exa")
  case object Peta  extends SIPrefix( 15,  "P", "peta")
  case object Tera  extends SIPrefix( 12,  "T", "tera")
  case object Giga  extends SIPrefix(  9,  "G", "giga")
  case object Mega  extends SIPrefix(  6,  "M", "mega")
  case object Kilo  extends SIPrefix(  3,  "k", "kilo")
  case object Hekto extends SIPrefix(  2,  "h", "hekto")
  case object Deka  extends SIPrefix(  1, "da", "deka")

  case object Dezi  extends SIPrefix( -1,  "d", "dezi")
  case object Zenti extends SIPrefix( -2,  "c", "zenti")
  case object Milli extends SIPrefix( -3,  "m", "milli")
  case object Mikro extends SIPrefix( -6,  "µ", "mikro")
  case object Nano  extends SIPrefix( -9,  "n", "nano")
  case object Piko  extends SIPrefix(-12,  "p", "piko")
  case object Femto extends SIPrefix(-15,  "f", "femto")
  case object Atto  extends SIPrefix(-18,  "a", "atto")
  case object Zepto extends SIPrefix(-21,  "z", "zepto")
  case object Yokto extends SIPrefix(-24,  "y", "yokto")
}

sealed abstract class SIPrefix (
  exp: Int,
  symbol: String,
  name: String
)
  extends Prefix(10, exp, symbol, name)
