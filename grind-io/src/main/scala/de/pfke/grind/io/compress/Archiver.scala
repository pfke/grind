package de.pfke.grind.io.compress

import java.io.BufferedInputStream
import java.nio.file.{Files, Path}

import de.pfke.grind.io.compress.ArchiveAlgorithm.ArchiveAlgorithm
import de.pfke.grind.io.jnio.{ChannelTools, PathOps}
import org.apache.commons.compress.archivers.ArchiveStreamFactory

import scala.collection.mutable.ArrayBuffer

object Archiver {
  /**
    * Archive the src files into the dest.
    *
    * @param src list of source files
    * @param dest this is the target file base name (will be enriched with the correct extension.
    * @param algorithm is the algo to use
    * @param root this is the absolute path and all src files should be archived relative to this
    * @return a path to the archived file
    */
  def archive(
    src: List[Path],
    dest: Path,
    algorithm: ArchiveAlgorithm,
    root: Option[Path] = None
  ): Path = doArchive(src = src, dest = dest, algorithm = algorithm, root = root)

  /**
    * Unarchive the src file into the dest.
    */
  def unArchive(
    src: Path,
    dest: Option[Path] = None,
    deleteSrcPath: Boolean = false
  ): (Path, List[Path]) = {
    val res = doUnArchive(src = src, dest = dest)

    if(deleteSrcPath) {
      PathOps.rm(src)
    }

    res
  }

  /**
    * Archive the src files into the dest.
    */
  private def doArchive(
    src: List[Path],
    dest: Path,
    algorithm: ArchiveAlgorithm,
    root: Option[Path]
  ): Path = {
    val realTarget = dest.getParent.resolve(s"${PathOps.basename(dest)}.$algorithm")

    if (Files.exists(realTarget)) {
      PathOps.rm(realTarget)
    }
    PathOps.createFile(realTarget)

    val co = new ArchiveStreamFactory().createArchiveOutputStream(algorithm.toString, PathOps.outputStream(realTarget))
    val deepTreeFn = PathOps.deepTree() _

    (src ++ src.flatMap(deepTreeFn)) // get dir content AND src if no dirs
      .filter(Files.isRegularFile(_))
      .foreach { f =>
        val relativeName = root match {
          case Some(x) => x.relativize(f).toString
          case None    => f.toString.substring(1) // filter the first "/"
        }

        val entry = co.createArchiveEntry(f.toFile, relativeName)

        co.putArchiveEntry(entry)
        ChannelTools.copyStreams(Files.newInputStream(f), co, closeOutput = false)
        co.closeArchiveEntry()
      }
    co.close()

    realTarget
  }

  /**
    * Unarchive the src file into the dest.
    */
  private def doUnArchive(
    src: Path,
    dest: Option[Path] = None
  ): (Path, List[Path]) = {
    dest match {
      case Some(x) if Files.exists(x) && !Files.isDirectory(x) => throw new IllegalArgumentException(s"$x is a file, can not unarchive to a file")
      case Some(x)                                             => doUnArchive(src = src, dest = x)
      case None                                                => doUnArchive(src = src, dest = src.getParent.resolve(PathOps.basename(src)))
    }
  }

  /**
    * Unarchive the src file into the dest.
    */
  private def doUnArchive(
    src: Path,
    dest: Path
  ): (Path, List[Path]) = {
    if (!Files.exists(dest)) {
      PathOps.createDirectory(dest)
    }

    var files = new ArrayBuffer[Path]()
    val in = new ArchiveStreamFactory().createArchiveInputStream(new BufferedInputStream(Files.newInputStream(src)))

    var entry = in.getNextEntry
    while (entry != null) {
      val file = dest.resolve(entry.getName)

      if(entry.isDirectory) {
        file.toFile.mkdirs()
      } else if (Files.exists(file)) {
      } else {
        PathOps.createDirectory(file.getParent)
        PathOps.createFile(file)

        ChannelTools.copyStreams(in, Files.newOutputStream(file), bytesToCopy = entry.getSize, closeInput = false)

        files += file
      }

      entry = in.getNextEntry
    }

    (dest, files.toList)
  }
}
