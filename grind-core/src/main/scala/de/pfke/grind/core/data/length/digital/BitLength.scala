package de.pfke.grind.core.data.length.digital

import de.pfke.grind.core.data.units.prefix.Prefix

object BitLength {
  def apply(in: Double): BitLength = new BitLength(data = in)
  def zero: BitLength = apply(0)
}

class BitLength(
  private[BitLength] val data: Double
)
  extends DigitalLength
    with Ordered[BitLength] {
  def +(that: BitLength): BitLength = BitLength(data + that.data)
  def -(that: BitLength): BitLength = BitLength(data - that.data)
  def *(that: BitLength): BitLength = BitLength(data * that.data)
  def /(that: BitLength): BitLength = BitLength(data / that.data)

  def +(op: Double): BitLength = BitLength(data + op)
  def -(op: Double): BitLength = BitLength(data - op)
  def *(op: Double): BitLength = BitLength(data * op)
  def /(op: Double): BitLength = BitLength(data / op)

  /**
    * Result of comparing `this` with operand `that`.
    *
    * Implement this method to determine how instances of A will be sorted.
    *
    * Returns `x` where:
    *   - `x < 0` when `this < that`
    *   - `x == 0` when `this == that`
    *   - `x > 0` when  `this > that`
    */
  override def compare(that: BitLength): Int = data.compareTo(that.data)

  /**
    * Equals implementation.
    */
  override def equals(obj: scala.Any): Boolean = obj.isInstanceOf[BitLength] && compare(obj.asInstanceOf[BitLength]) == 0

  /**
    * Return the value as byte.
    */
  def toBits: Double = data

  /**
    * Return the value as byte.
    */
  def toByte: Double = asByte.toByte

  /**
    * Return the value as byte.
    */
  def asByte: ByteLength = ByteLength(data / 8, Prefix.No)
}
