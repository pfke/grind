package de.pfke.grind.net.zeromq

import akka.util.ByteString
import org.zeromq.ZMQ

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ClientREQ(
  adr: String
  )(implicit val _context: ZMQ.Context = ZMQ.context(1))
  extends BaseClient(_context.socket(ZMQ.REQ), adr) {
  /**
   * Send the given bytes to the remote and return received reply.
   */
  def send(
    data: ByteString
    ): Future[ByteString] = {
    Future {
      if(!pleaseSend(data)) {
        throw new RuntimeException("could not send correctly")
      }
      pleaseReceive()
    }
  }
}
