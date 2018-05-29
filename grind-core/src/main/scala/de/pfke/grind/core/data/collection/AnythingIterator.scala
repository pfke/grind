package de.pfke.grind.core.data.collection

import java.nio.ByteOrder
import java.nio.charset.{Charset, StandardCharsets}

import akka.util.{ByteIterator, ByteString}
import de.pfke.grind.core.data.collection.BitStringAlignment.BitStringAlignment
import de.pfke.grind.core.data.length.digital.{BitLength, ByteLength, DigitalLength}
import de.pfke.grind.core.refl.{GenericOps, PrimitiveOps, SizeOf}

import scala.reflect.ClassTag
import scala.reflect.runtime.{universe => ru}

object AnythingIterator {
  def apply (
    in: ByteIterator
  ) (
    implicit
    byteOrder: ByteOrder
  ): AnythingIterator = new AnythingIterator(iter = in, bitAlignment = BitStringAlignment._32Bit)

  def apply (
    in: ByteString
  ) (
    implicit
    byteOrder: ByteOrder
  ): AnythingIterator = apply(in = in.iterator)

  def empty: AnythingIterator = apply(in = ByteString())(byteOrder = ByteOrder.LITTLE_ENDIAN)
}

class AnythingIterator (
  iter: ByteIterator,
  bitAlignment: BitStringAlignment
) (
  implicit
  byteOrder: ByteOrder
) {
  // fields
  private var _currentBitIterator: Option[BitStringIterator] = None

  /**
    * Returns a new iterator with the given bit alignment
    */
  def iterator (
    bitAlignment: BitStringAlignment = BitStringAlignment._32Bit
  ) = new AnythingIterator(iter, bitAlignment = bitAlignment)

  /**
    * Return remaining bytes
    */
  def len: DigitalLength = ByteLength(iter.len)

  /**
    * Return remaining bytes and empty the iterator
    */
  def length: DigitalLength = ByteLength(iter.length)

  def readString (
    lenToRead: DigitalLength
  ) (
    implicit
    charset: Charset = StandardCharsets.ISO_8859_1
  ): String = decodeString(getByteIterator, Some(lenToRead.toByte.toInt))

  /**
    * Entweder ein poar Bytes oder ein paar Bits lesen
    */
  def read[A] (
    lenToRead: Option[DigitalLength]
  )(
    implicit
    classTag: ClassTag[A],
    typeTag: ru.TypeTag[A]
  ): A = {
    val value = lenToRead match {
      case Some(t: BitLength) if GenericOps.isString(typeTag.tpe) => throw new IllegalArgumentException(s"could not read a string as bits")
      case Some(t: BitLength) => readAsBits(lenToRead = t)
      case Some(t: ByteLength) => readAsBytes(lenToRead = t)

      case Some(t) => throw new IllegalArgumentException(s"unknown length type: $t")

      case None => readAsBytes()

    }

    value.asInstanceOf[A]
  }

  /**
    * Entweder ein paar Bytes oder ein paar Bits lesen
    */
  def read[A] (
    lenToRead: DigitalLength
  )(
    implicit
    classTag: ClassTag[A],
    typeTag: ru.TypeTag[A]
  ): A = read(Some(lenToRead))

  /**
    * Entweder ein paar Bytes oder ein paar Bits lesen
    */
  def read[A]() (
    implicit
    classTag: ClassTag[A],
    typeTag: ru.TypeTag[A]
  ): A = read(None)

  /**
    * Anzahl der Bits lesen
    */
  protected def readAsBits[A](
    lenToRead: DigitalLength
  )(
    implicit
    classTag: ClassTag[A],
    typeTag: ru.TypeTag[A]
  ): A = PrimitiveOps.promoteTo(getBitIterator.read(lenToRead.toBits.toInt))

  /**
    * Bytes lesen (Datentyplänge)
    */
  protected def readAsBytes[A]() (
    implicit
    classTag: ClassTag[A],
    typeTag: ru.TypeTag[A]
  ): A = readAsBytes[A](lenToRead = ByteLength(SizeOf.guess[A]()))

  /**
    * Anzahl der Bytes lesen
    */
  protected def readAsBytes[A](
    lenToRead: DigitalLength
  )(
    implicit
    classTag: ClassTag[A],
    typeTag: ru.TypeTag[A]
  ): A = {
    def defaultLen = SizeOf.guess[A]()
    def len = lenToRead.toByte.toInt

    def isEqualLen = len == defaultLen
    def areTooFewBytes = len < defaultLen
    def areTooMuchBytes = len > defaultLen

    val iterToUse = getByteIterator

    require(iterToUse.len >= lenToRead.toByte, s"iterator contains to few bytes. Needed: ${lenToRead.toByte.toInt}, given: ${iterToUse.len}")

    /**
      * Lesen bei zuviel angeforderten Bytes (z.B. Datentyp hat 4 Bytes, es sollen aber 6 gelesen werden -> 4 lesen und den Rest weghauen)
      */
    def readWPadding[B](op: (ByteIterator) => B): B = {
      val lenDavor = iterToUse.len
      val value = op(iterToUse)
      val lenDanach = iterToUse.len

      iterToUse.getBytes(len - (lenDavor - lenDanach)) // read padding
      value
    }

    def readTooFew(): Long = {
      len match {
        case 0 => 0l
        case 1 => iterToUse.getByte.toLong
        case 2 => iterToUse.getShort.toLong
        case 3 => ((iterToUse.getShort << 8) | iterToUse.getByte).toLong
        case 4 => iterToUse.getInt.toLong
        case 5 => ((iterToUse.getShort << 8) | iterToUse.getByte).toLong
        case 6 => (iterToUse.getInt.toLong << 16) | iterToUse.getShort
        case 7 => (iterToUse.getInt.toLong << 24) | (iterToUse.getShort << 8) | iterToUse.getByte
      }
    }

    val result = PrimitiveOps.toScalaType(typeTag.tpe) match {
      case t if t =:= ru.typeOf[Boolean] && isEqualLen => iterToUse.getByte != 0
      case t if t =:= ru.typeOf[Boolean] && areTooFewBytes => false
      case t if t =:= ru.typeOf[Boolean] && areTooMuchBytes => readWPadding(_.getByte) != 0

      case t if t =:= ru.typeOf[Byte] && isEqualLen => iterToUse.getByte
      case t if t =:= ru.typeOf[Byte] && areTooFewBytes => readTooFew().toByte
      case t if t =:= ru.typeOf[Byte] && areTooMuchBytes => readWPadding(_.getByte)

      case t if t =:= ru.typeOf[Char] && isEqualLen => iterToUse.getShort.toChar
      case t if t =:= ru.typeOf[Char] && areTooFewBytes => readTooFew().toChar
      case t if t =:= ru.typeOf[Char] && areTooMuchBytes => readWPadding(_.getShort).toChar

      case t if t =:= ru.typeOf[Short] && isEqualLen => iterToUse.getShort
      case t if t =:= ru.typeOf[Short] && areTooFewBytes => readTooFew().toShort
      case t if t =:= ru.typeOf[Short] && areTooMuchBytes => readWPadding(_.getShort)

      case t if t =:= ru.typeOf[Int] && isEqualLen => iterToUse.getInt
      case t if t =:= ru.typeOf[Int] && areTooFewBytes => readTooFew().toInt
      case t if t =:= ru.typeOf[Int] && areTooMuchBytes => readWPadding(_.getInt)

      case t if t =:= ru.typeOf[Long] && isEqualLen => iterToUse.getLong
      case t if t =:= ru.typeOf[Long] && areTooFewBytes => readTooFew()
      case t if t =:= ru.typeOf[Long] && areTooMuchBytes => readWPadding(_.getLong)

      case t if t =:= ru.typeOf[Float] && isEqualLen => iterToUse.getFloat
      case t if t =:= ru.typeOf[Float] && areTooFewBytes => readTooFew().toFloat
      case t if t =:= ru.typeOf[Float] && areTooMuchBytes => readWPadding(_.getFloat)

      case t if t =:= ru.typeOf[Double] && isEqualLen => iterToUse.getDouble
      case t if t =:= ru.typeOf[Double] && areTooFewBytes => readTooFew().toDouble
      case t if t =:= ru.typeOf[Double] && areTooMuchBytes => readWPadding(_.getLong)

      case t if t =:= ru.typeOf[String] => decodeString(iter = iter, length = if (len == 0) None else Some(len))

      case t => throw new IllegalArgumentException(s"unsupported type to read, only value types are supported")
    }

    result.asInstanceOf[A]
  }

  /**
    * Decode a string
    *
    * @param iter byte iterator with the bytes to decode
    * @param length length to decode
    * @param charset charset to use
    *
    * @return
    */
  protected def decodeString(
    iter: ByteIterator,
    length: Option[Int] = None
  ) (
    implicit
    charset: Charset = StandardCharsets.ISO_8859_1
  ): String = {
    def getLength = length.getOrElse(iter.len)

    val stringArray = new Array[Byte](getLength)
    iter.getBytes(stringArray)

    new String(stringArray, charset)
  }

  /**
    * Löscht den aktuellen BitIterator
    */
  private def clearBitIterator(): Unit = {
    _currentBitIterator = None
  }

  /**
    * Gibt den aktuellen BitIterator zurueck, oder erstellt einen neuen.
    */
  private def getBitIterator: BitStringIterator = {
    val newIterator = _currentBitIterator match {
      case Some(x) => x
      case _ => BitStringIterator(iter = iter, alignment = bitAlignment)
    }

    _currentBitIterator = Some(newIterator)
    _currentBitIterator.get
  }

  /**
    * Gibt den aktuellen ByteIterator zurueck
    */
  private def getByteIterator: ByteIterator = {
    clearBitIterator()
    iter
  }
}
