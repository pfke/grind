package de.pfke.grind.net.zeromq

import java.util.UUID

import akka.util.ByteString
import de.pintono.grind.core.async.DoItLater
import org.zeromq.ZMQ
import org.zeromq.ZMQ.Poller

import scala.concurrent.{Await, Promise}
import scala.concurrent.duration._

class ClientSUB(
  adr: String,
  topic: ByteString = ByteString.empty,
  onRx: (ByteString) => Any
  )(implicit val _context: ZMQ.Context = ZMQ.context(1))
  extends BaseClient(_context.socket(ZMQ.SUB), adr)
  with DoItLater {
  // fields
  private val _poller = new Poller(2)
  private val _shutdownID = UUID.randomUUID()
  private val _shutdownClt = _context.socket(ZMQ.PAIR)
  private val _shutdownSvr = _context.socket(ZMQ.PAIR)
  private val _isDownPromise = Promise[Boolean]()

  // ctor
  _shutdownSvr.bind(s"inproc://${_shutdownID}")
  _shutdownClt.connect(s"inproc://${_shutdownID}")

  socket.subscribe(topic.toArray)

  _poller.register(socket)
  _poller.register(_shutdownSvr)

  rxLoop()

  /**
   * Terminate this connection.
   */
  override def close() = {
    _shutdownClt.send("CLOSE")

    Await.result(_isDownPromise.future, 1 second)
    super.close()
  }

  /**
   * This is our main rx loop.
   */
  private def rxLoop() = {
    var isRunning = true

    doIt {
      while(isRunning) {
        _poller.poll()

        if (_poller.pollin(0)) {
          onRx(pleaseReceive())
        }

        if (_poller.pollin(1)) {
          isRunning = false
        }
      }
      _isDownPromise.success(true)
    }
  }
}
