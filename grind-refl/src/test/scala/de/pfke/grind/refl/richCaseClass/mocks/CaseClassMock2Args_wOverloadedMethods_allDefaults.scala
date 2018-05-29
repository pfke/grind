package de.pfke.grind.refl.richCaseClass.mocks

case class CaseClassMock2Args_wOverloadedMethods_allDefaults(
  arg1: String = "",
  arg2: Int = 13
) {
  def method1() = "method1"
  def method2(var1: String) = s"method2(String): $var1"
  def method2(var1: String, var2: Int) = s"method2(String, Int): $var1, $var2"
  def method2(var1: Byte, var2: String, var3: Long) = s"method2(Byte, String, Long): $var1, $var2, $var3"
}
