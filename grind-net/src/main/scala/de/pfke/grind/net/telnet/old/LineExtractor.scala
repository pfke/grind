package de.pfke.grind.net.telnet.old

import java.nio.charset.{Charset, StandardCharsets}
import java.time.{Instant, Duration => jtDuration}
import java.util.concurrent.Executors

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.stream.scaladsl.{Framing, Keep, Sink, Source}
import akka.util.ByteString

import scala.collection.mutable.ArrayBuffer

class LineExtractor (
  reporter: (String) => Unit,
  charset: Charset = StandardCharsets.ISO_8859_1
) {
  implicit val system = ActorSystem("QuickStart")
  implicit val materializer = ActorMaterializer()

  // fields
  private var _queue = ByteString.empty
  private val _scheduledExecutorService = Executors.newScheduledThreadPool(1)

  private val _resultSource = Source.queue[String](bufferSize = 10, overflowStrategy = OverflowStrategy.dropBuffer)
    .toMat(Sink.foreach(x => reporter(x)))(Keep.left)
    .run()

  //Source
  //  .queue(bufferSize = 10, overflowStrategy = OverflowStrategy.dropHead)
  //  .via(Framing.delimiter(delimiter = ByteString("\n".getBytes(charset)), 8192, allowTruncation = true))
  //  .toMat(Sink.foreach(x => reporter(x)))(Keep.left)
  //  .run()


  // ctor
//  _internalRx
//    .successionEnds(
//      jtDuration.ofMillis(100),
//      _scheduledExecutorService,
//      _scheduledExecutorService
//    ) |= { _ => flushQueue() }

  /**
    * Decode the given bytestring
    */
  def decode(
    in: ByteString,
    timestamp: Instant = Instant.now()
  ): Unit = {
    val decodedLines = new ArrayBuffer[String]()

    synchronized {
      _queue = _queue ++ in

      var str = _queue.decodeString(charset.name())

      while (str.contains('\n')) {
        val idx = str.indexOf('\n') + 1

        _resultSource.offer(str.take(idx - 1).trim)
        str = str.drop(idx)
      }

      _queue = ByteString(str)
      // fire all decoded lines
    }
  }

  /**
    * Stop this zeuch.
    */
  def stop(): Unit = {
    _scheduledExecutorService.shutdown()
  }

  /**
    * Flush the queue.
    */
  private def flushQueue(): Unit = {
    synchronized {
      if (_queue.nonEmpty) {
        _resultSource.offer(_queue.decodeString(charset.name()))
      }
      _queue = ByteString.empty
    }}
}
