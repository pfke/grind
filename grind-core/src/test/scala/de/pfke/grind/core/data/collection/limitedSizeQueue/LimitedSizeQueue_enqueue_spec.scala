package de.pfke.grind.core.data.collection.limitedSizeQueue

import de.pfke.grind.core.data.collection.LimitedSizeQueue
import org.scalatest.{Matchers, WordSpecLike}

class LimitedSizeQueue_enqueue_spec
  extends WordSpecLike with Matchers {
  "operating on a full queue" when {
    "adding <= queue.limit elements" should {
      "queue size should not exceed the limit" in {
        val queue = new LimitedSizeQueue[Int](5)

        queue.enqueue((0 until 5).map { i => i }:_*)
        queue.size should be (5)

        queue.enqueue((0 until 5).map { i => i }:_*)
        queue.size should be (5)
      }
    }

//    "performace test" in {
//      val timer = new StopWatch()
//
//      (0 until 20).foreach { i =>
//        val q1 = new LimitedSizeQueue[Int](10000)
//
//        timer.reset()
//        timer.start()
//        q1.enqueue((0 until 5000000).map { i => i }.toSeq: _*)
//        timer.stop()
//
//        println(timer.diffTotal.toString)
//      }
//    }

    "adding > queue.limit elements" should {
      "should only add the last elements" in {
        val queue = new LimitedSizeQueue[Int](5)

        queue.enqueue((0 until 5).map { i => i }:_*)
        queue.size should be (5)

        queue.enqueue((10 to 20).map { i => i }:_*)

        queue.dequeue() should be (16)
        queue.dequeue() should be (17)
        queue.dequeue() should be (18)
        queue.dequeue() should be (19)
        queue.dequeue() should be (20)
      }
    }
  }
}
