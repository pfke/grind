package de.pfke.grind.net.telnet.old

object TelnetError
  extends Enumeration {
  type TelnetError = Value

  /**
    * Is in fail state.
    */
  val FAIL_STATE = Value

  /**
    * Some error while in normal phase.
    */
  val ERR_WHILE_CONNECT = Value

  /**
    * Some error while in normal operation.
    */
  val ERR_WHILE_NORMAL_OP = Value

  /**
    * Login failed.
    */
  val LOGIN_FAILED = Value

  /**
    * Telnet is not connected yet. => msg drop
    */
  val NOT_CONNECTED = Value

  /**
    * This msg is unhandled in this state.
    */
  val UNHANDLED = Value

  /**
    * Unknown msg received.
    */
  val UNKNOWN_MSG = Value

  /**
    * Unable to connect to remote.
    */
  val CONNECT_TIMEOUT = Value

  val CONNECT_SOCKET_EXCEPTION = Value
  val CONNECT_IO_EXCEPTION = Value
}
