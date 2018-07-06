package de.pfke.grind.net.telnet.old

import java.net.{InetAddress, InetSocketAddress}

import akka.actor.ActorSystem

import scala.concurrent.duration._
import scala.language.implicitConversions

object Runner {
  implicit val actorSystem = ActorSystem("de-test")
  implicit val connectTimeout = 5 seconds

  def main(args: Array[String]) {
///    val r1 = TelnetClient
///      .testCredentials (
///        descr = TelnetClientDescr(
///          address = new InetSocketAddress(InetAddress.getByName("192.168.178.1"), TelnetClientActor.DEFAULT_PORT),
///          cred = TelnetCredentials()
///        )
///      )
///
///    println("323232")

//    val client = TelnetClient(
//      address = InetAddress.getByName("192.168.178.11"),
//      onRx = { i => println(s">> '$i'") },
//      onTx = { i => println(s"<< '$i'") },
//      onError = { case (err,msg) => println(s"ee '$err: $msg'") }
//    )
//
//    client.errorEvents |= {
//      case Event(OnError, err) => println(s"$err")
//    }
//
//    client.runLevelEvents |= {
//      case Event(OnRunLevel, msg) => println(s"rl '$msg'")
//    }
//
////    client.trafficEvents |= {
////      case Event(OnTraffic, Rx(msg: String, _)) => println(s">> '$msg'")
////      case Event(OnTraffic, Tx(msg: String, _)) => println(s"<< '$msg'")
////
////      case Event(OnError, msg) => println(s"ee '$msg'")
////      case Event(OnRunLevel, msg) => println(s"rl '$msg'")
////    }
//
//    client.writeln("ps")
//
//
////    val f = TelnetClient.testCredentials(host = InetAddress.getByName("192.168.178.1"))
////    val f = TelnetClientActor.connect(host = InetAddress.getByName("192.168.178.1"))
////
////    f onFailure {
////      case f =>
////        println(s"fail: $f")
////    }
////
////    f onSuccess {
////      case f =>
////        println(s"succ: $f")
////    }
////
////    f onComplete {
////      case _ => actorSystem.shutdown()
////    }
  }
}
