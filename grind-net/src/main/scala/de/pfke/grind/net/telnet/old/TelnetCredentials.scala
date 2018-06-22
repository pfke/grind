package de.pfke.grind.net.telnet.old

import scala.concurrent.duration._

case class TelnetCredentials(
  user: Option[String] = None,
  password: Option[String] = None,
  pauseAfterWrite: Duration = 10 millis
)
