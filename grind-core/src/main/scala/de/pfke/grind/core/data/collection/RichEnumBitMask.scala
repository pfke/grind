package de.pfke.grind.core.data.collection

import de.pfke.grind.core.data.byTypes.primitives.BitTwiddling

import scala.collection.{BitSet, mutable}

object RichEnumBitMask {
  /**
    * Create new instance with this enum as underlying type
    *
    * @param enum this is the one
    * @tparam E type info
    *
    * @return new instance
    */
  def apply[E <: Enumeration](
    enum: E
  ): RichEnumBitMask[E] = applyWithLen(enum, 0, BitTwiddling.getMostSignificantBit(enum).getOrElse(0) + 1)

  /**
    * Create new instance with this enum as underlying type (with len info as mask)
    *
    * @param enum this is the one
    * @param shift shift the result
    * @tparam E type info
    *
    * @return new instance
    */
  def applyWithLen[E <: Enumeration](
    enum: E,
    shift: Int,
    len: Int
  ): RichEnumBitMask[E] = applyWithMask(enum, shift, (1 << len) - 1)

  /**
    * Create new instance with this enum as underlying type (with given mask as mask)
    *
    * @param enum this is the one
    * @param shift shift the result
    * @param mask mask out these bits
    * @tparam E type info
    *
    * @return new instance
    */
  def applyWithMask[E <: Enumeration](
    enum: E,
    shift: Int,
    mask: Int
  ): RichEnumBitMask[E] = new RichEnumBitMask[E](enum, _shift = shift, _mask = mask)
}

class RichEnumBitMask[E <: Enumeration](
  _enum: E,
  _shift: Int,
  _mask: Int
) {
  // fields
  private val _bs = new mutable.BitSet()

  def mask: Int = _mask
  def shift: Int = _shift

  /**
    * This method is used to clear out the all values.
    */
  def clear(): Unit = _bs.clear()

  /**
    * Returns true if the value is active
    */
  def contains(value: E#Value): Boolean = _bs.contains(shifted(value))

  /**
    * This method is used to clear out the given value.
    */
  def -=(value: E#Value): this.type = {
    _bs -= shifted(value)
    this
  }

  /**
    * This method takes the enum and sets its value.
    */
  def -=(value: E#Value*): this.type = {
    value.foreach(-=)
    this
  }

  /**
    * This method takes the enum and sets its value.
    */
  def +=(value: E#Value): this.type = {
    if(!maskOut(value))
      _bs += shifted(value)
    this
  }

  /**
    * This method takes the enum and sets its value.
    */
  def +=(value: E#Value*): this.type = {
    value.foreach(+=)
    this
  }

  /**
    * Add this value
    */
  def add(value: E#Value): Boolean = {
    if(!maskOut(value))
      _bs.add(shifted(value))
    false
  }

  /**
    * Remove this value
    */
  def remove(value: E#Value): Boolean = _bs.remove(shifted(value))

  /**
    * Return our bit mask.
    */
  def toBitMask: Array[Long] = toBitSet.toBitMask

  /**
    * Return our bit set.
    */
  def toBitSet: BitSet = _bs

  /**
    * Returns true if value should masked out
    */
  private def maskOut(value: E#Value) = ((1 << value.id) & _mask) == 0

  /**
    * Shift enum to usable var
    */
  private def shifted(value: E#Value) = value.id + _shift
}
