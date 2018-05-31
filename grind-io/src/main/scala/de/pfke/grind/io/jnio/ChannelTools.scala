package de.pfke.grind.io.jnio

import java.io.{InputStream, OutputStream}
import java.nio.ByteBuffer
import java.nio.channels.{Channels, ReadableByteChannel, WritableByteChannel}

object ChannelTools {
  /**
    * Copies the content of the given input channel into the output channel.
    * Should be very fast.
    *
    * @param input is the source channel
    * @param output is the destination channel
    * @param bytesToCopy is the size of the maximum copied bytes (leave empty and it copies max 2tb)
    * @param closeInput close input after copy
    * @param closeOutput close output after copy
    */
  def copyChannels (
    input: ReadableByteChannel,
    output: WritableByteChannel,
    bytesToCopy: Long = Long.MaxValue,
    closeInput: Boolean = false,
    closeOutput: Boolean = false
  ) {
    internalCopyChannels(input = input, output = output, bytesToCopy = bytesToCopy)

    // cleanup
    if (closeInput) { input.close() }
    if (closeOutput) { output.close() }
  }

  /**
    * Copies the content of the given input stream into the given output stream.
    *
    * @param input is the source stream
    * @param output is the destination stream
    * @param bytesToCopy is the size of the maximum copied bytes (leave empty and it copies max 2tb)
    * @tparam A is the type of the destination stream, used to return the object and be able to do method chaining
    * @param closeInput close input after copy
    * @param closeOutput close output after copy
    *
    * @return the dest stream itself
    */
  def copyStreams[A <: OutputStream] (
    input: InputStream,
    output: A,
    bytesToCopy: Long = Long.MaxValue,
    closeInput: Boolean = true,
    closeOutput: Boolean = true
  ): A = {
    copyChannels(
      input = Channels.newChannel(input),
      output = Channels.newChannel(output),
      closeInput = closeInput,
      closeOutput = closeOutput
    )

    output
  }

  /**
    * Copies the content of the src channel into the dest channel.
    * Should be very fast.
    *
    * @param input is the source channel
    * @param output is the destination channel
    * @param bytesToCopy is the size of the maximum copied bytes (leave empty and it copies max 2tb)
    */
  private def internalCopyChannels (
    input: ReadableByteChannel,
    output: WritableByteChannel,
    bytesToCopy: Long
  ) {
    val buffer = ByteBuffer.allocateDirect(math.min(bytesToCopy, 16 * 1024).toInt)
    var alreadyRead = 0L

    /**
      * Write the buffer content to the destination and keep <code>bytesToCopy</code> in mind...
      *
      * @return returns true if the is <code>bytesToCopy</code> limit reached
      */
    def flushBuffer: Boolean = {
      if (bytesToCopy <= buffer.limit()) {
        buffer.limit(math.min((bytesToCopy - alreadyRead).toInt, buffer.limit()))
      }
      alreadyRead += buffer.limit()

      // write to the channel, may block
      output.write(buffer)
      // If partial transfer, shift remainder down
      // If buffer is empty, same as doing clear()
      buffer.compact()

      if (alreadyRead >= bytesToCopy)
        return true
      false
    }

    // read source and copy
    while (input.read(buffer) != -1) {
      // prepare the buffer to be drained
      buffer.flip()

      if (flushBuffer) return
    }

    // EOF will leave buffer in fill state
    buffer.flip()
    // make sure the buffer is fully drained.
    while (buffer.hasRemaining) {
      if (flushBuffer) return
    }
  }
}
