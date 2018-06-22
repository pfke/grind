package de.pfke.grind.net.telnet.old

object TelnetException {
  /**
    * Create new TelnetException
    */
  def apply (
    msg: String
  ) = new TelnetException(msg = msg)
}

class TelnetException (
  msg: String
) extends Exception(msg)
