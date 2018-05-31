package de.pfke.grind.net.zeromq

import akka.util.ByteString
import org.scalatest.{Matchers, WordSpec}
import org.zeromq.ZMQ

import scala.concurrent.Await
import scala.concurrent.duration._

class REQtoREPSpec
  extends WordSpec
  with Matchers {
  "call send and return Future" should {
    "reply correct value" in {
      implicit val ctx = ZMQ.context(1)

      val svr = new ServerREP("inproc://sohi", onRx = { b => ByteString("klölö") })
      val clr = new ClientREQ("inproc://sohi")

      Await.result(clr.send(ByteString("d")), 1 second).decodeString("utf-8") should be ("klölö")

      svr.close()
    }
  }
}
