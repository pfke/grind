package de.pfke.grind.core.data.byTypes.complex

import java.nio.charset.{Charset, StandardCharsets}

object ArrayOps {
  /**
    * Convert an array to a string
    */
  def toString (
    in: Array[Byte]
  ) (
    implicit
    charset: Charset = StandardCharsets.UTF_8
  ): String = new String(in, charset)

  /**
    * Convert byte array to hex string.
    */
  def toHex (in: Array[Byte]): String = in.map("%02x" format _).mkString
}


object ArrayOpsIncludes
  extends ArrayOpsIncludes

trait ArrayOpsIncludes {
  implicit class ArrayOpsImplicits_fromArray (
    in: Array[Byte]
  ) {
    def toString (
      implicit
      charset: Charset = StandardCharsets.UTF_8
    ): String = ArrayOps.toString(in = in)
    def toHex: String = ArrayOps.toHex(in = in)
  }
}