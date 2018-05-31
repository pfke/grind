package de.pfke.grind.io.compress

object ArchiveAlgorithm
  extends Enumeration {
  type ArchiveAlgorithm = Value

  val AR: Value = Value("ar")
  val CPIO: Value = Value("cpio")
  val JAR: Value = Value("jar")
  val TAR: Value = Value("tar")
  val ZIP: Value = Value("zip")
}
