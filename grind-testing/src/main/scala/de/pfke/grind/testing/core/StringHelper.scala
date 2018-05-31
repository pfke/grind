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

package de.pfke.grind.testing.core

object StringHelper {
  /**
   * Append string with some noise.
   */
  def appendWithNoise (
    content: String
  ): String = {
    new StringBuilder()
      .append(content)
      .append(RandomHelper.nextString(1024))
      .result()
  }

  /**
   * Prepend string with some noise.
   */
  def prependWithNoise (
    content: String
  ): String = {
    new StringBuilder()
      .append(RandomHelper.nextString(1024))
      .append(content)
      .result()
  }

  /**
   * Surround a string with random noise strings.
   */
  def surroundWithNoise (
    content: String
  ): String = {
    new StringBuilder()
      .append(RandomHelper.nextString(1024))
      .append(content)
      .append(RandomHelper.nextString(1024))
      .result()
  }
}

object StringHelperIncludes
  extends StringHelperIncludes

trait StringHelperIncludes {
  implicit class StringHelperImplicits_fromString (
    in: String
  ) {
    def appendNoise: String = StringHelper.appendWithNoise(content = in)
    def prependNoise: String = StringHelper.prependWithNoise(content = in)
    def sourandWithNoise: String = StringHelper.surroundWithNoise(content = in)
  }
}
