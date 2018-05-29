package de.pfke.grind.refl.richRuntimeMirror.mocks

case class CaseClassMock2Args_wMethods(
  arg1: String,
  arg2: Int
) {
  def method1() = "method1"
  def method2(var1: String, var2: Int) = s"method2: $var1, $var2"
}
