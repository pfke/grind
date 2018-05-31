package de.pfke.grind.net.telnet

import java.io.{BufferedReader, InputStreamReader, PipedInputStream, PipedOutputStream}
import java.net.{InetAddress, InetSocketAddress}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object TelnetClientRunner {
  def main (args: Array[String]): Unit = {
    val r1 = new TelnetClient(new InetSocketAddress(InetAddress.getByName("192.168.178.23"), 23))

    Thread.sleep(1000)

    val in = new PipedInputStream()
    val out = new PipedOutputStream(in)

    val so = new BufferedReader(new InputStreamReader(in))

    r1.registerSpyStream(out)

    Future {
      var line: String = null

      while ({line = so.readLine(); line} != null) {
        println(line)
      }
    }

//    println(r1.read())

//    print(r1.send("ps"))
    r1.send("ps")
    r1.send("pwd")
    r1.send("ls -al")
    r1.stopSpyStream()
    r1.send("uname -a")
    Thread.sleep(1000)
//    val r2 = r1.send("ps")
//    print(s"'${r1.send("ps")}'")
//    println(r1.read())
//
//    Thread.sleep(1000)
//    println(r1.send("pwd"))
//
    r1.disconnect()

    println()
  }
}
