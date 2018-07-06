package de.pfke.grind.net.zeromq

import java.util.UUID

import akka.util.ByteString
import org.zeromq.ZMQ
import org.zeromq.ZMQ.Poller

import scala.concurrent.duration._
import scala.concurrent.{Await, Future, Promise}

//class ServerREP(
//  adr: String,
//  onRx: (ByteString) => ByteString
//  ) (
//  implicit
//  val _context: ZMQ.Context = ZMQ.context(1)
//)
//  extends BaseServer(_context.socket(ZMQ.REP), adr)
//{
//  // fields
//  private val _poller = new Poller(2)
//  private val _shutdownID = UUID.randomUUID()
//  private val _shutdownClt = _context.socket(ZMQ.PAIR)
//  private val _shutdownSvr = _context.socket(ZMQ.PAIR)
//  private val _isDownPromise = Promise[Boolean]()
//
//  // ctor
//  _shutdownSvr.bind(s"inproc://${_shutdownID}")
//  _shutdownClt.connect(s"inproc://${_shutdownID}")
//
//  _poller.register(socket)
//  _poller.register(_shutdownSvr)
//
//  rxLoop()
//
//  /**
//   * Terminate this connection.
//   */
//  override def close() = {
//    _shutdownClt.send("CLOSE")
//
//    Await.result(_isDownPromise.future, 1 second)
//    super.close()
//  }
//
//  /**
//   * This is our main rx loop.
//   */
//  private def rxLoop() = {
//    var isRunning = true
//
//    Future {
//      while(isRunning) {
//        _poller.poll()
//
//        if (_poller.pollin(0)) {
//          pleaseSend(onRx(pleaseReceive()))
//        }
//
//        if (_poller.pollin(1)) {
//          isRunning = false
//        }
//      }
//      _isDownPromise.success(true)
//    }
//  }
//}
