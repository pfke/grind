package de.pfke.grind.net.tftp

import java.io._
import java.net.SocketTimeoutException
import java.nio.file.Path

import org.apache.commons.net.io.{FromNetASCIIOutputStream, ToNetASCIIInputStream}
import org.apache.commons.net.tftp._

import scala.concurrent.{duration => sDur}
import scala.util.control.Breaks._

//class TftpTransfer (
//  server: TftpServer,
//  tftpPacket: TFTPPacket,
//  reportFileRead: (Path, ByteLength, sDur.Duration) => Unit,
//  reportFileWrite: (Path, ByteLength, sDur.Duration) => Unit
//)
//  extends Runnable
//    with Logging {
//  // fields
//  private var shutdownTransfer = false
//  private val transferTftp = new TFTP()
//
//  debug(s"new transfer ($tftpPacket) created")
//
//  /**
//    * Utility method to make sure that paths provided by tftp clients do not get outside of the
//    * serverRoot directory.
//    *
//    * @param directory directory with the file in it
//    * @param fileName new or existing file name
//    * @param createFile ensure directory exists (if requested)
//    * @return
//    */
//  private def buildSafeFile(
//    directory: Path,
//    fileName: String,
//    createFile: Boolean): Path = {
//    val temp = directory.resolve(fileName)
//
//    if (createFile) {
//      temp.createFile()
//    }
//
//    temp
//  }
//
//  /**
//    * Handle a tftp read request.
//    *
//    * @param trrp tftp request packet
//    */
//  private def handleRead(
//    trrp: TFTPReadRequestPacket
//  ) {
//    var is: InputStream = null
//    val file = buildSafeFile(server.localDirectory, trrp.getFilename, createFile = false)
//
//    debug(s"read request: ${trrp.getFilename}")
//
//    val startTime = System.currentTimeMillis()
//
//    try {
//      if (server.serverMode == TftpServerMode.PUT_ONLY) {
//        sendError(trrp, TFTPErrorPacket.ILLEGAL_OPERATION, "Read not allowed by server.")
//        return
//      }
//
//      try {
//        is = new BufferedInputStream(file.inputStream)
//      } catch {
//        case e: FileNotFoundException =>
//          sendError(trrp, TFTPErrorPacket.FILE_NOT_FOUND, e.getMessage)
//          return
//      }
//    } catch {
//      case e: Exception =>
//        sendError(trrp, TFTPErrorPacket.UNDEFINED, e.getMessage)
//        return
//    }
//
//    if (trrp.getMode == TFTP.NETASCII_MODE) {
//      is = new ToNetASCIIInputStream(is)
//    }
//
//    val temp = new Array[Byte](TFTPDataPacket.MAX_DATA_LENGTH)
//    var answer: TFTPPacket = null
//    var block = 1
//    var sendNext = true
//    var readLength = TFTPDataPacket.MAX_DATA_LENGTH
//    var lastSentData: TFTPDataPacket = null
//
//    breakable {
//      // We are reading a file, so when we read less than the
//      // requested bytes, we know that we are at the end of the file.
//      while (readLength == TFTPDataPacket.MAX_DATA_LENGTH && !shutdownTransfer) {
//        if (sendNext) {
//          readLength = is.read(temp)
//          if (readLength == -1) {
//            readLength = 0
//          }
//
//          lastSentData = new TFTPDataPacket(trrp.getAddress, trrp.getPort, block, temp, 0, readLength)
//          transferTftp.bufferedSend(lastSentData)
//        }
//
//        answer = null
//
//        var timeoutCount = 0
//
//        while (    !shutdownTransfer
//          && (   answer == null
//          || !answer.getAddress.equals(trrp.getAddress)
//          || (answer.getPort != trrp.getPort))) {
//          // listen for an answer.
//          if (answer != null) {
//            // The answer that we got didn't come from the
//            // expected source, fire back an error, and continue
//            // listening.
//            error("TFTP Server ignoring message from unexpected source.")
//
//            sendError(trrp, TFTPErrorPacket.UNKNOWN_TID, "Unexpected Host or Port")
//          }
//
//          try {
//            answer = transferTftp.bufferedReceive()
//          } catch {
//            case e: SocketTimeoutException =>
//              if (timeoutCount >= server.maxTimeoutRetries){
//                throw e
//              }
//              // didn't get an ack for this data. need to resend
//              // it.
//              timeoutCount += 1
//              transferTftp.bufferedSend(lastSentData)
//          }
//        }
//
//        if (answer == null || !answer.isInstanceOf[TFTPAckPacket]) {
//          if (!shutdownTransfer) {
//            error("Unexpected response from tftp client during transfer (" + answer + ").  Transfer aborted.")
//          }
//          break()
//        } else {
//          // once we get here, we know we have an answer packet
//          // from the correct host.
//          val ack = answer.asInstanceOf[TFTPAckPacket]
//          if (ack.getBlockNumber != block) {
//            /*
//             * The origional tftp spec would have called on us to resend the
//             * previous data here, however, that causes the SAS Syndrome.
//             * http://www.faqs.org/rfcs/rfc1123.html section 4.2.3.1 The modified
//             * spec says that we ignore a duplicate ack. If the packet was really
//             * lost, we will time out on receive, and resend the previous data at
//             * that point.
//             */
//            sendNext = false
//          } else {
//            // send the next block
//            block += 1
//            if (block > 65535) {
//              // wrap the block number
//              block = 0
//            }
//            sendNext = true
//          }
//        }
//      }
//    }
//
//    try {
//      if (is != null) {
//        is.close()
//      }
//    } catch {
//      case e: IOException => // noop
//    }
//
//    printTransferStats(file, startTime, "read")
//  }
//
//  /**
//    * Handle a tftp write request.
//    *
//    * @param twrp tftp request packet
//    */
//  private def handleWrite(
//    twrp: TFTPWriteRequestPacket
//  ) {
//    var bos: OutputStream = null
//    val file = buildSafeFile(server.localDirectory, twrp.getFilename, createFile = true)
//
//    debug(s"write request: ${twrp.getFilename}")
//
//    val startTime = System.currentTimeMillis()
//
//    try {
//      if (server.serverMode == TftpServerMode.GET_ONLY) {
//        sendError(twrp, TFTPErrorPacket.ILLEGAL_OPERATION, "Write not allowed by server.")
//        return
//      }
//
//      var lastBlock = 0
//
//      try {
//        if(!file.exists)
//          file.createFile()
//        bos = new BufferedOutputStream(file.outputStream)
//
//        if (twrp.getMode == TFTP.NETASCII_MODE) {
//          bos = new FromNetASCIIOutputStream(bos)
//        }
//      } catch {
//        case e: Exception =>
//          sendError(twrp, TFTPErrorPacket.UNDEFINED, e.getMessage)
//          return
//      }
//
//      var lastSentAck = new TFTPAckPacket(twrp.getAddress, twrp.getPort, 0)
//      transferTftp.bufferedSend(lastSentAck)
//
//      breakable {
//        while (true) {
//          // get the response - ensure it is from the right place.
//          var dataPacket: TFTPPacket = null
//          var timeoutCount = 0
//
//          while (!shutdownTransfer
//            && (dataPacket == null
//            || !dataPacket.getAddress.equals(twrp.getAddress)
//            || dataPacket.getPort != twrp.getPort)) {
//            // listen for an answer.
//            if (dataPacket != null) {
//              // The data that we got didn't come from the
//              // expected source, fire back an error, and continue
//              // listening.
//              error("TFTP Server ignoring message from unexpected source.")
//              sendError(twrp, TFTPErrorPacket.UNKNOWN_TID, "Unexpected Host or Port")
//            }
//
//            try {
//              dataPacket = transferTftp.bufferedReceive()
//            } catch {
//              case e: SocketTimeoutException =>
//                if (timeoutCount >= server.maxTimeoutRetries) {
//                  throw e
//                }
//                // It didn't get our ack. Resend it.
//                transferTftp.bufferedSend(lastSentAck)
//                timeoutCount += 1
//            }
//          }
//
//          if (dataPacket != null && dataPacket.isInstanceOf[TFTPWriteRequestPacket]) {
//            // it must have missed our initial ack. Send another.
//            lastSentAck = new TFTPAckPacket(twrp.getAddress, twrp.getPort, 0)
//            transferTftp.bufferedSend(lastSentAck)
//          } else if (dataPacket == null || !dataPacket.isInstanceOf[TFTPDataPacket]) {
//            if (!shutdownTransfer) {
//              error("Unexpected response from tftp client during transfer (" + dataPacket + ").  Transfer aborted.")
//            }
//            break()
//          } else {
//            val block = dataPacket.asInstanceOf[TFTPDataPacket].getBlockNumber
//            val data = dataPacket.asInstanceOf[TFTPDataPacket].getData
//            val dataLength = dataPacket.asInstanceOf[TFTPDataPacket].getDataLength
//            val dataOffset = dataPacket.asInstanceOf[TFTPDataPacket].getDataOffset
//
//            if (block > lastBlock || (lastBlock == 65535 && block == 0)) {
//              // it might resend a data block if it missed our ack
//              // - don't rewrite the block.
//              bos.write(data, dataOffset, dataLength)
//              lastBlock = block
//            }
//
//            lastSentAck = new TFTPAckPacket(twrp.getAddress, twrp.getPort, block)
//            transferTftp.bufferedSend(lastSentAck)
//            if (dataLength < TFTPDataPacket.MAX_DATA_LENGTH) {
//              // end of stream signal - The tranfer is complete.
//              bos.close()
//              bos = null
//
//              // But my ack may be lost - so listen to see if I
//              // need to resend the ack.
//              for(i <- 0 until server.maxTimeoutRetries) {i: Int=>
//                try {
//                  dataPacket = transferTftp.bufferedReceive()
//                } catch {
//                  case e: SocketTimeoutException =>
//                    // this is the expected route - the client
//                    // shouldn't be sending any more packets.
//                    break()
//                }
//
//                if (    dataPacket != null
//                  && (   !dataPacket.getAddress.equals(twrp.getAddress)
//                  || dataPacket.getPort != twrp.getPort)) {
//                  // make sure it was from the right client...
//                  sendError(twrp, TFTPErrorPacket.UNKNOWN_TID, "Unexpected Host or Port")
//                } else {
//                  // This means they sent us the last
//                  // datapacket again, must have missed our
//                  // ack. resend it.
//                  transferTftp.bufferedSend(lastSentAck)
//                }
//              }
//
//              // all done.
//              break()
//            }
//          }
//        }
//      }
//    } finally {
//      if (bos != null) {
//        bos.close()
//      }
//    }
//
//    printTransferStats(file, startTime, "write")
//  }
//
//  /**
//    * Thread run method.
//    */
//  def run() {
//    try {
//      transferTftp.beginBufferedOps()
//      transferTftp.setDefaultTimeout(server.socketTimeout)
//
//      transferTftp.open()
//
//      tftpPacket match {
//        case n: TFTPReadRequestPacket => handleRead(n)
//        case n: TFTPWriteRequestPacket => handleWrite(n)
//
//        case _ => error(s"Unsupported TFTP request ($tftpPacket) ignored.")
//      }
//    } catch {
//      case e: Exception => if (!shutdownTransfer) {
//        error(s"Unexpected Error in during TFTP file transfer. Transfer aborted. ($e)")
//      }
//    } finally {
//      try {
//        if(transferTftp != null && transferTftp.isOpen) {
//          transferTftp.endBufferedOps()
//          transferTftp.close()
//        }
//      } catch {
//        case e: Exception => // ignore
//      }
//      server.remove(this)
//    }
//  }
//
//  /**
//    * Print transfer stats to log.debug.
//    *
//    * @param file is the file itself
//    * @param startTimeMillis transfer start time in milli seconds
//    * @param dbgString "read" or "write"
//    */
//  private def printTransferStats(
//    file: Path,
//    startTimeMillis: Long,
//    dbgString: String
//  ) {
//    val duration = math.max(1, System.currentTimeMillis() - startTimeMillis)
//    val fileSize = math.max(1, file.size.toDouble)
//
//    debug(
//      "%s finished in %d,%ds for %s => %.2f kB/s".format(
//        dbgString,
//        duration / 1000,
//        duration % 1000,
//        fileSize,
//        fileSize/duration
//      ))
//
//    dbgString match {
//      case "read"  => reportFileRead (file, fileSize.byte, sDur.Duration.apply(duration, sDur.MILLISECONDS))
//      case "write" => reportFileWrite(file, fileSize.byte, sDur.Duration.apply(duration, sDur.MILLISECONDS))
//    }
//  }
//
//  /**
//    * Send error packet to remote.
//    *
//    * @param trrp initial request packet
//    * @param err error code
//    * @param errStr error string
//    */
//  private def sendError(
//    trrp: TFTPPacket,
//    err: Int,
//    errStr: String
//  ) {
//    transferTftp.bufferedSend(
//      new TFTPErrorPacket(
//        trrp.getAddress,
//        trrp.getPort,
//        err,
//        errStr))
//  }
//
//  /**
//    * Shut this transfer down.
//    */
//  def shutdown() {
//    shutdownTransfer = true
//
//    try {
//      transferTftp.close()
//    } catch {
//      case e: RuntimeException => // ignore
//    }
//  }
//}
