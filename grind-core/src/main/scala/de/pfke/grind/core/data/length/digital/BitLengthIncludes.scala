package de.pfke.grind.core.data.length.digital

object BitLengthIncludes
  extends BitLengthIncludes

trait BitLengthIncludes {
  implicit class BitLengthImplicits_fromBitLength(
    in: BitLength
  ) {
    def asOption: Option[BitLength] = if(in.compare(BitLength.zero) == 0) None else Some(in)
  }

  implicit class BitLengthImplicits_toBitLength(
    in: Double
  ) {
    def bit: BitLength = BitLength(in)
    def bits: BitLength = bit
    def Bit: BitLength = bit
    def Bits: BitLength = bit
  }
}
