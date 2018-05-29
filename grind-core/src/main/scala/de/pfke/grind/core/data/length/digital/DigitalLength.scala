package de.pfke.grind.core.data.length.digital

import de.pfke.grind.core.data.length.Length

object DigitalLength {
  def fromBits(in: Double): DigitalLength = if (in % 8 == 0) ByteLength(in / 8) else BitLength(in)
  def zero: DigitalLength = ByteLength.zero
}

abstract class DigitalLength
  extends Length {
  def +(that: DigitalLength): DigitalLength = DigitalLength.fromBits(this.toBits + that.toBits)
  def -(that: DigitalLength): DigitalLength = DigitalLength.fromBits(this.toBits - that.toBits)
  def *(that: DigitalLength): DigitalLength = DigitalLength.fromBits(this.toBits * that.toBits)
  def /(that: DigitalLength): DigitalLength = DigitalLength.fromBits(this.toBits / that.toBits)

  /**
    * Return the value as bits.
    */
  def toBits: Double

  /**
    * Return the value as byte.
    */
  def toByte: Double
}
