package de.pfke.grind.core.data.length.digital

import de.pfke.grind.core.data.byTypes.primitives.DoubleOpsIncludes
import de.pfke.grind.core.data.units.prefix.{IECPrefix, Prefix, SIPrefix}

object ByteLengthIncludes
  extends ByteLengthIncludes

trait ByteLengthIncludes {
  implicit class ByteLengthImplicits_fromByteLength (
    in: ByteLength
  ) {
    def asOption: Option[ByteLength] = if(in.compare(ByteLength.zero) == 0) None else Some(in)
  }

  implicit class ByteLengthImplicits_toByteLength (
    in: Double
  ) {
    def byte: ByteLength = ByteLength(in, Prefix.No)
    def bytes: ByteLength = byte
    def B: ByteLength = byte
    def Byte: ByteLength = byte
    def Bytes: ByteLength = byte
  }

  implicit class ByteLengthImplicits_toByteLengthFromIEC (
    in: Double
  ) {
    def Kibibyte: ByteLength = create(IECPrefix.Kibi)
    def KiB: ByteLength = Kibibyte

    def Mebibyte: ByteLength = create(IECPrefix.Mebi)
    def MiB: ByteLength = Mebibyte

    def Gibibyte: ByteLength = create(IECPrefix.Gibi)
    def GiB: ByteLength = Gibibyte

    def Tebibyte: ByteLength = create(IECPrefix.Tebi)
    def TiB: ByteLength = Tebibyte

    def Pebibyte: ByteLength = create(IECPrefix.Pebi)
    def PiB: ByteLength = Pebibyte

    def Exbibyte: ByteLength = create(IECPrefix.Exbi)
    def EiB: ByteLength = Exbibyte

    def Zebibyte: ByteLength = create(IECPrefix.Zebi)
    def ZiB: ByteLength = Zebibyte

    def Yobibyte: ByteLength = create(IECPrefix.Yobi)
    def YiB: ByteLength = Yobibyte

    private def create(prefix: Prefix) = ByteLength(in, prefix)
  }

  implicit class toByteLengthFromSI (
    in: Double
  ) {
    def Kilobyte: ByteLength = create(SIPrefix.Kilo)
    def KB: ByteLength = Kilobyte

    def Megabyte: ByteLength = create(SIPrefix.Mega)
    def MB: ByteLength = Megabyte

    def Gigabyte: ByteLength = create(SIPrefix.Giga)
    def GB: ByteLength = Gigabyte

    def Terabyte: ByteLength = create(SIPrefix.Tera)
    def TB: ByteLength = Terabyte

    def Petabyte: ByteLength = create(SIPrefix.Peta)
    def PB: ByteLength = Petabyte

    def Exabyte: ByteLength = create(SIPrefix.Exa)
    def EB: ByteLength = Exabyte

    def Zettabyte: ByteLength = create(SIPrefix.Zetta)
    def ZB: ByteLength = Zettabyte

    def Yottabyte: ByteLength = create(SIPrefix.Yotta)
    def YB: ByteLength = Yottabyte

    private def create(prefix: Prefix) = ByteLength(in, prefix)
  }

  /**
    * Format byte value into a human readable string.
    */
  implicit class ByteLengthImplicits_speedFormatterFromByteLength (
    in: ByteLength
  )
    extends DoubleOpsIncludes.DoubleImplicits_fromDouble(in = in.toByte)
}
