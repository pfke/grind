package de.pfke.grind.core.version

trait Version
  extends Ordered[Version]

object Version {
  def apply(in: String): Version = {
    if(TwoNumberVersion.isVersion(in))
      TwoNumberVersion(in)
    else if(PatchLevelVersion.isVersion(in))
      PatchLevelVersion(in)
    else
      throw new IllegalArgumentException(s"given string '$in' is not a valid version")
  }
}

trait HasMajorMinor {
  def major: Int
  def minor: Int

  def compareMajorMinor(that: HasMajorMinor) = if(major != that.major) major - that.major else minor - that.minor
}
