package de.pfke.grind.net

import org.scalatest.{Matchers, WordSpecLike}

/**
 * Created by Me.
 * User: Heiko Blobner
 * Mail: heiko.blobner@gmx.de
 *
 * Date: 04.02.14
 * Time: 12:38
 *
 */
class NetUtilsSpec
  extends WordSpecLike with Matchers {
  "A NetUtils object" when {
    "using method 'findCommonNetmask'" should {
      "merge /24" in {
        NetUtils.findCommonNetmask("192.18.178.1", "192.18.178.20") should be ("/24")
      }
      "merge /16" in {
        NetUtils.findCommonNetmask("192.18.177.1", "192.18.178.20") should be ("/16")
      }
      "merge /8" in {
        NetUtils.findCommonNetmask("192.18.177.1", "192.1.178.20") should be ("/8")
      }
      "merge /1" in {
        NetUtils.findCommonNetmask("192.18.177.1", "197.19.178.20") should be ("/1")
      }
    }
  }
}
