package de.pfke.grind.net.telnet.old

import java.nio.charset.{Charset, StandardCharsets}
import java.time.{Instant, Duration => jtDuration}
import java.util.concurrent.Executors

import akka.util.ByteString

import scala.collection.mutable.ArrayBuffer

class LineExtractor (
  reportOp: (String) => Unit,
  charset: Charset = StandardCharsets.ISO_8859_1
) {
  // fields
  private val _internalRx = new EventSource[Boolean]()
  private var _queue = ByteString.empty
  private val _scheduledExecutorService = Executors.newScheduledThreadPool(1)

  // ctor
  _internalRx
    .successionEnds(
      jtDuration.ofMillis(100),
      _scheduledExecutorService,
      _scheduledExecutorService
    ) |= { _ => flushQueue() }

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

        decodedLines += str.take(idx - 1).trim
        str = str.drop(idx)
      }

      _queue = ByteString(str)
      // set timer to flush the queue
      if (_queue.nonEmpty) {
        _internalRx.push(true)
      }

      // fire all decoded lines
      decodedLines.foreach(reportOp)
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
        reportOp(_queue.decodeString(charset.name()))
      }
      _queue = ByteString.empty
    }}
}
