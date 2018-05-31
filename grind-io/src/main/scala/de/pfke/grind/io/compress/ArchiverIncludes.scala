package de.pfke.grind.io.compress

import java.nio.file.Path

import de.pfke.grind.io.compress.ArchiveAlgorithm.ArchiveAlgorithm

import scala.collection.immutable.Iterable

object ArchiverIncludes
  extends ArchiverIncludes

trait ArchiverIncludes {
  implicit class ArchiveImplicits_fromPaths(
    src: Iterable[Path]
  ) {
    def archive (
      dest: Path,
      algorithm: ArchiveAlgorithm = ArchiveAlgorithm.TAR,
      rootDir: Option[Path] = None
    ): Path = Archiver.archive(src = src.toList, dest = dest, algorithm = algorithm, root = rootDir)
  }

  implicit class UnArchiveImplicits_fromPaths(
    src: Path
  ) {
    def unArchive(
      deleteSrcPath: Boolean = false
    ): (Path, List[Path]) = Archiver.unArchive(src = src, dest = None, deleteSrcPath = deleteSrcPath)

    def unArchive(
      dest: Path,
      deleteSrcPath: Boolean
    ): (Path, List[Path]) = Archiver.unArchive(src = src, dest = Some(dest), deleteSrcPath = deleteSrcPath)
  }
}
