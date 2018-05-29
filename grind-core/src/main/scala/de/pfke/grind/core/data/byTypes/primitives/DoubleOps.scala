package de.pfke.grind.core.data.byTypes.primitives

import scala.concurrent.duration._
import scala.concurrent.{duration => sDur}

object DoubleOps {
  def asHumanReadableByte (in: Double): String = format(value = in)
  def asHumanReadableByte_SI (in: Double): String = format(value = in, isSI = true)

  def asHumanReadableTime (
    in: Double
  ): String = {
    in match {
      case _ if (in / 1d) > 1.0           => asHumanReadableTime_s(in)
      case _ if (in / 0.001d) > 1.0       => asHumanReadableTime_ms(in)
      case _ if (in / 0.000001d) > 1.0    => asHumanReadableTime_µs(in)
      case _ if (in / 0.000000001d) > 1.0 => asHumanReadableTime_ns(in)
      case _                              => asHumanReadableTime_s(in)
    }
  }

  def asHumanReadableTime_s (in: Double): String = "%.3fs".format(in)
  def asHumanReadableTime_ms (in: Double): String = "%.3fms".format(in * 1000)
  def asHumanReadableTime_µs (in: Double): String = "%.3fµs".format(in * 1000000)
  def asHumanReadableTime_ns (in: Double): String = "%.3fns".format(in * 1000000000)

  def asSpeed (
    in: Double,
    duration: Duration
  ): String = {
    val duration_in_ns = duration.toNanos
    val one_s_in_ns = (1 second).toNanos
    val speed_per_s = in * one_s_in_ns / duration_in_ns

    s"${DoubleOps.asHumanReadableByte_SI(speed_per_s)}/s"
  }

  def asSpeed (
    in: Double,
    duration_in_ms: Double
  ): String = asSpeed(in = in, sDur.Duration.apply(duration_in_ms, sDur.MILLISECONDS))

  /**
    * Formats the given byte count into a human readable string.
    *
    *                              SI     BINARY
    *                   0:        0 B        0 B
    *                  27:       27 B       27 B
    *                 999:      999 B      999 B
    *                1000:     1.0 kB     1000 B
    *                1023:     1.0 kB     1023 B
    *                1024:     1.0 kB    1.0 KiB
    *                1728:     1.7 kB    1.7 KiB
    *              110592:   110.6 kB  108.0 KiB
    *             7077888:     7.1 MB    6.8 MiB
    *           452984832:   453.0 MB  432.0 MiB
    *         28991029248:    29.0 GB   27.0 GiB
    *       1855425871872:     1.9 TB    1.7 TiB
    * 9223372036854775807:     9.2 EB    8.0 EiB   (Long.MAX_VALUE)
    */
  private def format(
    value: Double,
    isSI: Boolean = false
  ): String = {
    val unit = if(isSI) 1000 else 1024

    if (value < unit) return "%.0f B".format(value)

    val exp = (math.log(value) / math.log(unit)).toInt
    val pre = (if(isSI) "kMGTPEZY" else "KMGTPEZY").charAt(exp-1) + (if(isSI) "" else "i")

    "%.3f %sB".format(value / math.pow(unit, exp), pre)
  }
}

object DoubleOpsIncludes
  extends DoubleOpsIncludes

trait DoubleOpsIncludes {
  /**
    * Format value into a human readable string.
    */
  implicit class DoubleImplicits_fromDouble (
    in: Double
  ) {
    def asHumanReadableSize: String = DoubleOps.asHumanReadableByte(in = in)
    def asHumanReadableSize_SI: String = DoubleOps.asHumanReadableByte_SI(in = in)

    def asHumanReadableTime: String = DoubleOps.asHumanReadableTime(in = in)
    def asHumanReadableTime_s: String  = DoubleOps.asHumanReadableTime_s(in = in)
    def asHumanReadableTime_ms: String = DoubleOps.asHumanReadableTime_ms(in = in)
    def asHumanReadableTime_µs: String = DoubleOps.asHumanReadableTime_µs(in = in)
    def asHumanReadableTime_ns: String = DoubleOps.asHumanReadableTime_ns(in = in)

    def asSpeed (duration: Duration): String = DoubleOps.asSpeed(in = in, duration = duration)
    def asSpeed (duration_in_ms: Double): String = asSpeed(sDur.Duration.apply(duration_in_ms, sDur.MILLISECONDS))
  }
}
