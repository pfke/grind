package de.pfke.grind.net.akkaSocket

import akka.util.{ByteIterator, ByteString}
import scala.annotation.tailrec

class FrameExtractor(
  headerLength: Int,
  lengthDecode: (ByteIterator) => Int,
  maxMsgSize: Int = Int.MaxValue
) {
  require(headerLength > 0, s"header length ($headerLength) must be > 0")
  require(headerLength <= maxMsgSize, s"header length ($headerLength) must be < maxMsgSize ($maxMsgSize)")

  // fields
  private var buffer = None: Option[ByteString]

  /**
    * Extract as many complete frames as possible from the given ByteString.
    */
  def extract(bs: Array[Byte]): List[ByteString] = extract(ByteString(bs))

  /**
    * Extract as many complete frames as possible from the given ByteString.
    */
  def extract(
    bs: ByteString
  ): List[ByteString] = {
    val data = if (buffer.isEmpty) bs else buffer.get ++ bs
    val (nb, frames) = extractFrames(data, Nil)
    buffer = nb

    frames match {
      case Nil        => Nil
      case one :: Nil => List(one)
      case many       => many.reverse.toList
    }
  }

  /**
    * Extract as many complete frames as possible from the given ByteString
    * and return the remainder together with the extracted frames in reverse
    * order.
    */
  @tailrec
  private def extractFrames(
    bs: ByteString,
    acc: List[ByteString]
  ): (Option[ByteString], Seq[ByteString]) = {
    if (bs.isEmpty) {
      (None, acc)
    } else if (bs.length < headerLength) {
      (Some(bs.compact), acc)
    } else {
      val completeLen = lengthDecode(bs.iterator)

      if (completeLen <= 0 || completeLen > maxMsgSize)
        throw new IllegalArgumentException(s"decoded frame of size $completeLen exceeds max message size (max = $maxMsgSize)")

      if (bs.length >= completeLen) {
        extractFrames(bs drop completeLen, bs.slice(0, completeLen) :: acc)
      } else {
        (Some(bs.compact), acc)
      }
    }
  }
}
