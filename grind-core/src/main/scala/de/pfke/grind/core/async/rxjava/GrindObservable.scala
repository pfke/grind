package de.pfke.grind.core.async.rxjava

import io.reactivex.functions.Consumer
import io.reactivex.{Observable, ObservableEmitter, ObservableOnSubscribe, Observer}
import io.reactivex.subjects.PublishSubject

import scala.reflect.ClassTag

trait GrindObservable[A]
  extends Observable[A] {
  // fields
  private val _busSubject = PublishSubject.create[A]()

//  _busSubject.subscribe(new Consumer[A] {
//    override def accept (t: A): Unit = println(t.toString)
//  })
//  _busSubject.onNext("lökölk")


//  override def onNext (value: A): Unit = {
//    post(value)
//    super.onNext(value)
//  }

  /**
    * Return a new filtered observable
    */
  //def observable[B <: A] (
  //  implicit
  //  classTag: ClassTag[B]
  //): Observable[B] = {
  //  _busSubject
  //    .collect { case t: B => t }
  //}

  /**
    * Register a new listener
    */
  def registerObserver[B <: A] (
    observer: Observer[B]
  ) (
    implicit
    classTag: ClassTag[B]
  ): Unit = {
//    observable[B]
//      .subscribe(observer)
  }

  /**
    * Posten eines Events
    */
  def post (event: A): Unit = _busSubject.onNext(event)
}
