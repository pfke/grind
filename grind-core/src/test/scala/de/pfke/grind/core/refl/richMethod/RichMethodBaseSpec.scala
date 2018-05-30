package de.pfke.grind.core.refl.richMethod

import org.scalatest.{Matchers, WordSpec}

class RichMethodBaseSpec
  extends WordSpec
    with Matchers {
  val namespace = "de.pfke.squeeze.core.refl.richMethodRefl.mocks"

  protected def buildModClassName(className: String) = s"${buildRealClassName(className)}$$"
  protected def buildRealClassName(className: String) = s"$namespace.$className"
}
