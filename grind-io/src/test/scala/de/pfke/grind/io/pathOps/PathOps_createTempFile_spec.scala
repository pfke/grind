package de.pfke.grind.io.pathOps

import java.nio.file.{Files, Paths}

import de.pfke.grind.io.jnio.PathOps
import de.pfke.grind.testing.scalatest.CleanFilesAfterAll
import org.scalatest.{Matchers, WordSpec}

class PathOps_createTempFile_spec
  extends WordSpec
    with Matchers
    with CleanFilesAfterAll {
  "testing method 'createTempFile'" when {
    "passing no string and no args" should {
      "should return an existing path object" in {
        Files.exists(
          PathOps.createTempFile()().cleanFileAfter
        ) shouldBe(right = true)
      }

      "should create a file" in {
        Files.isRegularFile(
          PathOps.createTempFile()().cleanFileAfter
        ) shouldBe(right = true)
      }

      "should create into '/tmp/'" in {
        PathOps.createTempFile()()
          .cleanFileAfter
          .getParent should be(Paths.get("/tmp"))
      }
    }
  }

  "passing a prefix" should {
    "should return an existing path object" in {
      Files.exists(
        PathOps.createTempFile(Some("myname"))().cleanFileAfter
      ) shouldBe(right = true)
    }

    "should create a file" in {
      Files.isRegularFile(
        PathOps.createTempFile(Some("myname"))().cleanFileAfter
      ) shouldBe(right = true)
    }

    "should create into '/tmp/'" in {
      PathOps.createTempFile(Some("myname"))()
        .cleanFileAfter
        .getParent should be(Paths.get("/tmp"))
    }

    "should match my passed prefix" in {
      PathOps.createTempFile(Some("myname"))()
        .cleanFileAfter
        .toString should startWith("/tmp/myname")
    }
  }

  "passing a leading '/'" should {
    "throw an exception" in {
      an[IllegalArgumentException] shouldBe thrownBy(PathOps.createTempFile(Some("/myname"))())
    }
  }

  "passing a prefix+suffix" should {
    "should return an existing path object" in {
      Files.exists(
        PathOps.createTempFile(Some("myname"), Some("klökl"))().cleanFileAfter
      ) shouldBe(right = true)
    }

    "should create a file" in {
      Files.isRegularFile(
        PathOps.createTempFile(Some("myname"), Some("klökl"))().cleanFileAfter
      ) shouldBe(right = true)
    }

    "should create into '/tmp/'" in {
      PathOps.createTempFile(Some("myname"), Some("klökl"))()
        .cleanFileAfter
        .getParent should be(Paths.get("/tmp"))
    }

    "should match my passed prefix" in {
      PathOps.createTempFile(Some("myname"), Some("klökl"))()
        .cleanFileAfter
        .toString should startWith("/tmp/myname")
    }

    "should match my passed suffix" in {
      PathOps.createTempFile(Some("myname"), Some("klökl"))()
        .cleanFileAfter
        .toString should endWith("klökl")
    }
  }
}
