package de.pfke.grind.core.data.byTypes.stream

import java.io.{InputStream, OutputStream}
import java.nio.ByteBuffer

import scala.annotation.tailrec
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object StreamOps {
  def pipeTo (
    in: InputStream,
    out: OutputStream,
    bufferSize: Int = 1<<10
  ): Future[Unit] = Future { pipeTo(in = in, out = out, buffer = Array.ofDim[Byte](bufferSize)) }

  @tailrec final def pipeTo (
    in: InputStream,
    out: OutputStream,
    buffer: Array[Byte]
  ): Unit = {
    in.read(buffer) match {
      case n if n > 0 =>
        println(n)
        out.write(buffer, 0, n)
        pipeTo(in = in, out = out, buffer = buffer)
      case _ =>
        in.close()
        out.close()
    }
  }

  def toByteBuffer (
    in: InputStream
  ): ByteBuffer = {
    val bb = ByteBuffer.allocate(in.available())

    in.read(bb.array())
    bb
  }
}

object StreamOpsIncludes
  extends StreamOpsIncludes

trait StreamOpsIncludes {
  implicit def StreamOpsImplicits_fromInputStreamToByteBuffer (in: InputStream): ByteBuffer = StreamOps.toByteBuffer(in = in)

  implicit class StreamOpsImplicits_fromInputStream (
    in: InputStream
  ) {
    def > (out: OutputStream): Future[Unit] = StreamOps.pipeTo(in = in, out = out)

    def pipeTo (out: OutputStream, bufferSize: Int = 1<<10): Future[Unit] = StreamOps.pipeTo(in = in, out = out, bufferSize = bufferSize)
    def pipeTo (out: OutputStream, buffer: Array[Byte]): Unit = StreamOps.pipeTo(in = in, out = out, buffer = buffer)

    def toByteBuffer: ByteBuffer = StreamOps.toByteBuffer(in = in)
  }
}
