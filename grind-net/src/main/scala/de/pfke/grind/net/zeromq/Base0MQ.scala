package de.pfke.grind.net.zeromq

import akka.util.ByteString
import org.zeromq.ZMQ

abstract class Base0MQ(
  protected val socket: ZMQ.Socket
  ) {
  /**
   * Terminate this connection.
   */
  def close(): Unit = {
    socket.close()
  }

  /**
   * Receiving from socket.
   */
  protected def pleaseReceive(): ByteString = ByteString(socket.recv())

  /**
   * Send the given bytes to the remote and return received reply.
   */
  protected def pleaseSend(data: ByteString): Boolean = socket.send(data.toArray)
}
