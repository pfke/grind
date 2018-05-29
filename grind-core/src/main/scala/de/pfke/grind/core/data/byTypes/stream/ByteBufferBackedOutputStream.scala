package de.pfke.grind.core.data.byTypes.stream

import java.io.{IOException, OutputStream}
import java.nio.ByteBuffer

/**
  * Wrap a byte buffer into an output stream
  *
  * @param buffer is the byte buffer itself
  */
class ByteBufferBackedOutputStream (
  buffer: ByteBuffer
)
  extends OutputStream {
  /**
    * Write one byte
    */
  @throws(classOf[IOException])
  def write(b: Int): Unit = buffer.put(b.asInstanceOf[Byte])

  /**
    * Write an array of bytes
    */
  @throws(classOf[IOException])
  override def write (
    bytes: Array[Byte],
    off: Int,
    len: Int
  ): Unit = {
    require(bytes.length >= len, s"given array is smaller then len (${bytes.length} < $len)")

    buffer.put(bytes, off, len)
  }
}
