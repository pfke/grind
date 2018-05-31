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

package de.pfke.grind.testing.io

import java.io.{BufferedWriter, OutputStreamWriter}
import java.nio.charset.{Charset, StandardCharsets}
import java.nio.file.{Files, Path}

import de.pfke.grind.testing.core.RandomHelper

object PathHelper {
  def createTempFile (
    filename: String
  ): Path = Files.createTempFile(filename, null)

  def createTempFile_wContent (
    filename: String,
    content: String
  ) (
    implicit
    charset: Charset = StandardCharsets.UTF_8
  ): Path = writeToFile(createTempFile(filename = filename), content)

  def createTempFile_wRandomContent (
    filename: String
  ) (
    implicit
    charset: Charset = StandardCharsets.UTF_8
  ): Path = createTempFile_wContent(filename, RandomHelper.nextString())

  def appendToFile (
    file: Path,
    content: String
  ) (
    implicit
    charset: Charset = StandardCharsets.UTF_8
  ): Path = writeToFile(file, readFromFile(file) ++ content)

  def prependToFile (
    file: Path,
    content: String
  ) (
    implicit
    charset: Charset = StandardCharsets.UTF_8
  ): Path = writeToFile(file, content ++ readFromFile(file))

  def readFromFile (
    file: Path
  ) (
    implicit
    charset: Charset = StandardCharsets.UTF_8
  ): String = if (Files.exists(file)) new String(Files.readAllBytes(file), charset) else ""

  def writeToFile (
    file: Path,
    content: String
  ) (
    implicit
    charset: Charset = StandardCharsets.UTF_8
  ): Path = {
    Files.deleteIfExists(file)
    file.toFile.createNewFile()

    val writer = new BufferedWriter(
      new OutputStreamWriter(
        Files.newOutputStream(file),
        charset
      ))

    writer.write(content)
    writer.close()

    file
  }
}

object PathHelperIncludes
  extends PathHelperIncludes

trait PathHelperIncludes {
  implicit class PathHelperImplicit_fromString (
    in: String
  ) (
    implicit
    charset: Charset = StandardCharsets.UTF_8
  ) {
    def createTempFile: Path = PathHelper.createTempFile(filename = in)
    def createTempFile_wContent (content: String): Path = PathHelper.createTempFile_wContent(filename = in, content = content)
    def createTempFile_wRandomContent: Path = PathHelper.createTempFile_wRandomContent(filename = in)
  }

  implicit class PathHelperImplicit_fromPath (
    in: Path
  ) (
    implicit
    charset: Charset = StandardCharsets.UTF_8
  ) {
    def append (content: String): Path = PathHelper.appendToFile(file = in, content = content)
    def prepend (content: String): Path = PathHelper.prependToFile(file = in, content = content)
    def read: String = PathHelper.readFromFile(file = in)
    def write (content: String): Path = PathHelper.writeToFile(file = in, content = content)
  }
}