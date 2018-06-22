package de.pfke.grind.net.telnet.old

import java.io._
import java.net.{InetAddress, InetSocketAddress, ServerSocket}

import akka.actor.ActorSystem

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

object Gegenpart {
  def main (args: Array[String]): Unit = {
    val serverAddress = new InetSocketAddress(InetAddress.getByName("localhost"), 65456)
    val ret: Future[(DataInputStream, DataOutputStream)] = Future {
      val server = new ServerSocket(65456)
//      server.bind(serverAddress)

      val client = server.accept()

      println(990)
        new DataInputStream(client.getInputStream)
      println(32332)
      (
        new DataInputStream(client.getInputStream),
        new DataOutputStream(client.getOutputStream)
      )
    }

    implicit val actorSystem = ActorSystem()
    val actor = TelnetClientActor()
//    Thread.sleep(1000)
    actor ! ConnectReq(TelnetClientDescr(
      serverAddress.getAddress,
//      server.getInetAddress,
      serverAddress.getPort,
//      server.getLocalPort,
      cred = TelnetCredentials(),
      timeout = 30 seconds
    ))

    println("fferfrefer")

    val (ret1, ret2) = ret.await
    println("kjlh")

    ret2.writeUTF("Hello World")


    //val tn = TelnetClient(
    //  host = InetAddress.getByName("192.168.178.1"),
    //  port = TelnetClientActorOld.DEFAULT_PORT,
    //  cred = TelnetCredentials(),
    //  timeout = 1 seconds
    //)
    //
    //
    //println("2222e")
    //
    //tn.stop()

    //val telnet = new Socket("192.168.178.1", 23)
    //val in = new DataInputStream(telnet.getInputStream)
    //val out = new DataOutputStream(telnet.getOutputStream)
    //
    //out.writeUTF("klö\n")
    //val s= in.readUTF()
    //println(s)
  }
}
