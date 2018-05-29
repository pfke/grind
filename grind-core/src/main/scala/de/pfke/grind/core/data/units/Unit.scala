package de.pfke.grind.core.data.units

import de.pfke.grind.core.data.units.prefix.Prefix

abstract class Unit(
  prefix: Prefix,
  symbol: String
) {
  override def toString = s"$prefix$symbol"
}
