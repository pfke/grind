package de.pfke.grind.refl.sizeOf

import de.pfke.grind.core.refl.SizeOf
import org.scalatest.{Matchers, WordSpec}

class sizeOf_guessA_spec
  extends WordSpec
      with Matchers {
    "testing method 'guess[A]()'" when {
      "called w/ primitives" should {
        "'Boolean' should have correct size" in {
          SizeOf.guess[Boolean]() should be (1)
        }

        "'Byte' should have correct size" in {
          SizeOf.guess[Byte]() should be (1)
        }

        "'Char' should have correct size" in {
          SizeOf.guess[Char]() should be (2)
        }

        "'Double' should have correct size" in {
          SizeOf.guess[Double]() should be (8)
        }

        "'Float' should have correct size" in {
          SizeOf.guess[Float]() should be (4)
        }

        "'Int' should have correct size" in {
          SizeOf.guess[Int]() should be (4)
        }

        "'Long' should have correct size" in {
          SizeOf.guess[Long]() should be (8)
        }

        "'Short' should have correct size" in {
          SizeOf.guess[Short]() should be (2)
        }
      }
    }
  }
