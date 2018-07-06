package de.pfke.grind.net

import java.net.{Inet4Address, InetAddress}

import de.pfke.grind.core.data._
import org.apache.commons.net.util.SubnetUtils

object NetUtils {
  /**
   * Convert from cidr to netmask.
   */
  def fromNetmaskToCidr (
    netmask: String
  ): String = netmask match {
    case "255.255.255.0" => "24"
    case "255.255.0.0"   => "16"
    case "255.0.0.0"     => "8"
    case _               => "1"
  }

  /**
   * Convert from cidr to netmask.
   */
  def fromCidrToNetmask (
    cidr: String
  ): String = cidr.replaceFirst("/", "") match {
    case "24" => "255.255.255.0"
    case "16" => "255.255.0.0"
    case  "8" => "255.0.0.0"
    case    _ => "128.0.0.0"
  }

  /**
   * Find the best matching ip address from goven list.
   */
  def findBestMatchingAsCIDR (
    addresses: List[InetAddress],
    toMatch: String
    ): Option[String] = {
    addresses
      .map { i => findCommonNetmask(toMatch, i.getHostAddress) }
      .sortBy { i => i }
      .reverse
      .headOption match {
      case Some(x) => Some(s"$toMatch$x")
      case None => None
    }
  }

  /**
   * Build a common netmask.
   */
  def findCommonNetmask(
    ip1: String,
    ip2: String
    ): String = {
    val b1 = ip1.countMatches(ip2)

    ip1
      .substring(0, b1)                 // den gleichen String rausholen
      .count(_ == '.') match {          // Punkte zählen und Netzmaske erstellen
          case 3 => "/24"
          case 2 => "/16"
          case 1 => "/8"
          case _ => "/1"
        }
  }

  /**
   * Return only the matching part of 2 ips..
   */
  def findMatchingPart(
    ip1: String,
    ip2: String
    ): String = ip1.substring(0, ip1.countMatches(ip2))

  /**
   * Return all inet addresses of the local interfaces.
   */
  def getAllLocalIPAddresses: List[InetAddress] = {
    //NetworkInterface
    //  .getNetworkInterfaces
    //  .flatMap { i =>
    //  JavaConverters.enumerationAsScalaIterator(i).map(_.get.getInetAddresses).toList
    //}//.toList
    List.empty
  }

  /**
   * Filter for ipv4 addresses
   */
  val ipv4Filter: (InetAddress) => Boolean = _.isInstanceOf[Inet4Address]

  /**
   * Match by host address.
   *
   * NetUtils.getAllLocalIPAddresses.filter(NetUtils.matchAddress("192.168.178"))
   */
  def matchAddress(value: String): (InetAddress) => Boolean = {
    { _.getHostAddress.startsWith(value) }
  }

  /**
   * Match by cidr.
   *
   * val r1 = NetUtils.findBestMatchingAsCIDR(NetUtils.getAllLocalIPAddresses, "192.168.178.1")
   * val r2 = NetUtils
   *   .getAllLocalIPAddresses
   *   .filter(NetUtils.ipv4Filter)
   *   .filter(NetUtils.matchNetmask(r1.get))
   */
  def matchCIDR(cidrNotation: String): (InetAddress) => Boolean = {
    val subnet = new SubnetUtils(cidrNotation).getInfo

    { i => subnet.isInRange(i.getHostAddress) }
  }
}
