package de.pfke.grind.io.compress.archiver

import java.nio.file.{Files, Path}

import de.pfke.grind.core.crypto.ChecksumOps
import de.pfke.grind.core.data._
import de.pfke.grind.io._
import de.pfke.grind.io.compress.{ArchiveAlgorithm, Archiver}
import de.pfke.grind.io.jnio.PathOps
import de.pfke.grind.testing.core.RandomHelper
import de.pfke.grind.testing.scalatest.CleanFilesAfterAll
import org.scalatest.{Matchers, WordSpecLike}

class Archiver_archive_spec
  extends WordSpecLike
  with Matchers
  with CleanFilesAfterAll {
  "using method 'archive' with a list of src files" when {
    val dir = PathOps.createTempDirectory(Some("Archiver_archive_spec"))().cleanFileAfter
    val srcDir = dir.resolve("src").createDirectory()
    val srcFiles = (0 to 10).map { i => srcDir.resolve(s"file$i").write(s"content file $i".asByteBuffer_iso8859_1) }.toList
    val targetBase = dir.resolve("target")

    "algo == tar" should {
      val target = Archiver.archive(
        src = srcFiles,
        dest = targetBase,
        algorithm = ArchiveAlgorithm.TAR
      )

      "archive should exist" in {
        Files.exists(target) shouldBe (right = true)
      }

      "archive should end with '.tar" in {
        PathOps.extension(target) should be("tar")
      }

      "return a valid archive" in {
        val outDir = dir.resolve(s"$target.ent")

        val (out, fl) = Archiver.unArchive(target, Some(outDir))

        out should be(outDir)
        fl.size should be(srcFiles.length)
        fl.foreach { i =>
          withClue(s"testing file $i:") {
            srcFiles.find { ii => i.toString.endsWith(ii.toString) } match {
              case Some(x: Path) => ChecksumOps.fromFile()(x.toFile) should be(ChecksumOps.fromFile()(i.toFile))
              case _ => fail(s"$i not found in src file list")
            }
          }
        }
      }
    }
  }

  "using method 'archive' with a list of src dir" when {
    val dir = Files.createTempDirectory("ArchiverSpec").cleanFileAfter
    val srcDir = dir.resolve("src").createDirectory()
    val srcFiles = (0 to 10).map { i => srcDir.resolve(s"file$i").write(s"content file $i".asByteBuffer_iso8859_1) }.toList
    val targetBase = dir.resolve("target")

    "passing a dir" should {
      "archive should exist" in {
        Files.exists(
          Archiver.archive(
            src = List(srcDir),
            dest = targetBase.resolve(RandomHelper.nextString(10)),
            algorithm = ArchiveAlgorithm.ZIP
          )) shouldBe (right = true)
      }

      "archive should end with '.zip" in {
        PathOps.extension(
          Archiver.archive(
            src = List(srcDir),
            dest = targetBase.resolve(RandomHelper.nextString(10)),
            algorithm = ArchiveAlgorithm.ZIP
          )) should be ("zip")
      }

      "return a valid archive" in {
        val target = Archiver.archive(
          src = List(srcDir),
          dest = targetBase.resolve(RandomHelper.nextString(10)),
          algorithm = ArchiveAlgorithm.ZIP
        )
        val outDir = dir.resolve(s"$target.ent")

        val (out, fl) = Archiver.unArchive(target, Some(outDir))

        out should be (outDir)
        fl.size should be (srcFiles.length)
        fl.foreach { i =>
          withClue(s"testing file $i:") {
            // suche mit endsWith, da der absolute Dateiname ein anderer ist
            srcFiles.find { ii => i.toString.endsWith(ii.toString) } match {
              case Some(x: Path) => ChecksumOps.fromFile()(x.toFile) should be (ChecksumOps.fromFile()(i.toFile))
              case _ => fail(s"$i not found in src file list")
            }}}}
    }
  }

  "using method 'archive' with a root dir" when {
    val dir = Files.createTempDirectory("ArchiverSpec").cleanFileAfter
    val srcDir = dir.resolve("src").createDirectory()
    val srcFiles = (0 to 10).map { i => srcDir.resolve(s"file$i").write(s"content file $i".asByteBuffer_iso8859_1) }.toList
    val targetBase = dir.resolve("target")

    "passing a dir" should {
      "archive should exist" in {
        Files.exists(
          Archiver.archive(
            src = List(srcDir),
            dest = targetBase,
            algorithm = ArchiveAlgorithm.TAR,
            root = Some(srcDir.resolve("archive_should_exist"))
          )) shouldBe (right = true)
      }

      "archive should end with '.tar" in {
        PathOps.extension(
          Archiver.archive(
            src = List(srcDir),
            dest = targetBase,
            algorithm = ArchiveAlgorithm.TAR,
            root = Some(srcDir.resolve("archive_should_end_with_tar"))
          )) should be ("tar")
      }

      "return a valid archive" in {
        val target = Archiver.archive(
          src = List(srcDir),
          dest = targetBase,
          algorithm = ArchiveAlgorithm.TAR,
          root = Some(srcDir.resolve("return_a_valid_archive"))
        )
        val outDir = dir.resolve(s"$target.ent")

        val (out, fl) = Archiver.unArchive(target, Some(outDir))

        out should be (outDir)
        fl.size should be (srcFiles.length)
        fl.foreach { i =>
          withClue("testing file $i") {
            srcFiles.find { f => PathOps.filename(f) == PathOps.filename(i) } match {
              case Some(x: Path) => ChecksumOps.fromFile()(x.toFile) should be (ChecksumOps.fromFile()(i.toFile))
              case _ => fail(s"$i not found in src file list")
            }}}}
    }
  }
}
