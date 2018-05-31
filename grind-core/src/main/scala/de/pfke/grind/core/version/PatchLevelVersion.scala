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

package de.pfke.grind.core.version

object PatchLevelVersion {
  // fields
  private val fullVersionWithPoint_r = """(\d+)\.(\d+).(\d+)""".r
  private val fullVersion_r = """(\d+)\.(\d+)-(\d+)""".r
  private val majorVersion_r = """(\d+)""".r
  private val minorVersion_r = """\.(\d+)-(\d+)""".r

  /**
    * Try to convert from string to int and return as Option[Int]:
    *
    * toInt("102").getOrElse(23)
    */
  implicit def asInt(value: String): Int = {
    try {
      Integer.parseInt(value, 10)
    } catch {
      case e: NumberFormatException => 0
    }
  }

  /**
   * Convert from string to version.
   */
  implicit def apply(in: String): PatchLevelVersion = in match {
    case fullVersionWithPoint_r(_1, _2, _3) => PatchLevelVersion(major = _1, minor = _2, patchLevel = _3)
    case fullVersion_r(_1, _2, _3)          => PatchLevelVersion(major = _1, minor = _2, patchLevel = _3)
    case majorVersion_r(_1)                 => PatchLevelVersion(major = _1, minor = 0, patchLevel = 0)
    case minorVersion_r(_1, _2)             => PatchLevelVersion(major = 0, minor = _1, patchLevel = _2)

    case _                                  => throw new IllegalArgumentException(s"given string '$in' is not a valid patch level version")
  }

  /**
   * Returns true, if the given string is a patch level version
   */
  def isVersion(in: String): Boolean = in match {
    case fullVersionWithPoint_r(_, _, _) => true
    case fullVersion_r(_, _, _)          => true
    case majorVersion_r(_)               => true
    case minorVersion_r(_, _)            => true

    case _ => false
  }
}

case class PatchLevelVersion(
  major: Int = 0,
  minor: Int = 0,
  patchLevel: Int = 0
  )
  extends Version
  with HasMajorMinor
  with Ordered[Version]
  {
  /**
   * Result of comparing `this` with operand `that`.
   *
   * Implement this method to determine how instances of A will be sorted.
   */
  override def compare(that: Version) = {
    that match {
      case v: PatchLevelVersion => compareMajorMinor(v) + patchLevel - v.patchLevel
      case v: TwoNumberVersion  => if (compareMajorMinor(v) == 0) patchLevel else compareMajorMinor(v)

      case _ => 0
    }
  }

  override def toString = s"$major.$minor-$patchLevel"
}
