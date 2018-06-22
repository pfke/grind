package de.pfke.grind.net.zeromq

import org.zeromq.ZMQ

class BaseClient(
  _socket: ZMQ.Socket,
  _adr: String
  )
  extends Base0MQ(_socket) {
  // ctor
  socket.connect(_adr)
}
