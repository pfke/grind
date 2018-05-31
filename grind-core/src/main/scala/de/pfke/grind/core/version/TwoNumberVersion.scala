package de.pfke.grind.core.version

import scala.language.implicitConversions

/**
 * Usage:
 *
 * import Version._
 *
 * val ver: Version = v1.3
 */
object TwoNumberVersion {
  private val fullVersion_r = """(\d+)\.(\d+)$""".r
  private val majorVersion_r = """(\d+)$""".r
  private val minorVersion_r = """\.(\d+)$""".r

  /**
   * Convert from string to version.
   */
  implicit def apply(in: String): TwoNumberVersion = in match {
    case fullVersion_r (major,minor) => new TwoNumberVersion(major = major.toInt, minor = minor.toInt)
    case majorVersion_r(major      ) => new TwoNumberVersion(major = major.toInt                     )
    case minorVersion_r(      minor) => new TwoNumberVersion(                     minor = minor.toInt)

    case _                           => throw new IllegalArgumentException(s"given string '$in' is not a valid two number version")
  }

  /**
   * Returns true, if the given string is a two number version
   */
  def isVersion(in: String): Boolean = in match {
    case fullVersion_r (major,minor) => true
    case majorVersion_r(major      ) => true
    case minorVersion_r(      minor) => true

    case _                           => false
  }
}

case class TwoNumberVersion(
  major: Int = 0,
  minor: Int = 0
  )
  extends Version
  with HasMajorMinor
  with Ordered[Version]
  {
  /**
   * Result of comparing `this` with operand `that`.
   *
   * Implement this method to determine how instances of A will be sorted.
   *
   * Returns `x` where:
   *
   *   - `x < 0` when `this < that`
   *
   *   - `x == 0` when `this == that`
   *
   *   - `x > 0` when  `this > that`
   */
  override def compare(that: Version) = {
    that match {
      case v: PatchLevelVersion => if (compareMajorMinor(v) == 0) -v.patchLevel else compareMajorMinor(v)
      case v: TwoNumberVersion  => compareMajorMinor(v)

      case _ => 0
    }
  }

  override def toString = s"$major.$minor"
}
