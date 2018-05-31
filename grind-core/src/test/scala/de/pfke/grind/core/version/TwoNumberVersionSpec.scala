package de.pfke.grind.core.version

import org.scalatest.{Matchers, WordSpec}

class TwoNumberVersionSpec
  extends WordSpec
  with Matchers {
  "implicit def apply(:String)" should {
    "convert 1.5" in {
      val ver: TwoNumberVersion = "1.5"

      ver.major should be (1)
      ver.minor should be (5)
    }
    "convert .8" in {
      val ver: TwoNumberVersion = ".8"

      ver.major should be (0)
      ver.minor should be (8)
    }
    "convert 8" in {
      val ver: TwoNumberVersion = "8"

      ver.major should be (8)
      ver.minor should be (0)
    }
    "convert 07.0034" in {
      val ver: TwoNumberVersion = "07.0034"

      ver.major should be (7)
      ver.minor should be (34)
    }

    "not convert \"\"" in {
      an[IllegalArgumentException] shouldBe thrownBy {
        TwoNumberVersion("")
      }
    }
    "not convert -28.23" in {
      an[IllegalArgumentException] shouldBe thrownBy {
        TwoNumberVersion("-28.23")
      }
    }
    "not convert -28.-23" in {
      an[IllegalArgumentException] shouldBe thrownBy {
        TwoNumberVersion("-28.-23")
      }
    }
    "not convert -28" in {
      an[IllegalArgumentException] shouldBe thrownBy {
        TwoNumberVersion("-28")
      }
    }
    "not convert 28.-23" in {
      an[IllegalArgumentException] shouldBe thrownBy {
        TwoNumberVersion("28.-23")
      }
    }
    "not convert .-23" in {
      an[IllegalArgumentException] shouldBe thrownBy {
        TwoNumberVersion(".-23")
      }
    }
  }

  "comparing two TwoNumberVersions" should {
    "return -1 on 0.1 vs. 1.1 " in {
      TwoNumberVersion(0, 1) compare TwoNumberVersion(1, 1) should be (-1)
    }

    "return  1 on 1.1 vs. 0.1 " in {
      TwoNumberVersion(1, 1) compare TwoNumberVersion(0, 1) should be (1)
    }

    "return  0 on 1.1 vs. 1.1 " in {
      TwoNumberVersion(1, 1) compare TwoNumberVersion(1, 1) should be (0)
    }

    "return -1 on 1.0 vs. 1.1 " in {
      TwoNumberVersion(1, 0) compare TwoNumberVersion(1, 1) should be (-1)
    }

    "return  1 on 1.2 vs. 1.1 " in {
      TwoNumberVersion(1, 2) compare TwoNumberVersion(1, 1) should be (1)
    }

    "return  0 on 1.3 vs. 1.3 " in {
      TwoNumberVersion(1, 3) compare TwoNumberVersion(1, 3) should be (0)
    }

    "return  1 on 3.0 vs. 2.2 " in {
      TwoNumberVersion(3, 0) compare TwoNumberVersion(2, 2) should be (1)
    }

    "return -1 on 2.2 vs. 3.0 " in {
      TwoNumberVersion(2, 2) compare TwoNumberVersion(3, 0) should be (-1)
    }
  }

  "comparing TwoNumberVersion and PatchLevelVersion" should {
    "return -1 on 0.1 vs. 1.1-123 " in {
      TwoNumberVersion(0, 1) compare PatchLevelVersion(1, 1, 123) should be  < 0
    }

    "return  1 on 1.1 vs. 0.1-0 " in {
      TwoNumberVersion(1, 1) compare PatchLevelVersion(0, 1, 0) should be > 0
    }

    "return  0 on 1.1 vs. 1.1-0 " in {
      TwoNumberVersion(1, 1) compare PatchLevelVersion(1, 1, 0) should be (0)
    }

    "return -1 on 1.0 vs. 1.1-0 " in {
      TwoNumberVersion(1, 0) compare PatchLevelVersion(1, 1, 0) should be < 0
    }

    "return  1 on 1.2 vs. 1.1-9654 " in {
      TwoNumberVersion(1, 2) compare PatchLevelVersion(1, 1, 9654) should be > 0
    }

    "return  0 on 1.3 vs. 1.3-1 " in {
      TwoNumberVersion(1, 3) compare PatchLevelVersion(1, 3, 1) should be < 0
    }
  }
}
