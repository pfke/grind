/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Heiko Blobner
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.pfke.grind.testing.net

import java.io.{BufferedReader, DataOutputStream, InputStreamReader}
import java.net.{InetAddress, ServerSocket}

import de.pfke.grind.testing.net.TelnetServerMockWhenType.TelnetServerMockWhenMode

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object TelnetServerMockWhenType
  extends Enumeration {
  type TelnetServerMockWhenMode = Value

  val CONNECTED: Value = Value
  val RESPONSE: Value = Value
}

class TelnetServerMockWhen(
  val mode: TelnetServerMockWhenMode,
  val expected: Option[String] = None
) {
  private val _respondWith = new ArrayBuffer[String]()

  private[testing] def getRespondWith: List[String] = _respondWith.toList

  def respond(data: String): Unit = {
    _respondWith += data
  }
}

class TelnetServerMock {
  // fields
  private val _server = new ServerSocket(0)
  private val _dindoutFuture = waitForConnect()
  private val _whens = new ArrayBuffer[TelnetServerMockWhen]()
  private var _isRunning = true

  // ctor
  Future {
    while (_isRunning) {
      Thread.sleep(1000)

      val read = din.readLine()

      val toSend = _whens
        .filter(_.mode == TelnetServerMockWhenType.RESPONSE)
        .filter(0 == _.expected.getOrElse("").compareTo(read))
        .flatMap(_.getRespondWith)
        .mkString("\n")
      dout.writeUTF(toSend)
    }
  }

  def server: ServerSocket = _server
  def serverAddress: InetAddress = server.getInetAddress
  def serverPort: Int = server.getLocalPort

  def din: BufferedReader = Await.result(_dindoutFuture, 10.seconds)._1
  def dout: DataOutputStream = Await.result(_dindoutFuture, 10.seconds)._2

  def stop(): Unit = {
    din.close()
    dout.close()

    _isRunning = false
  }

  def when(expected: String): TelnetServerMockWhen = {
    val when = new TelnetServerMockWhen(TelnetServerMockWhenType.RESPONSE, expected = Some(expected))

    _whens += when
    when
  }

  def whenConnected(): TelnetServerMockWhen = {
    val when = new TelnetServerMockWhen(TelnetServerMockWhenType.CONNECTED)

    _whens += when
    when
  }

  private def waitForConnect(): Future[(BufferedReader, DataOutputStream)] = {
    Future {
      val client = _server.accept()
      client.setKeepAlive(true)

      val ldin = new BufferedReader(new InputStreamReader(client.getInputStream))
      val ldout = new DataOutputStream(client.getOutputStream)

      // check whens
      val toSend = _whens
        .filter(_.mode == TelnetServerMockWhenType.CONNECTED)
        .flatMap(_.getRespondWith)
        .mkString("\n")
      ldout.writeUTF(toSend)

      (ldin, ldout)
    }
  }
}
