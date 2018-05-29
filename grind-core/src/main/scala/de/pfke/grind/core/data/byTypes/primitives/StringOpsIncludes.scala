package de.pfke.grind.core.data.byTypes.primitives

import java.nio.charset.{Charset, StandardCharsets}
import java.nio.{ByteBuffer, CharBuffer}

object StringOps {
  def asByteBuffer_iso8859_1 (in: String): ByteBuffer = asByteBuffer(in = in)(charset = StandardCharsets.ISO_8859_1)
  def asByteBuffer_utf8 (in: String): ByteBuffer = asByteBuffer(in = in)(charset = StandardCharsets.UTF_8)

  def asByteBuffer (
    in: String
  ) (
    implicit
    charset: Charset = StandardCharsets.UTF_8
  ): ByteBuffer = {
    try {
      charset
        .newEncoder()
        .encode(CharBuffer.wrap(in))
    } catch {
      case _: Exception => ByteBuffer.allocateDirect(0)
    }
  }

  def asBoolean (in: String): Boolean = asBooleanOpt(in = in).getOrElse(false)
  def asBooleanOpt (in: String): Option[Boolean] = {
    in.toLowerCase match {
      case "true" => Some(true)
      case "false" => Some(false)

      case _ => None
    }
  }

  /**
    * Try to convert from string to double and return as Option[Double]:
    *
    * toDouble("10.2").getOrElse(23.0)
    */
  def asDoubleOpt (in: String): Option[Double] = {
    try {
      Some(java.lang.Double.parseDouble(in.replace(',', '.')))
    } catch {
      case _: NumberFormatException => None
    }
  }

  /**
    * Try to convert from string to int and return as Option[Int]:
    *
    * toInt("102").getOrElse(23)
    */
  def asIntOpt (in: String, radix: Int = 10): Option[Int] = {
    try {
      Some(Integer.parseInt(in, radix))
    } catch {
      case _: NumberFormatException => None
    }
  }

  /**
    * Try to convert from string to int and return as Option[Int]:
    *
    * toInt("102").getOrElse(23)
    */
  def asLongOpt (in: String, radix: Int = 10): Option[Long] = {
    try {
      Some(java.lang.Long.parseLong(in, radix))
    } catch {
      case e: NumberFormatException => None
    }
  }

  /**
    * Try to convert from string to unsigned int and return as Option[Int]:
    */
  def asUIntOpt (in: String, radix: Int = 10): Option[Int] = {
    try {
      Some(Integer.parseUnsignedInt(in, radix))
    } catch {
      case e: NumberFormatException => None
    }
  }

  /**
    * Try to convert from string to int and return as Option[Int]:
    *
    * toInt("102").getOrElse(23)
    */
  def asULongOpt (in: String, radix: Int = 10): Option[Long] = {
    try {
      Some(java.lang.Long.parseUnsignedLong(in, radix))
    } catch {
      case e: NumberFormatException => None
    }
  }

  /**
    * Comfort-Funktion um den übergebenen String in eine Option zu packen, oder halt None zurückzugeben,
    * wenn der String leer ist.
    */
  def asOption (in: String): Option[String] = if(in.isEmpty) None else Some(in)

  /**
    * Diese Funktion gibt die Anzahl der gleichen Zeichen von String 1 und String 2 zurück.
    */
  def countMatches (in: String, toMatch: String): Int = {
    if (in.isEmpty || toMatch.isEmpty) {
      0
    } else if(in(0) == toMatch(0)) {
      1 + countMatches(in.drop(1), toMatch.drop(1))
    } else {
      0
    }
  }

  /**
    * Returns true if the given string can be converted into an integer.
    */
  def isInt (in: String, radix: Int = 10): Boolean = asIntOpt(in = in, radix = radix).nonEmpty

  /**
    * Returns true if the given string can be converted into an unsigned integer.
    */
  def isLong (in: String, radix: Int = 10): Boolean = asLongOpt(in = in, radix = radix).nonEmpty

  /**
    * Returns true if the given string can be converted into an unsigned integer.
    */
  def isUInt (in: String, radix: Int = 10): Boolean = asUIntOpt(in = in, radix = radix).nonEmpty

  /**
    * Returns true if the given string can be converted into an unsigned integer.
    */
  def isULong (in: String, radix: Int = 10): Boolean = asULongOpt(in = in, radix = radix).nonEmpty

  /**
    * Einruecken mit defaults
    */
  def indent (in: String): String = indentBy(in = in)

  /**
    * Den uebergebenen String einruecken
    *
    * @param by um diese Anzahl Spaces einruecken
    * @param indent1stLine die erste Zeile auch einruecken?
    * @return formatierter String
    */
  def indentBy(
    in: String,
    by: Int = 2,
    indent1stLine: Boolean = false
  ): String = trimSpaces(in.replaceAll("\n", "\n".padTo(by + 1, ' ')))

  /**
    * Trim spaces from lines
    */
  def trimSpaces(
    in: String
  ): String = in.replaceAll("\n\\s+\n", "\n\n")
}

object StringOpsIncludes
  extends StringOpsIncludes

trait StringOpsIncludes {
  /**
    * Convert string to byte buffer
    */
  implicit def StringOpsImplicits_fromStringToByteBuffer (
    in: String
  ) (
    implicit
    charset: Charset = StandardCharsets.UTF_8
  ): ByteBuffer = new StringOpsImplicits_fromString(in).asByteBuffer

  implicit class StringOpsImplicits_fromString (
    in: String
  ) {
    def asByteBuffer_iso8859_1: ByteBuffer = StringOps.asByteBuffer_iso8859_1(in = in)
    def asByteBuffer_utf8: ByteBuffer = StringOps.asByteBuffer_utf8(in = in)

    def asByteBuffer(implicit charset: Charset = StandardCharsets.UTF_8): ByteBuffer = StringOps.asByteBuffer(in = in)

    def asBoolean: Boolean = StringOps.asBoolean(in = in)
    def asBooleanOpt: Option[Boolean] = StringOps.asBooleanOpt(in = in)
    def asDoubleOpt: Option[Double] = StringOps.asDoubleOpt(in = in)
    def asIntOpt (radix: Int = 10): Option[Int] = StringOps.asIntOpt(in = in, radix = radix)
    def asLongOpt (radix: Int = 10): Option[Long] =  StringOps.asLongOpt(in = in, radix = radix)
    def asUIntOpt (radix: Int = 10): Option[Int] = StringOps.asUIntOpt(in = in, radix = radix)
    def asULongOpt (radix: Int = 10): Option[Long] = StringOps.asULongOpt(in = in, radix = radix)
    def asOption: Option[String] = StringOps.asOption(in = in)
    def countMatches (toMatch: String): Int = StringOps.countMatches(in = in, toMatch = toMatch)
    def isInt (radix: Int = 10): Boolean = StringOps.isInt(in = in, radix = radix)
    def isLong (radix: Int = 10): Boolean = StringOps.isLong(in = in, radix = radix)
    def isUInt (radix: Int = 10): Boolean = StringOps.isUInt(in = in, radix = radix)
    def isULong (radix: Int = 10): Boolean =  StringOps.isULong(in = in, radix = radix)

    def indent: String = StringOps.indentBy(in = in)
    def indentBy (
      by: Int = 2,
      indent1stLine: Boolean = false
    ): String = StringOps.indentBy(in = in, by = by, indent1stLine = indent1stLine)
    def trimSpaces: String = StringOps.trimSpaces(in = in)
  }
}
