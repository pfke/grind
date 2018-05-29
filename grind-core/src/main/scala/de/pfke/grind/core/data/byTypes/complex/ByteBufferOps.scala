package de.pfke.grind.core.data.byTypes.complex

import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.charset.{Charset, StandardCharsets}

import de.pfke.grind.core.data.byTypes.stream.ByteBufferBackedInputStream

object ByteBufferOps {
  def asString (
    in: ByteBuffer
  ) (
    implicit charset: Charset = StandardCharsets.UTF_8
  ): String = {
    try {
      val oldPos = in.position()
      val str = charset
        .newDecoder()
        .decode(in)
        .toString

      in.position(oldPos)
      str
    } catch {
      case _: Exception => ""
    }
  }

  def asString_iso8859_1 (in: ByteBuffer): String = asString(in = in)(StandardCharsets.ISO_8859_1)
  def asString_utf8 (in: ByteBuffer): String = asString(in = in)(StandardCharsets.UTF_8)
}

object ByteBufferOpsIncludes
  extends ByteBufferOpsIncludes

trait ByteBufferOpsIncludes {
  /**
    * Convert byte buffer to input stream
    */
  implicit def ByteBufferToInputStream(
    in: ByteBuffer
  ): InputStream = new ByteBufferBackedInputStream(in)

  implicit class ByteBufferImplicits_fromByteBuffer(
    in: ByteBuffer
  ) {
    def asArray: Array[Byte] = {
      val out = new Array[Byte](in.remaining())
      in.get(out)
      in.flip()
      out
    }

    def asString_iso8859_1: String = asString(StandardCharsets.ISO_8859_1)
    def asString_utf8: String = asString(StandardCharsets.UTF_8)

    def asString (
      implicit charset: Charset = StandardCharsets.UTF_8
    ): String = ByteBufferOps.asString(in = in)
  }
}
