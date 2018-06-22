package de.pfke.grind.core.akka.eventBus

import de.pfke.grind.core.async.akka.eventBus.bus.GrindCallbackEventBus
import de.pfke.grind.core.async.akka.eventBus.provider.LoggingEventProvider

object Runner {
  def main (args: Array[String]): Unit = {
    val r1 = new Object with GrindCallbackEventBus with LoggingEventProvider
    r1.subscribeToLoggingError({
      case i => println(i)
    })

    r1.subscribeToLogging({
      case i => println(s"all: $i")
    })

    r1.publishLoggingDebug("jkljkl")
    r1.publishLoggingError("err")

  }
}
