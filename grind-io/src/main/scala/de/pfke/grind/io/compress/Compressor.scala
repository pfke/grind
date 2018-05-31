package de.pfke.grind.io.compress

import java.io.BufferedInputStream
import java.nio.file.{Files, Path}

import de.pfke.grind.io.compress.CompressorAlgorithm.CompressorAlgorithm
import de.pfke.grind.io.jnio.{ChannelTools, PathOps}
import org.apache.commons.compress.compressors.CompressorStreamFactory

object Compressor {
  // fields
  private val _cTypeToFactString = Map(
    CompressorAlgorithm.BZIP2 -> CompressorStreamFactory.BZIP2,
    CompressorAlgorithm.GZIP -> CompressorStreamFactory.GZIP
  )

  /**
    * Uncompress the src file into the dest.
    */
  def compress(
    src: Path,
    dest: Option[Path] = None,
    algorithm: CompressorAlgorithm = CompressorAlgorithm.BZIP2,
    deleteSrcFile: Boolean = false
  ): Path = doCompress(src = src, dest = dest, algorithm = algorithm, deleteSrcFile = deleteSrcFile)

  /**
    * Uncompress the src file into the dest.
    *
    * val uncomp = blocking { run { Compressor.unCompress(archive) }} mapToOption
    */
  def unCompress(
    src: Path,
    dest: Option[Path] = None
  ): Path = {
    dest match {
      case Some(x) if Files.isDirectory(x) => doUnCompress(src, x.resolve(PathOps.basename(src)))
      case Some(x)                         => doUnCompress(src, x)
      case None                            => doUnCompress(src, src.getParent.resolve(PathOps.basename(src)))
    }
  }

  /**
    * Compress the src file into the dest.
    */
  private def doCompress(
    src: Path,
    dest: Option[Path] = None,
    algorithm: CompressorAlgorithm,
    deleteSrcFile: Boolean = false
  ): Path = {
    val realTarget = dest match {
      case Some(x) => x
      case None    => src.getParent.resolve(s"${src.toString}.$algorithm")
    }

    if (Files.exists(realTarget)) {
      PathOps.rm(realTarget)
    }
    PathOps.createFile(realTarget)

    ChannelTools.copyStreams(
      PathOps.inputStream(src),
      new CompressorStreamFactory()
        .createCompressorOutputStream(
          _cTypeToFactString(algorithm),
          PathOps.outputStream(realTarget)
        ))

    if(deleteSrcFile) {
      PathOps.rm(src)
    }
    realTarget
  }

  /**
    * Uncompress the src file into the dest.
    */
  private def doUnCompress(
    src: Path,
    dest: Path
  ): Path = {
    if (Files.exists(dest)) {
      PathOps.rm(dest)
    }
    PathOps.createFile(dest)

    ChannelTools.copyStreams(
      new CompressorStreamFactory().createCompressorInputStream(new BufferedInputStream(PathOps.inputStream(src))),
      PathOps.outputStream(dest)
    )

    dest
  }
}
