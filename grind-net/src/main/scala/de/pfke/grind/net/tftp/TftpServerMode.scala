package de.pfke.grind.net.tftp

object TftpServerMode
  extends Enumeration {
  type TFTPServerMode = Value

  val GET_ONLY = Value
  val PUT_ONLY = Value
  val GET_AND_PUT = Value
}
