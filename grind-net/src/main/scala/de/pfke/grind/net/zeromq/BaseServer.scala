package de.pfke.grind.net.zeromq

import org.zeromq.ZMQ

class BaseServer(
  _socket: ZMQ.Socket,
  _adr: String
  )
  extends Base0MQ(_socket) {
  // ctor
  socket.bind(_adr)
}
