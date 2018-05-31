package de.pfke.grind.io.pathOps

import java.nio.file.Files

import de.pfke.grind.io.jnio.PathOps
import de.pfke.grind.testing.scalatest.CleanFilesAfterAll
import org.scalatest.{Matchers, WordSpec}

class PathOps_flatTree_spec
  extends WordSpec
    with Matchers
    with CleanFilesAfterAll {
  "testing method 'flatFileTree'" when {
    "on empty dir" should {
      "should return 0 childs" in {
        PathOps
          .flatTree(
            PathOps.createTempDirectory(Some("PathOps_flatTree_spec"))().cleanFileAfter
          ).size should be (0)
      }
    }

    "on non-empty dir (files only)" should {
      val path = PathOps.createTempDirectory(Some("PathOps_flatTree_spec"))().cleanFileAfter

      val file01 = Files.createFile(path.resolve("file01"))
      val file02 = Files.createFile(path.resolve("file02"))
      val file03 = Files.createFile(path.resolve("file03"))
      val file04 = Files.createFile(path.resolve("file04"))
      val file05 = Files.createFile(path.resolve("file05"))

      "should return 5 childs" in {
        PathOps.flatTree(path).size should be (5)
      }

      "file 1 should exist" in {
        PathOps.flatTree(path).contains(file01) shouldBe (right = true)
      }

      "file 2 should exist" in {
        PathOps.flatTree(path).contains(file01) shouldBe (right = true)
      }

      "file 3 should exist" in {
        PathOps.flatTree(path).contains(file01) shouldBe (right = true)
      }

      "file 4 should exist" in {
        PathOps.flatTree(path).contains(file01) shouldBe (right = true)
      }

      "file 5 should exist" in {
        PathOps.flatTree(path).contains(file01) shouldBe (right = true)
      }
    }
  }
}
