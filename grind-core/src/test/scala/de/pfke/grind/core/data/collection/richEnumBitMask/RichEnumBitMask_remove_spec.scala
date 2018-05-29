/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Heiko Blobner
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.pfke.grind.core.data.collection.richEnumBitMask

import de.pfke.grind.core.data.collection.RichEnumBitMask
import org.scalatest.{Matchers, WordSpecLike}

import scala.collection.immutable.BitSet

class RichEnumBitMask_remove_spec
  extends WordSpecLike with Matchers {
  "method '-=(.)'" when {
    "shift = 0, len-mask" should {
      "remove emtpy set" in {
        val tto = RichEnumBitMask.apply(TestEnum)

        tto -= TestEnum.e2
        tto.toBitSet should be (BitSet())
      }

      "remove on non empty set" in {
        val tto = RichEnumBitMask.apply(TestEnum)

        tto += TestEnum.e2
        tto += TestEnum.e1
        tto -= TestEnum.e1
        tto.toBitSet should be (BitSet(TestEnum.e2.id))
      }
    }

    "shift > 0, len >= msb" should {
      "remove to empty" in {
        val tto = RichEnumBitMask.applyWithLen(TestEnum, shift = 6, len = 10)

        tto += TestEnum.e2
        tto -= TestEnum.e2
        tto.toBitSet should be (BitSet())
      }

      "remove more enum" in {
        val tto = RichEnumBitMask.applyWithLen(TestEnum, shift = 6, len = 10)

        tto += TestEnum.e2
        tto += TestEnum.e1
        tto -= TestEnum.e2
        tto -= TestEnum.e1
        tto.toBitSet should be (BitSet())
      }
    }

    "shift > 0, len < msb" should {
      "remove to empty" in {
        val tto = RichEnumBitMask.applyWithLen(TestEnum, shift = 6, len = 2)

        tto += TestEnum.e2
        tto -= TestEnum.e2
        tto.toBitSet should be (BitSet())
      }

      "remove more enum" in {
        val tto = RichEnumBitMask.applyWithLen(TestEnum, shift = 6, len = 2)

        tto += TestEnum.e2
        tto += TestEnum.e1
        tto -= TestEnum.e2
        tto -= TestEnum.e1
        tto.toBitSet should be (BitSet())
      }

      "mask out exceeding bits" in {
        val tto = RichEnumBitMask.applyWithLen(TestEnum, shift = 6, len = 2)

        tto += TestEnum.e3
        tto += TestEnum.e2
        tto += TestEnum.e1
        tto -= TestEnum.e3
        tto -= TestEnum.e2
        tto -= TestEnum.e1
        tto.toBitSet should be (BitSet())
      }
    }
  }

  "method '-=(*)'" when {
    "shift = 0, len-mask" should {
      "remove emtpy set" in {
        val tto = RichEnumBitMask.apply(TestEnum)

        tto -= TestEnum.e2
        tto.toBitSet should be (BitSet())
      }

      "remove on non empty set" in {
        val tto = RichEnumBitMask.apply(TestEnum)

        tto += TestEnum.e2
        tto += TestEnum.e1
        tto -= (TestEnum.e1, TestEnum.e2)
        tto.toBitSet should be (BitSet())
      }
    }

    "shift > 0, len >= msb" should {
      "remove to empty" in {
        val tto = RichEnumBitMask.applyWithLen(TestEnum, shift = 6, len = 10)

        tto += TestEnum.e2
        tto += TestEnum.e1
        tto -= (TestEnum.e1, TestEnum.e2)
        tto.toBitSet should be (BitSet())
      }

      "remove more enum" in {
        val tto = RichEnumBitMask.applyWithLen(TestEnum, shift = 6, len = 10)

        tto += TestEnum.e2
        tto += TestEnum.e1
        tto -= (TestEnum.e2, TestEnum.e1)
        tto.toBitSet should be (BitSet())
      }
    }

    "shift > 0, len < msb" should {
      "remove to empty" in {
        val tto = RichEnumBitMask.applyWithLen(TestEnum, shift = 6, len = 2)

        tto += TestEnum.e2
        tto += TestEnum.e1
        tto -= (TestEnum.e1, TestEnum.e2)
        tto.toBitSet should be (BitSet())
      }

      "remove more enum" in {
        val tto = RichEnumBitMask.applyWithLen(TestEnum, shift = 6, len = 2)

        tto += TestEnum.e2
        tto += TestEnum.e1
        tto -= (TestEnum.e2, TestEnum.e1)
        tto.toBitSet should be (BitSet())
      }

      "mask out exceeding bits" in {
        val tto = RichEnumBitMask.applyWithLen(TestEnum, shift = 6, len = 2)

        tto += TestEnum.e3
        tto += TestEnum.e2
        tto += TestEnum.e1
        tto -= (TestEnum.e2, TestEnum.e1, TestEnum.e3)
        tto.toBitSet should be (BitSet())
      }
    }
  }

  "method 'remove(.)'" when {
    "shift = 0, len-mask" should {
      "remove emtpy set" in {
        val tto = RichEnumBitMask.apply(TestEnum)

        tto.remove(TestEnum.e2)
        tto.toBitSet should be (BitSet())
      }

      "remove on non empty set" in {
        val tto = RichEnumBitMask.apply(TestEnum)

        tto += TestEnum.e2
        tto += TestEnum.e1
        tto.remove(TestEnum.e1)
        tto.toBitSet should be (BitSet(TestEnum.e2.id))
      }
    }

    "shift > 0, len >= msb" should {
      "remove to empty" in {
        val tto = RichEnumBitMask.applyWithLen(TestEnum, shift = 6, len = 10)

        tto += TestEnum.e2
        tto.remove(TestEnum.e2)
        tto.toBitSet should be (BitSet())
      }

      "remove more enum" in {
        val tto = RichEnumBitMask.applyWithLen(TestEnum, shift = 6, len = 10)

        tto += TestEnum.e2
        tto += TestEnum.e1
        tto.remove(TestEnum.e2)
        tto.remove(TestEnum.e1)
        tto.toBitSet should be (BitSet())
      }
    }

    "shift > 0, len < msb" should {
      "remove to empty" in {
        val tto = RichEnumBitMask.applyWithLen(TestEnum, shift = 6, len = 2)

        tto += TestEnum.e2
        tto.remove(TestEnum.e2)
        tto.toBitSet should be (BitSet())
      }

      "remove more enum" in {
        val tto = RichEnumBitMask.applyWithLen(TestEnum, shift = 6, len = 2)

        tto += TestEnum.e2
        tto += TestEnum.e1
        tto.remove(TestEnum.e2)
        tto.remove(TestEnum.e1)
        tto.toBitSet should be (BitSet())
      }

      "mask out exceeding bits" in {
        val tto = RichEnumBitMask.applyWithLen(TestEnum, shift = 6, len = 2)

        tto += TestEnum.e3
        tto += TestEnum.e2
        tto += TestEnum.e1
        tto.remove(TestEnum.e3)
        tto.remove(TestEnum.e2)
        tto.remove(TestEnum.e1)
        tto.toBitSet should be (BitSet())
      }
    }
  }
}
