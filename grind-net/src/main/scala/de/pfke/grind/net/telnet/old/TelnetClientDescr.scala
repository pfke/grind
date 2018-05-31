package de.pfke.grind.net.telnet.old

import java.net.{InetAddress, InetSocketAddress}

import akka.actor.ActorSystem

import scala.concurrent.duration._

object TelnetClientDescr {
  def apply (
    host: InetAddress,
    port: Int,
    cred: TelnetCredentials,
    timeout: FiniteDuration
  ) (
    implicit
    actorSystem: ActorSystem
  ): TelnetClientDescr = TelnetClientDescr(address = new InetSocketAddress(host, port), cred = cred, timeout = timeout)
}

case class TelnetClientDescr (
  address: InetSocketAddress,
  cred: TelnetCredentials,
  timeout: FiniteDuration = 30 seconds
)
