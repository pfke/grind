package de.pfke.grind.net.tftp

import java.io._
import java.net.{InetSocketAddress, SocketException, UnknownHostException}

import org.apache.commons.net.tftp.{TFTP, TFTPClient}

class TftpClient (
  host: InetSocketAddress,
  transferMode: Int = TFTP.BINARY_MODE
) {
  // fields
  private val tftpClient = new TFTPClient

  /**
    *
    * @param localFilename
    * @param remoteFilename
    * @return
    *
    * @throws IOException could not open local file for reading
    * @throws IOException I/O exception occurred while sending file
    * @throws IOException error closing local file
    * @throws UnknownHostException if the hostname could not be resolved
    */
  def send(
    localFilename: String,
    remoteFilename: String
  ): Unit = {
    // Try to open local file for reading
    val input = try {
      new FileInputStream(localFilename)
    } catch {
      case e: IOException =>
        tftpClient.close()
        throw e
    }

    // open client
    openTftpClient()

    // Try to send local file via TFTP
    try {
      tftpClient.sendFile(remoteFilename, transferMode, input, host.getHostName, host.getPort)
    } catch {
      case e: UnknownHostException => throw e
      case e: IOException => throw e
    } finally {
      // Close local socket and input file
      closeTftpClient()
      input.close()
    }
  }

  def receive (
    localFilename: String,
    remoteFilename: String,
    overrideRemote: Boolean = false
  ): Unit = {
    val file: File = new File(localFilename)

    // If file exists, don't overwrite it.
    if (file.exists() && !overrideRemote) {
      throw new IOException(s"$localFilename already exists.")
    }

    // Try to open local file for writing
    val output = try {
      new FileOutputStream(file)
    } catch {
      case e: IOException =>
        tftpClient.close()
        throw e
    }

    // open client
    openTftpClient()

    // Try to receive remote file via TFTP
    try {
      tftpClient.receiveFile(remoteFilename, transferMode, output, host.getHostName, host.getPort)
    } catch {
      case e: UnknownHostException => throw e
      case e: IOException => throw e
    } finally {
      // Close local socket and output file
      closeTftpClient()
      output.close()
    }
  }

  /**
    * Open the tftp client.
    *
    * @throws SocketException if could not open local UDP socket
    */
  private def openTftpClient () {
    if (!tftpClient.isOpen) {
      tftpClient.open()
    }
  }

  /**
    * Close the tftp client.
    *
    * @throws SocketException if could not open local UDP socket
    */
  private def closeTftpClient () {
    tftpClient.close()
  }
}
