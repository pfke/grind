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

class RichEnumBitMask_contains_E_spec
  extends WordSpecLike with Matchers {
  "method 'contains(.)'" when {
    "shift = 0, len-mask" should {
      "return false emtpy set" in {
        val tto = RichEnumBitMask.apply(TestEnum).contains(TestEnum.e1) shouldBe (right = false)
      }

      "return true on non empty set" in {
        val tto = RichEnumBitMask.apply(TestEnum)

        tto += TestEnum.e2
        tto += TestEnum.e1
        tto.contains(TestEnum.e2) shouldBe (right = true)
      }
    }

    "shift > 0, len >= msb" should {
      "return false emtpy set" in {
        val tto = RichEnumBitMask.applyWithLen(TestEnum, shift = 6, len = 10).contains(TestEnum.e1) shouldBe (right = false)
      }

      "return true on non empty set" in {
        val tto = RichEnumBitMask.applyWithLen(TestEnum, shift = 6, len = 10)

        tto += TestEnum.e2
        tto += TestEnum.e1
        tto.contains(TestEnum.e1) shouldBe (right = true)
      }
    }

    "shift > 0, len < msb" should {
      "return false emtpy set" in {
        val tto = RichEnumBitMask.applyWithLen(TestEnum, shift = 6, len = 2).contains(TestEnum.e1) shouldBe (right = false)
      }

      "return true on non empty set (not masked out)" in {
        val tto = RichEnumBitMask.applyWithLen(TestEnum, shift = 6, len = 2)

        tto += TestEnum.e2
        tto += TestEnum.e1
        tto += TestEnum.e3
        tto.contains(TestEnum.e2) shouldBe (right = true)
      }

      "return false on non empty set (masked out)" in {
        val tto = RichEnumBitMask.applyWithLen(TestEnum, shift = 6, len = 2)

        tto += TestEnum.e2
        tto += TestEnum.e1
        tto += TestEnum.e3
        tto.contains(TestEnum.e3) shouldBe (right = false)
      }
    }
  }
}
