package de.pfke.grind.net.zeromq

import akka.util.ByteString
import org.zeromq.ZMQ

class ServerPUP(
  adr: String
  )(implicit val _context: ZMQ.Context = ZMQ.context(1))
  extends BaseServer(_context.socket(ZMQ.PUB), adr) {
  /**
   * Send the given bytes.
   */
  def send(data: ByteString): Boolean = send(topic = ByteString.empty, data = data)

  /**
   * Send the given bytes.
   */
  def send(
    topic: ByteString,
    data: ByteString
    ): Boolean = pleaseSend(data)
}
