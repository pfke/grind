package de.pfke.grind.core.refl.richClass.mocks

object ObjectMock2Args_wOverloadedMethods {
  def method1() = "method1"
  def method2(var1: String) = s"method2(String): $var1"
  def method2(var1: String, var2: Int) = s"method2(String, Int): $var1, $var2"
  def method2(var1: Byte, var2: String, var3: Long) = s"method2(Byte, String): $var1, $var2"
}
