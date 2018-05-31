package de.pfke.grind.net.zeromq

import java.util.concurrent.TimeoutException

import akka.util.ByteString
import org.scalatest.{Matchers, WordSpec}
import org.zeromq.ZMQ

import scala.concurrent.{Await, Promise}
import scala.concurrent.duration._

class SUBtoPUPSpec
  extends WordSpec
  with Matchers {
  "subscribe on * topic" should {
    "register for everything" in {
      implicit val ctx = ZMQ.context(1)

      val subPromise = Promise[ByteString]()
      val svr = new ServerPUP("inproc://sohi")
      val clt = new ClientSUB("inproc://sohi", onRx = { b => subPromise.success(b)})

      svr.send(ByteString("me sohi"))
      Await.result(subPromise.future, 1 seconds).decodeString("utf-8") should be("me sohi")

      clt.close()
      svr.close()
    }
  }

  "subscribe on specific topic" should {
    "receive 'me' pubs" in {
      implicit val ctx = ZMQ.context(1)

      val subPromise = Promise[ByteString]()
      val svr = new ServerPUP("inproc://sohi")
      val clt = new ClientSUB("inproc://sohi", topic = ByteString("me"), onRx = { b => subPromise.success(b) })

      svr.send(ByteString("me sohi"))
      Await.result(subPromise.future, 1 seconds).decodeString("utf-8") should be ("me sohi")

      clt.close()
      svr.close()
    }

    "not receive 'you' pubs" in {
      implicit val ctx = ZMQ.context(1)

      val subPromise = Promise[ByteString]()
      val svr = new ServerPUP("inproc://sohi")
      val clt = new ClientSUB("inproc://sohi", topic = ByteString("me"), onRx = { b => subPromise.success(b) })

      svr.send(ByteString("you sohi"))
      an[TimeoutException] should be thrownBy Await.result(subPromise.future, 500 millis)

      clt.close()
      svr.close()
    }
  }
}
