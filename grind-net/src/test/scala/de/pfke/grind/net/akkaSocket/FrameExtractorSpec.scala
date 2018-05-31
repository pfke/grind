package de.pfke.grind.net.akkaSocket

import akka.util.{ByteIterator, ByteString}
import de.pfke.grind.net.akkaSocket.FrameExtractor
import org.scalatest.{Matchers, WordSpec}

class FrameExtractorSpec
  extends WordSpec
  with Matchers {
  "a frame extractor" when {
    "freshly instantiated" should {
      "throw an exception if the passed headerLength is 0" in {
        an[IllegalArgumentException] should be thrownBy {
          new FrameExtractor(headerLength = 0, lengthDecode = zeroReturningDecoder)
        }
      }

      "throw an exception if the passed maxMsgSize < headerLength" in {
        an[IllegalArgumentException] should be thrownBy {
          new FrameExtractor(headerLength = 9, lengthDecode = zeroReturningDecoder, maxMsgSize = 8)
        }
      }

      "not throw an exception if the passed maxMsgSize <= headerLength" in {
        new FrameExtractor(headerLength = 8, lengthDecode = zeroReturningDecoder, maxMsgSize = 8)
      }
    }

    "fed with exactly one message" should {
      val msg = ByteString(8,2,3,4,5,6,7,8)

      "throw an exception when lengthDecode returns 0" in {
        an[IllegalArgumentException] should be thrownBy {
          new FrameExtractor(headerLength = 8, lengthDecode = zeroReturningDecoder, maxMsgSize = 8).extract(msg)
        }
      }
      "throw an exception when decoded msg length is > maxMsgSize" in {
        an[IllegalArgumentException] should be thrownBy {
          new FrameExtractor(headerLength = 1, lengthDecode = firstByteReturningDecoder, maxMsgSize = 2).extract(msg)
        }
      }
      "return an empty list if passed msg is not complete" in {
        new FrameExtractor(headerLength = 1, lengthDecode = firstByteReturningDecoder, maxMsgSize = 20).extract(msg.slice(0, 3)) should be(List.empty)
      }
      "return the passed bytestring" in {
        new FrameExtractor(headerLength = 1, lengthDecode = firstByteReturningDecoder, maxMsgSize = 20).extract(msg) should be(List(msg))
      }
    }

    "feed with 2 message" should {
      val msg = ByteString(3,2,3,5,5,6,7,8)

      "return the 2 messages, if passed the complete msg" in {
        new FrameExtractor(headerLength = 1, lengthDecode = firstByteReturningDecoder, maxMsgSize = 20).extract(msg).length should be(2)
      }
      "return the 2 messages, if passed the complete msg (check msg #1)" in {
        new FrameExtractor(headerLength = 1, lengthDecode = firstByteReturningDecoder, maxMsgSize = 20).extract(msg)(0) should be(msg.slice(0, 3))
      }
      "return the 2 messages, if passed the complete msg (check msg #2)" in {
        new FrameExtractor(headerLength = 1, lengthDecode = firstByteReturningDecoder, maxMsgSize = 20).extract(msg)(1) should be(msg.slice(3, 8))
      }

      "return the 1 messages, if passed the incomplete msg" in {
        new FrameExtractor(headerLength = 1, lengthDecode = firstByteReturningDecoder, maxMsgSize = 20).extract(msg.slice(0, 7)).length should be(1)
      }
      "return the 1 messages, if passed the incomplete msg (check msg #1)" in {
        new FrameExtractor(headerLength = 1, lengthDecode = firstByteReturningDecoder, maxMsgSize = 20).extract(msg.slice(0, 7))(0) should be(msg.slice(0, 3))
      }
    }
  }

  private def zeroReturningDecoder(bi: ByteIterator): Int = 0
  private def firstByteReturningDecoder(bi: ByteIterator): Int = bi.getByte.toInt
}
