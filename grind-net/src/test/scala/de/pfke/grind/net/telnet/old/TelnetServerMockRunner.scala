package de.pfke.grind.net.telnet.old

import de.pintono.grind.testing.TelnetServerMock

object TelnetServerMockRunner {
  def main (args: Array[String]): Unit = {
    val serverMock = new TelnetServerMock

    serverMock
      .whenConnected()
      .respond("hello heiko")

    serverMock
      .when("username")
      .respond("#")

    println(serverMock.serverAddress)
    println(serverMock.serverPort)

    Thread.sleep(30000)

  }
}
