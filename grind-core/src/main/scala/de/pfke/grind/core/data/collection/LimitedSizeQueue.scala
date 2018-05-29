package de.pfke.grind.core.data.collection

import scala.collection.mutable

class LimitedSizeQueue[A](
  val limit: Int
)
  extends mutable.Queue[A] {
  override def enqueue(elems: A*): Unit = {
    if(elems.size > limit) {
      super.clear()
    } else if((elems.size + size) > limit) {
      val no_elems_to_drop = limit - math.min(0, size - elems.size)

      (0 until no_elems_to_drop).foreach { _ => dequeue() }
    }

    super.enqueue(elems.reverse.slice(0, limit).reverse:_*)
  }
}
