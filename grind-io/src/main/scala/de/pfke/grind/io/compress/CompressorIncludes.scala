package de.pfke.grind.io.compress

import java.nio.file.Path

import de.pfke.grind.io.compress.CompressorAlgorithm.CompressorAlgorithm

object CompressorIncludes
  extends CompressorIncludes

trait CompressorIncludes {
  implicit class CompressImplicits_fromPaths(
    src: Path
  ) {
    def compress(
      dest: Option[Path] = None,
      algorithm: CompressorAlgorithm = CompressorAlgorithm.BZIP2,
      deleteSrcFile: Boolean = false
    ): Path = Compressor.compress(src = src, dest = dest, algorithm = algorithm, deleteSrcFile = deleteSrcFile)

    def unCompress(): Path = unCompress(dest = None)

    def unCompress(
      dest: Path
    ): Path = unCompress(dest = Some(dest))

    def unCompress(
      dest: Option[Path]
    ): Path = Compressor.unCompress(src = src, dest = dest)
  }
}
