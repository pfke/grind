package de.pfke.grind.core.async.rxjava

import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject

object Runner {
  def main (args: Array[String]): Unit = {
    val _busSubject = PublishSubject.create[String]()

    _busSubject.subscribe(new Consumer[String] {
      override def accept (t: String): Unit = println(t.toString)
    })
    _busSubject.onNext("lökölk")
    _busSubject.onComplete()

    //  override def onNext (value: A): Unit = {
    //    post(value)
    //    super.onNext(value)
    //  }

  }
}
