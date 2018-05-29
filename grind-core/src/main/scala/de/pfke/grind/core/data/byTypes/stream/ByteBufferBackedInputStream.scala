package de.pfke.grind.core.data.byTypes.stream

import java.io.{IOException, InputStream}
import java.nio.ByteBuffer

/**
  * Wrap a byte buffer into an input stream
  *
  * @param buffer is the byte buffer itself
  */
class ByteBufferBackedInputStream (
  buffer: ByteBuffer
)
  extends InputStream {
  /**
    * Read one byte
    */
  @throws(classOf[IOException])
  override def read (): Int = if (buffer.hasRemaining) buffer.get() & 0xFF else -1

  /**
    * Read the given len
    */
  @throws(classOf[IOException])
  override def read (
    bytes: Array[Byte],
    off: Int,
    len: Int
  ): Int = {
    require(bytes.length >= len, s"given array is smaller then len (${bytes.length} < $len)")

    if (!buffer.hasRemaining) {
      return -1
    }

    val lenToRead = Math.min(len, buffer.remaining())
    buffer.get(bytes, off, lenToRead)
    lenToRead
  }
}
