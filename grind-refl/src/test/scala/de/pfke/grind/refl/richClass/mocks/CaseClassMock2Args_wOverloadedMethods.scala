package de.pfke.grind.refl.richClass.mocks

case class CaseClassMock2Args_wOverloadedMethods(
  arg1: List[String],
  arg2: CaseClassMock0Args
) {
  def method1() = "method1"
  def method2(var1: String) = s"method2(String): $var1"
  def method2(var1: String, var2: Int) = s"method2(String, Int): $var1, $var2"
  def method2(var1: Byte, var2: String) = s"method2(Byte, String): $var1, $var2"
}
