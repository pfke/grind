package de.pfke.grind.core.data.collection

import java.nio.ByteOrder

import akka.util.{ByteIterator, ByteString}
import de.pfke.grind.core.data.collection.BitStringAlignment.BitStringAlignment

import scala.collection.mutable.ArrayBuffer

object BitStringIterator {
  def apply (
    data: ByteString,
    alignment: BitStringAlignment
  ) (
    implicit byteOrder: ByteOrder
  ): BitStringIterator = apply(iter = data.iterator, alignment = alignment)

  def apply (
    iter: ByteIterator,
    alignment: BitStringAlignment
  ) (
    implicit byteOrder: ByteOrder
  ): BitStringIterator = new BitStringIterator(iter = iter, alignment = alignment)

  def empty (
    implicit byteOrder: ByteOrder
  ) = apply(ByteString.empty, BitStringAlignment._32Bit)
}

class BitStringIterator (
  iter: ByteIterator,
  val alignment: BitStringAlignment
)(
  implicit byteOrder: ByteOrder
) extends Iterator[Byte] {
  // fields
  private var _bitArray = new ArrayBuffer[Long]()
  private var _bitCount = 0
  private var _currentBitsPointer = 0

  /**
    * Return remaining bytes
    */
  def len: Int = (_bitCount - _currentBitsPointer) + (iter.len * 8)

  override def hasNext: Boolean = ((_currentBitsPointer + 1) < _bitCount) || iter.hasNext

  override def next (): Byte = read(1).toByte

  /**
    * Read bits from iterator.
    * Read start at bit 0.
    *
    * E.g. when you read 8 bit, u will get 0x78 values first.
    * 0x12345678
    */
  def read (
    bits: Int
  ): Long = {
    require(bits <= 64, s"bits to read to big ($bits, can read max 64 bits)")

    if (bits <= 0) return 0l

    val currentArrayIdx = _currentBitsPointer / 64
    val relativeBitPos = 64 - (_currentBitsPointer % 64) - bits

    ensureBits(bits + _currentBitsPointer)

    def readBits = _bitArray(currentArrayIdx) >>> relativeBitPos

    // alles in einem array-idx lesbar?
    val resultVal: Long = if (relativeBitPos < 0) {

      // read the bits from the current array idx
      val upperBitOffset = bits + relativeBitPos
      val upperValue = _bitArray(currentArrayIdx) & BitStringBuilder.bitmask(upperBitOffset)

      // read the bits form the following array idx
      val lowerBitOffset = 64 + relativeBitPos
      val lowerValue = (_bitArray(currentArrayIdx + 1) >>> lowerBitOffset) & BitStringBuilder.bitmask(bits - upperBitOffset)

      upperValue << (bits - upperBitOffset) | lowerValue
    } else {
      readBits & BitStringBuilder.bitmask(bits)
    }

    // inc current bit pos
    _currentBitsPointer += bits
    resultVal
  }

  /**
    * Return our aligned bit width value
    */
  private def alignBitsBy = BitStringAlignment.bitWidth(alignment)

  /**
    * Ensure that our array holds at least <code>neededBits</code>
    */
  private def ensureBits (
    neededBits: Int
  ): Unit = {
    if (neededBits > _bitCount) {
      readNextChunk(neededBits = neededBits)
    }
  }

  /**
    * Read next chunk. chunk size depends on alignment
    */
  private def readNextChunk (
    neededBits: Int
  ): Unit = {
    require(alignBitsBy <= (iter.len * 8), s"empty iterator (wanted=$alignBitsBy, remaining=${iter.len * 8})")

    def readChunk() = alignment match {
      case BitStringAlignment._8Bit  => iter.getByte.toLong
      case BitStringAlignment._16Bit => iter.getShort.toLong
      case BitStringAlignment._32Bit => iter.getInt.toLong
    }

    val chunk = readChunk()
    val idx = _bitCount / 64
    val bitOffset = math.abs(64 - (_bitCount % 64) - alignBitsBy) // wir kommen von oben und lesen nach unten

    // inc array?
    if ((_bitCount % 64) == 0) {
      _bitArray += 0l
    }

    // update elem
    _bitArray.update(idx, _bitArray(idx) | ((chunk << bitOffset) & BitStringBuilder.bitmask(bitOffset + alignBitsBy)))
    // update bit count
    _bitCount += alignBitsBy
    // read more chunks?
    ensureBits(neededBits = neededBits)
  }
}
