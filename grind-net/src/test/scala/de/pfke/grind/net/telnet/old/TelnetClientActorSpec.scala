package de.pfke.grind.net.telnet.old

import java.net.InetAddress

import akka.actor.ActorRef
import akka.testkit.{DefaultTimeout, ImplicitSender, TestKit, TestProbe}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.duration._

class TelnetClientActorSpec
  extends TestKit(SafeActorSystem("ModelHandler_actor_Create_1String_Spec"))
    with DefaultTimeout with ImplicitSender
    with WordSpecLike with Matchers with BeforeAndAfterAll
    with MockitoSugar {
  "checking connect events" when {
    "right after start" should {
      "expect NotConnected" in {
        val actor = TelnetClientActor()
        val testProbe = TestProbe()

        actor.subscribeToConnection(testProbe.ref)

        testProbe.expectMsgType[ConnectionEvent.NotConnected]
      }
    }

    "connect to something w/o success" should {
      "expect NotConnected, Connecting, Disconnecting, NotConnected" in {
        val actor = TelnetClientActor()
        val testProbe = TestProbe()

        actor.subscribeToConnection(testProbe.ref)

        actor ! ConnectReq(TelnetClientDescr(
          InetAddress.getLocalHost,
          49999,
          cred = TelnetCredentials(),
          timeout = 1 seconds
        ))

        testProbe.expectMsgType[ConnectionEvent.NotConnected]
        testProbe.expectMsgType[ConnectionEvent.Connecting]
        testProbe.expectMsgType[ConnectionEvent.NotConnected]
      }
    }

    "connect to something w/ success" should {
      "expect NotConnected, Connecting, Connected" in {
        val serverMock = new TelnetServerMock
        val actor = TelnetClientActor()
        val testProbe = TestProbe()

        actor.subscribeToConnection(testProbe.ref)

        actor ! ConnectReq(TelnetClientDescr(
          serverMock.serverAddress,
          serverMock.serverPort,
          cred = TelnetCredentials(),
          timeout = 1 seconds
        ))

        testProbe.expectMsgType[ConnectionEvent.NotConnected]
        testProbe.expectMsgType[ConnectionEvent.Connecting]
        testProbe.expectMsgType[ConnectionEvent.Connected]
      }
    }
  }

//  "send ConnectReq to a server w/o credentials" when {
//    "using no login credentials" should {
//      "should send expected msgs" in {
//        val (serverMock, actor, testProbe) = init_n_subscribe("#")
//        actor ! ConnectReq(TelnetClientDescr(
//          serverMock.serverAddress,
//          serverMock.serverPort,
//          cred = TelnetCredentials(),
//          timeout = 30 seconds
//        ))
//
//        testProbe.expectMsgType[ConnectionEvent.NotConnected]
//        testProbe.expectMsgType[ConnectionEvent.Connecting]
//        testProbe.expectMsgType[ConnectionEvent.Connected]
//
//        actor ! PoisonPill
//      }
//    }
//  }
//
//  "send ConnectReq to a server w/ credentials (login+pwd)" when {
//    "using no login credentials" should {
//      "should run into an timeout" in {
//        val (serverMock, actor, testProbe) = init_n_subscribe("   Fritz!Box user:   ")
//        actor ! ConnectReq(TelnetClientDescr(
//          serverMock.serverAddress,
//          serverMock.serverPort,
//          cred = TelnetCredentials(),
//          timeout = 30 seconds
//        ))
//
//        testProbe.expectMsgType[ConnectionEvent.NotConnected]
//        testProbe.expectMsgType[ConnectionEvent.Connecting]
//        testProbe.expectMsgType[ConnectionEvent.Disconnecting]
//        testProbe.expectMsgType[ConnectionEvent.NotConnected]
//
//        actor ! PoisonPill
//      }
//    }
//
//    "using login credentials (login only)" should {
//      "fail, if no login is passed" in {
//        val (serverMock, actor, testProbe) = init_n_subscribe("   Fritz!Box user:   ")
//
//        serverMock
//          .when("username")
//          .respond("#")
//
//        actor ! ConnectReq(TelnetClientDescr(
//          serverMock.serverAddress,
//          serverMock.serverPort,
//          cred = TelnetCredentials(),
//          timeout = 30 seconds
//        ))
//
//        testProbe.expectMsgType[ConnectionEvent.NotConnected]
//        testProbe.expectMsgType[ConnectionEvent.Connecting]
//        testProbe.expectMsgType[ConnectionEvent.Disconnecting]
//        testProbe.expectMsgType[ConnectionEvent.NotConnected]
//
//        actor ! PoisonPill
//      }
//
//      "success, if login is passed" in {
//        val (serverMock, actor, testProbe) = init_n_subscribe("   Fritz!Box user:   ")
//
//        serverMock
//          .when("username")
//          .respond("#")
//
//        println(s"${serverMock.serverPort}")
//
//        actor ! ConnectReq(TelnetClientDescr(
//          serverMock.serverAddress,
//          serverMock.serverPort,
//          cred = TelnetCredentials(
//            user = Some("username")
//          ),
//          timeout = 30 seconds
//        ))
//
//        testProbe.expectMsgType[ConnectionEvent.NotConnected]
//        testProbe.expectMsgType[ConnectionEvent.Connecting]
//        testProbe.expectMsgType[ConnectionEvent.Connected]
//
//        actor ! PoisonPill
//      }
//    }
//  }

  private def init_n_subscribe (
    respond: String
  ): (TelnetServerMock, ActorRef, TestProbe) = {
    val serverMock = new TelnetServerMock

    serverMock
      .whenConnected()
      .respond(respond)

    val actor = TelnetClientActor()
    val testProbe = TestProbe()
    actor.subscribeToConnection(testProbe.ref)

    (serverMock, actor, testProbe)
  }
}
