package de.pfke.grind.net.tftp

import java.net.{InetAddress, InetSocketAddress}

import de.pintono.grind.io._
import de.pintono.grind.data._
import de.pintono.grind.core.crypto.ChecksumAlgorithm
import de.pintono.grind.core.event.{GrindEvent, TrafficEvent}
import de.pintono.grind.core.event.TrafficEvent.Rx
import de.pintono.grind.core.logging.ConfigureLogger
import de.pintono.grind.net.tftp.TftpServer.TftpFileTransmit
import org.apache.commons.net.tftp.TFTP
import rx.lang.scala.Subscriber

object Runner {
  def main(args: Array[String]) {
    ConfigureLogger.addConsole()

    val tftpServer = new TftpServer()
    tftpServer
//      .observable[GrindEvent]
//      .subscribe( n => println("r0: '" + n + "'"))
      .observable
      .subscribe{ n: Rx => println("r0: '" + n + "'")}

    tftpServer
      .registerObserver(
        Subscriber{n: Rx =>
          n.data match {
            case TftpFileTransmit(file, size, dur, speed) => println(file.checksum(ChecksumAlgorithm.MD5).toHex)
            case _ =>
          }
          println("ra1: '" + n.data + "'")
        }
    )

    val tftpClient = new TftpClient(new InetSocketAddress(InetAddress.getByName(tftpServer.localAddress.getHostName), tftpServer.localPort))

    val r1 = "_1st.file".createTempFile().write("hello heiko".asByteBuffer_iso8859_1).toString
    println(r1.asPath.checksum(ChecksumAlgorithm.MD5).toHex)

    tftpClient.send(
      localFilename = "_1st.file".createTempFile().write("hello heiko".asByteBuffer_iso8859_1).toString,
      remoteFilename = "_rem_1st.file"
    )


    Thread.sleep(10000)

    tftpServer.shutdown()


//    val r1 = NetUtils.getAllLocalIPAddresses.filter(NetUtils.ipv4Filter)
//    val r2 = NetUtils.getAllLocalIPAddresses.filter(NetUtils.matchAddress("192.168.178"))
//
//    val r3 = NetUtils.findMatchingPart("192.168.178.1", "192.168.178.20")
//    val r4 = NetUtils.findBestMatchingAsCIDR(NetUtils.getAllLocalIPAddresses, "192.168.178.1")
//    val r5 = r1.filter(NetUtils.matchCIDR(r4.get))
//
//    val t1 = NetUtils
//      .getAllLocalIPAddresses
//      .filter(NetUtils.ipv4Filter)
//      .filter(NetUtils.matchCIDR(r4.get))
//
//
//    ConfigureLogger.addConsole()
//
//    val tftp = TftpServer()
//    tftp.runLevelEvents |= {
//      case Event(OnRunLevel, p) => println(p)
//    }
//    tftp.trafficEvents |= {
//      case Event(OnTraffic, Rx(e: TftpFileTransmit, _))  => println(e)
////      case e@_ => println(s"<< $e")
//    }
////    tftp.trafficEvents |= {
////      e => println(s"<< $e")
////    }
//
//    println(s"address: ${tftp.localAddress}")
//    println(s"port: ${tftp.localPort}")
//
//    Thread.sleep(300000)
  }
}
