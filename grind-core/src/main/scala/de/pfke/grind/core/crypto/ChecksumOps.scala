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

package de.pfke.grind.core.crypto

import java.io.{File, InputStream}
import java.nio.file.{Files, Path}
import java.security.MessageDigest

import de.pfke.grind.core.crypto.ChecksumAlgorithm.ChecksumAlgorithm

object ChecksumAlgorithm
  extends Enumeration {
  type ChecksumAlgorithm = Value

  val MD2: Value = Value("MD2")
  val MD5: Value = Value("MD5")
  val SHA_1: Value = Value("SHA-1")
  val SHA_256: Value = Value("SHA-256")
  val SHA_384: Value = Value("SHA-384")
  val SHA_512: Value = Value("SHA-512")
}

object ChecksumOps {
  /**
    * Build check sum from file.
    */
  def fromFile (
    algorithm: ChecksumAlgorithm = ChecksumAlgorithm.SHA_256
  ) (
    in: File
  ): Array[Byte] = fromPath(algorithm = algorithm)(in = in.toPath)

  /**
    * Build check sum from nio file.
    */
  def fromPath (
    algorithm: ChecksumAlgorithm = ChecksumAlgorithm.SHA_256
  ) (
    in: Path
  ): Array[Byte] = fromStream(algorithm = algorithm)(Files.newInputStream(in))

  /**
    * Build check sum from inputstream.
    */
  def fromStream (
    algorithm: ChecksumAlgorithm = ChecksumAlgorithm.SHA_256
  ) (
    in: InputStream
  ): Array[Byte] = {
    val md = MessageDigest.getInstance(algorithm.toString)
    val ar = new Array[Byte](1024*1024)
    var len = in.read(ar)

    while(len != -1) {
      md.update(ar, 0, len)
      len = in.read(ar)
    }

    md.digest()
  }

  /**
    * Build check sum from string.
    */
  def fromString (
    algorithm: ChecksumAlgorithm = ChecksumAlgorithm.SHA_256
  ) (
    in: String
  ): Array[Byte] = {
    val md = MessageDigest.getInstance(algorithm.toString)

    md.update(in.getBytes)
    md.digest()
  }
}

class ChecksumOps(
  algorithm: ChecksumAlgorithm
) {
  /**
    * Build check sum from file.
    */
  def calc (in: File): Array[Byte] = ChecksumOps.fromFile(algorithm)(in)

  /**
    * Build check sum from inputstream.
    */
  def calc (in: InputStream): Array[Byte] = ChecksumOps.fromStream(algorithm)(in)

  /**
    * Build check sum from nio file.
    */
  def calc (in: Path): Array[Byte] = ChecksumOps.fromPath(algorithm)(in)

  /**
    * Calc check sum from string
    */
  def calc (in: String): Array[Byte] = ChecksumOps.fromString(algorithm)(in)
}

object ChecksumOpsIncludes
  extends ChecksumOpsIncludes

trait ChecksumOpsIncludes {
  implicit class ChecksumImplicits_fromFile (
    in: File
  ) {
    def calc (implicit algo: ChecksumAlgorithm = ChecksumAlgorithm.SHA_256): Array[Byte] = ChecksumOps.fromFile(algo)(in = in)
  }

  implicit class ChecksumImplicits_fromPath (
    in: Path
  ) {
    def calc (implicit algo: ChecksumAlgorithm = ChecksumAlgorithm.SHA_256): Array[Byte] = ChecksumOps.fromPath(algo)(in = in)
  }

  implicit class ChecksumImplicits_fromStream (
    in: InputStream
  ) {
    def calc (implicit algo: ChecksumAlgorithm = ChecksumAlgorithm.SHA_256): Array[Byte] = ChecksumOps.fromStream(algo)(in = in)
  }

  implicit class ChecksumImplicits_fromString (
    in: String
  ) {
    def calc (implicit algo: ChecksumAlgorithm = ChecksumAlgorithm.SHA_256): Array[Byte] = ChecksumOps.fromString(algo)(in = in)
  }
}
