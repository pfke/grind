package de.pfke.grind.net.telnet

import java.io._
import java.net.{InetAddress, InetSocketAddress}
import java.nio.ByteBuffer
import java.nio.charset.{Charset, StandardCharsets}

import akka.util.ByteString
import de.pintono.grind.core.akka.eventBus.GrindCallbackEventBus
import de.pintono.grind.core.akka.eventBus.provider.{ConnectionEventProvider, TrafficEventProvider}
import org.apache.commons.net.{telnet => at}

object TelnetClient {
  def apply (
    address: InetSocketAddress,
    charset: Charset = StandardCharsets.ISO_8859_1
  ): TelnetClient = new TelnetClient(address = address, charset = charset)

  def byAddress (
    address: InetAddress,
    port: Int = 23,
    charset: Charset = StandardCharsets.ISO_8859_1
  ): TelnetClient = apply(address = new InetSocketAddress(address, port), charset = charset)
}

class TelnetClient (
  address: InetSocketAddress,
  charset: Charset = StandardCharsets.ISO_8859_1
)
//  extends GrindCallbackEventBus
//    with ConnectionEventProvider
//    with TrafficEventProvider
{
  // fields
  private val _buffer = Array.ofDim[Byte](1024 * 1024)
  private val (_telnetClient, _inStream, _outStream) = connect()

  /**
    * Verbindung beenden und so...
    */
  def disconnect (): Boolean = {
    try {
      _telnetClient.disconnect()

      true
    } catch {
      case _: IOException => false
    }
  }

  /**
    * Auslesen des Stream und Rueckgabe.
    */
  def read () (
    implicit
    charset: Charset = StandardCharsets.ISO_8859_1
  ): String = {
    val inBuffer = synchronized {
      val read_bytes = _inStream.read(_buffer)

      ByteBuffer
        .allocate(read_bytes)
        .put(_buffer, 0, read_bytes)
    }
    
    ByteString(inBuffer).decodeString(charset)
  }

  /**
    * Reads input stream until the given pattern is reached. The
    * pattern is NOT discarded and what was read up to the pattern is
    * returned.
    */
  def readTo (
    pattern: String
  ): Option[String] = {
    val lastChar = pattern.charAt(pattern.length() - 1)
    val sb = new StringBuilder()
    var read_char = 0

    while ({ read_char = _inStream.read(); read_char } != -1) {
      val ch = read_char.toChar

      sb.append(ch)
      
      if(ch == lastChar) {
        val str = sb.toString()

        if(str.endsWith(pattern)) {
          return Some(str.substring(0, str.length()))
        }
      }
    }

    None
  }

  /**
    * Reads input stream until the given pattern is reached. The
    * pattern is discarded and what was read up until the pattern is
    * returned.
    */
  def readUntil (
    pattern: String
  ): Option[String] = {
    readTo(pattern) match {
      case Some(x) => Some(x.dropRight(pattern.length))
      case None => None
    }
  }

  def registerSpyStream (
    spyStream: OutputStream
  ): Unit = _telnetClient.registerSpyStream(spyStream)

  def stopSpyStream (): Unit = _telnetClient.stopSpyStream()

  /**
    * Send string and return answer
    */
  def send (
    command: String,
    readToPattern: String = "#"
  ): Option[String] = {
    try {
      write(command)

      readTo(readToPattern)
    } catch {
      case _: IOException => None
    }
  }

  /**
    * Send string, do not return the answer
    */
  def write (
    in: String
  ) (
    implicit
    charset: Charset = StandardCharsets.ISO_8859_1
  ): Unit = {
    _outStream.println(in)
    _outStream.flush()
  }

  /**
    * Internal function to connect to dingens.
    */
  private def connect(): (at.TelnetClient, InputStream, PrintStream) = {
    val tc = new at.TelnetClient()

    tc.connect(address.getAddress, address.getPort)

    val in = tc.getInputStream
    val out = new PrintStream(tc.getOutputStream)

    (tc, in, out)
  }
}
