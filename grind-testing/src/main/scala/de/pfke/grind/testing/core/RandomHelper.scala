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

import scala.util.Random

object RandomHelper {
  // fields
  private val _rnd = new Random(new Random().nextInt())

  /**
   * Return a new random array.
   */
  def nextArray (
    len: Int
  ): Array[Byte] = {
    val ar = new Array[Byte](len)
    _rnd.nextBytes(ar)

    ar
  }

  /**
   * Return a new random boolean.
   */
  def nextBool: Boolean = _rnd.nextBoolean()

  /**
   * Return a new random byte.
   */
  def nextByte: Byte = nextInt(upper = 255).toByte

  /**
   * Return a new random float.
   */
  def nextDouble: Double = _rnd.nextDouble()

  /**
   * Return a new random float.
   */
  def nextFloat: Float = _rnd.nextFloat()

  /**
   * Return a new random int with upper and lower bounds.
   */
  def nextInt (
    lower: Int = Int.MinValue,
    upper: Int = Int.MaxValue
  ): Int = _rnd.nextInt(upper - lower) + lower

  /**
   * Return a new random long.
   */
  def nextLong: Long = _rnd.nextLong()

  /**
   * Return a new random short.
   */
  def nextShort: Short = nextInt().toShort

  /**
    * Return a random string with the given length.
    */
  def nextString (
    length: Int = 100
  ): String = new String((0 until length).map { i=> _rnd.nextPrintableChar() }.toArray)
}
