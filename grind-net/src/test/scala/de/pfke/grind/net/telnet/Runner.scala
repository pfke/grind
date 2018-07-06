package de.pfke.grind.net.telnet

import java.nio.charset.Charset

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.stream.scaladsl.{Framing, Keep, Sink, Source}
import akka.util.ByteString

object Runner {
  def main (args: Array[String]): Unit = {
    implicit val system = ActorSystem("QuickStart")
    implicit val materializer = ActorMaterializer()

    val r1 = Source.queue[String](bufferSize = 10, overflowStrategy = OverflowStrategy.dropTail)
      .toMat(Sink.foreach(x => println(x)))(Keep.left)
      .run()
    val r2 = Source.queue[String](bufferSize = 10, overflowStrategy = OverflowStrategy.dropTail)
      .runWith(Sink.foreach(x => println(x)))
    val r3 = Source
      .queue[ByteString](bufferSize = 10, overflowStrategy = OverflowStrategy.dropHead)
      .via(Framing.delimiter(delimiter = ByteString("\n".getBytes(Charset.defaultCharset())), 8192, allowTruncation = true))
      .toMat(Sink.foreach(x => println(x.utf8String)))(Keep.left)
      .run()


    //r1.offer("1")
    //r1.offer("2")
    //r1.offer("3")
    //r1.complete()
    //r1.offer("4")


    r3.offer(ByteString("sdsd\njj"))

//    system.terminate()
  }
}
