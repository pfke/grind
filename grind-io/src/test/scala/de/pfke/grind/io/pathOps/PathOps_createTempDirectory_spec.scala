package de.pfke.grind.io.pathOps

import java.nio.file.{Files, Paths}

import de.pfke.grind.io.jnio.PathOps
import de.pfke.grind.testing.scalatest.CleanFilesAfterAll
import org.scalatest.{Matchers, WordSpec}

class PathOps_createTempDirectory_spec
  extends WordSpec
    with Matchers
    with CleanFilesAfterAll {
  "testing method 'createTempDir'" when {
    "passing no string and no args" should {
      "should return an existing path object" in {
        Files.exists(
          PathOps.createTempDirectory()().cleanFileAfter
        ) shouldBe(right = true)
      }

      "should create a dir" in {
        Files.isDirectory(
          PathOps.createTempDirectory()().cleanFileAfter
        ) shouldBe(right = true)
      }

      "should create into '/tmp/'" in {
        PathOps.createTempDirectory()()
          .cleanFileAfter
          .getParent should be(Paths.get("/tmp"))
      }
    }

    "passing a prefix" should {
      "should return an existing path object" in {
        Files.exists(
          PathOps.createTempDirectory(Some("myname"))().cleanFileAfter
        ) shouldBe(right = true)
      }

      "should create a dir" in {
        Files.isDirectory(
          PathOps.createTempDirectory(Some("myname"))().cleanFileAfter
        ) shouldBe(right = true)
      }

      "should create into '/tmp/'" in {
        PathOps.createTempDirectory(Some("myname"))()
          .cleanFileAfter
          .getParent should be(Paths.get("/tmp"))
      }

      "should match my passed prefix" in {
        PathOps.createTempDirectory(Some("myname"))()
          .cleanFileAfter
          .toString should startWith("/tmp/myname")
      }
    }

    "passing a leading '/'" should {
      "throw an exception" in {
        an[IllegalArgumentException] shouldBe thrownBy(PathOps.createTempDirectory(Some("/myname"))())
      }
    }
  }
}
