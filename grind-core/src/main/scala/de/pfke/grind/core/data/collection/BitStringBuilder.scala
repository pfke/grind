package de.pfke.grind.core.data.collection

import java.nio.ByteOrder

import akka.util.ByteString
import de.pfke.grind.core.data.collection.BitStringAlignment.BitStringAlignment

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag
import scala.reflect.runtime.{universe => ru}

object BitStringBuilder {
  def newBuilder(
    alignment: BitStringAlignment = BitStringAlignment._32Bit
  ) (
    implicit
    byteOrder: ByteOrder = ByteOrder.BIG_ENDIAN
  ): BitStringBuilder = new BitStringBuilder(alignment = alignment)

  /**
    * Create a bitmask from given bits
    */
  private[collection] def bitmask(bits: Int): Long = if (bits == 64) 0xffffffffffffffffl else (1l << bits) - 1
}

class BitStringBuilder(
  alignment: BitStringAlignment
) (
  implicit
  byteOrder: ByteOrder
)
  extends mutable.Builder[Byte, ByteString] {
  // fields
  private val _data = new ArrayBuffer[Long]()
  private var _bitLength = 0

  /**
    * Append byte to builder
    */
  override def += (
    elem: Byte
  ): this.type = appendBits(bits = 8, value = elem)

  /**
    * Build ByteString from our data
    */
  def result(): ByteString = {
    val builder = ByteString.newBuilder

    val writeFunc: (Long) => Unit = alignment match {
      case BitStringAlignment._8Bit  => i => builder.putByte(i.toByte)
      case BitStringAlignment._16Bit => i => builder.putShort(i.toShort)
      case BitStringAlignment._32Bit => i => builder.putInt(i.toInt)
    }

    // macht folgendes:
    //   alignment match {
    //     case BitStringAlignment._8Bit  => List(i & bitmask(8),  (i >>>  8) & bitmask(8),  (i >>> 16) & bitmask(8),  (i >>> 24) & bitmask(8),  (i >>> 32) & bitmask(8),  (i >>> 40) & bitmask(8),  (i >>> 48) & bitmask(8),  (i >>> 56) & bitmask(8))
    //     case BitStringAlignment._16Bit => List(i & bitmask(16), (i >>> 16) & bitmask(16), (i >>> 32) & bitmask(16), (i >>> 48) & bitmask(16))
    //     case BitStringAlignment._32Bit => List(i & bitmask(32), (i >>> 32) & bitmask(32))
    //   }
    def makeToList (
      step: Int
    ) (
      value: Long
    ) = (0 until 64 by step).map { i => (value >>> i) & BitStringBuilder.bitmask(step) }
    val makeToListFn = makeToList(step = BitStringAlignment.bitWidth(alignment))(_)

    // round up to aligned bytes
    val alignedBytes = (_bitLength + (BitStringAlignment.bitWidth(alignment) - 1)) / 8
    // get no. of values to render
    val elementsToUse = alignedBytes / (BitStringAlignment.bitWidth(alignment) / 8)

    _data
      .flatMap(makeToListFn) // separate our values by alignment
      .take(elementsToUse)   // drop unneeded values
      .reverse
      .foreach(writeFunc)    // write to ByteString builder

    builder.result()
  }

  /**
    * Clear this bit stirng
    */
  override def clear (): Unit = {
    _data.clear()
    _bitLength = 0
  }

  /**
    * Return the bitlength
    */
  def lengthBit: Int = _bitLength

  /**
    * Append given number of bits
    */
  def appendBits[A] (
    bits: Int,
    value: A
  )(
    implicit
    classTag: ClassTag[A],
    typeTag: ru.TypeTag[A]
  ): this.type = {
    val longValue = value match {
      case t: Boolean if t => 1l
      case _: Boolean => 0l

      case t: Byte => t.toLong
      case t: Char => t.toLong
      case t: Short => t.toLong
      case t: Int => t.toLong
      case t: Long => t.toLong
      case t: Float => t.toLong
      case t: Double => t.toLong

      case _ => throw new IllegalArgumentException(s"unsupported type to write ($typeTag), only value types are supported")
    }

    appendBits(bits = bits, value = longValue)
  }

  /**
    * Append given number of bits
    */
  def appendBits (
    bits: Int,
    value: Long
  ): this.type = {
    shl(bits = bits)
    or(bits = bits, value = value)

    this
  }

  /**
    * Or the given data to our array
    */
  def | (in: Byte): this.type = or(bits = 8, value = in.toLong)
  def | (in: Short): this.type = or(bits = 16, value = in.toLong)
  def | (in: Int): this.type = or(bits = 32, value = in.toLong)
  def | (in: Long): this.type = or(bits = 64, value = in.toLong)

  /**
    * Or the given data to our array
    */
  private def or (
    bits: Int,
    value: Long
  ): this.type = {
    ensureBits(bits)

    _data
      .update(
        0,
        _data.head | (value.toLong & BitStringBuilder.bitmask(bits))
      )

    this
  }

  /**
    * shl the complete array
    */
  def << (bits: Int): this.type = shl(bits = bits)
  def shl (
    bits: Int
  ): this.type = {
    ensureBits(bits + _bitLength)

    val arrayIdx = (_bitLength - 1) / 64

    // von i-1 ins i verschieben
    (arrayIdx to 0 by -1)
      .foreach { i =>
        val lowerIdxMinus = (bits / 64) + (if ((bits % 64) == 0) 0 else 1)
        val lowerIdx = i - lowerIdxMinus
        val bitsFromLower = bits % 64
        val shiftLower = 64 - bitsFromLower
        val lowerValue = if (lowerIdx < 0) 0l else _data(lowerIdx) >>> shiftLower

        // Sonderfall: alles kopieren
        if ((bits % 64) == 0) {
          _data.update(
            i,
            lowerValue
          )
        } else {
          _data.update(
            i,
            ((_data(i) << bitsFromLower) & ~bitsFromLower) | lowerValue
          )
        }
      }

    this
  }

  /**
    * Ensure, that our bit array is at least .. bits long (ceil to full longs)
    */
  private def ensureBits(bits: Int): Unit = {
    // create new array index, if necessary
    (_data.size until ((bits + 63) / 64))
      .foreach { i => _data += 0l }

    // update our bitlength
    _bitLength = math.max(_bitLength, bits)
  }
}
