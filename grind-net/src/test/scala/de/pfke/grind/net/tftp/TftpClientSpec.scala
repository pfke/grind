package de.pfke.grind.net.tftp

import java.io.IOException
import java.net.{InetAddress, InetSocketAddress}

import de.pintono.grind.io._
import org.apache.commons.net.tftp.TFTP
import org.scalatest.{Matchers, WordSpec}

class TftpClientSpec
  extends WordSpec
  with Matchers {
  "testing w/o server running" should {
    "throw an exception if the local file does not exist" in {
      an[IOException] shouldBe thrownBy(
        new TftpClient(new InetSocketAddress(InetAddress.getLocalHost, TFTP.DEFAULT_PORT))
          .send(
            localFilename = "file_does_not_exist",
            remoteFilename = "_rem_1st.file"
          )
      )
    }

    "throw an exception if host is not available" in {
      an[IOException] shouldBe thrownBy(
        new TftpClient(new InetSocketAddress("175.12.34.1", TFTP.DEFAULT_PORT))
          .send(
            localFilename = "file_does_not_exist".createTempFile().toString,
            remoteFilename = "_rem_1st.file"
          )
      )
    }
  }
}
