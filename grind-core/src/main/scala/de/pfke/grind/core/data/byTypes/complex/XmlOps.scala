package de.pfke.grind.core.data.byTypes.complex

import scala.xml.NodeSeq

object XmlOps {
  /**
    * Usage: ns \\ "sohi" \@ ("attr", _ == "some_name")
    */
  def \@(
    ns: NodeSeq,
    attrName: String,
    matcher: String => Boolean
    ): NodeSeq = ns filter { _ \\ ("@" + attrName) exists (s => matcher(s.text)) }
}

object XmlIncludes
  extends XmlIncludes

trait XmlIncludes {
  implicit class RichNodeSeq(ns: NodeSeq) {
    def \@(
      attrName: String,
      matcher: String => Boolean
      ): NodeSeq = XmlOps.\@(ns = ns, attrName = attrName, matcher = matcher)
  }
}
