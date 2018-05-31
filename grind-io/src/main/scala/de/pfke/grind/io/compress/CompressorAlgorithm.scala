package de.pfke.grind.io.compress

object CompressorAlgorithm
  extends Enumeration {
  type CompressorAlgorithm = Value

  val GZIP  = Value("gz")
  val BZIP2 = Value("bz2")
}
