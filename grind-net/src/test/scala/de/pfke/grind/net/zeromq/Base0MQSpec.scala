package de.pfke.grind.net.zeromq

import akka.util.ByteString
import org.scalatest.{Matchers, WordSpec}
import org.zeromq.ZMQ
import org.zeromq.ZMQ.Context

class Base0MQSpec
  extends WordSpec
  with Matchers {
  class ThisClient(
    context: Context,
    socketType: Int,
    adr: String
    )
    extends Base0MQ(context.socket(socketType)) {
    // ctor
    socket.connect(adr)

    def receive: ByteString = pleaseReceive()
    def send(data: ByteString) = pleaseSend(data)
  }
  class ThisServer(
    context: Context,
    socketType: Int,
    adr: String
    )
    extends Base0MQ(context.socket(socketType)) {
    // ctor
    socket.bind(adr)

    def receive: ByteString = pleaseReceive()
    def send(data: ByteString) = pleaseSend(data)
  }

  def createREQ_REP(): (ThisServer, ThisClient) = {
    val ctx = ZMQ.context(1)

    (new ThisServer(ctx, ZMQ.REP, "inproc://sohi"), new ThisClient(ctx, ZMQ.REQ, "inproc://sohi"))
  }

  "using as REQ->REP" should {
    "tx REQ" in {
      val (svr, clt) = createREQ_REP()

      clt.send(ByteString("hello ")) should be (right = true)

      clt.close()
      svr.close()
    }

    "rx REP" in {
      val (svr, clt) = createREQ_REP()

      clt.send(ByteString("hello ")) should be (right = true)
      svr.receive.decodeString("utf-8") should be ("hello ")

      clt.close()
      svr.close()
    }

    "tx REP" in {
      val (svr, clt) = createREQ_REP()

      clt.send(ByteString("hello ")) should be (right = true)
      svr.receive.decodeString("utf-8") should be ("hello ")
      svr.send(ByteString("hello heiko")) should be (right = true)

      clt.close()
      svr.close()
    }

    "rx REQ" in {
      val (svr, clt) = createREQ_REP()

      clt.send(ByteString("hello ")) should be (right = true)
      svr.receive.decodeString("utf-8") should be ("hello ")
      svr.send(ByteString("hello heiko")) should be (right = true)
      clt.receive.decodeString("utf-8") should be ("hello heiko")

      clt.close()
      svr.close()
    }
  }
}
