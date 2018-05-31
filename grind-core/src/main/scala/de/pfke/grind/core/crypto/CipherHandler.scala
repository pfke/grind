package de.pfke.grind.core.crypto

import de.pfke.grind.core.crypto.CipherAlgorithm.CipherAlgorithm
import javax.crypto.{Cipher, SecretKey}

object CipherAlgorithm
  extends Enumeration {
  type CipherAlgorithm = Value

  val AES_CBC_NoPadding: Value = Value("AES/CBC/NoPadding")
  val AES_CBC_PKCS5Padding: Value = Value("AES/CBC/PKCS5Padding")
  val AES_ECB_NoPadding: Value = Value("AES/ECB/NoPadding")
  val AES_ECB_PKCS5Padding: Value = Value("AES/ECB/PKCS5Padding")
  val DES_CBC_NoPadding: Value = Value("DES/CBC/NoPadding")
  val DES_CBC_PKCS5Padding: Value = Value("DES/CBC/PKCS5Padding")
  val DES_ECB_NoPadding: Value = Value("DES/ECB/NoPadding")
  val DES_ECB_PKCS5Padding: Value = Value("DES/ECB/PKCS5Padding")
  val DESede_CBC_NoPadding: Value = Value("DESede/CBC/NoPadding")
  val DESede_CBC_PKCS5Padding: Value = Value("DESede/CBC/PKCS5Padding")
  val DESede_ECB_NoPadding: Value = Value("DESede/ECB/NoPadding")
  val DESede_ECB_PKCS5Padding: Value = Value("DESede/ECB/PKCS5Padding")
  val RSA_ECB_PKCS1Padding: Value = Value("RSA/ECB/PKCS1Padding")
  val RSA_ECB_OAEPWithSHA_1AndMGF1Padding: Value = Value("RSA/ECB/OAEPWithSHA-1AndMGF1Padding")
  val RSA_ECB_OAEPWithSHA_256AndMGF1Padding: Value = Value("RSA/ECB/OAEPWithSHA-256AndMGF1Padding")
}

object CipherHandler {

}

class CipherHandler(
  algorithm: CipherAlgorithm
) {
  def decrypt (
    data: Array[Byte],
    secretKey: SecretKey
  ): Array[Byte] = {
    val cipher = Cipher.getInstance(algorithm.toString)
    cipher.init(Cipher.DECRYPT_MODE, secretKey)

    cipher.doFinal(data)
  }

  def encrypt (
    data: String,
    secretKey: SecretKey
  ): Array[Byte] = encrypt(data = data.getBytes, secretKey = secretKey)

  def encrypt (
    data: Array[Byte],
    secretKey: SecretKey
  ): Array[Byte] = {
    val cipher = Cipher.getInstance(algorithm.toString)

    cipher.init(Cipher.ENCRYPT_MODE, secretKey)

    cipher.doFinal(data)
  }
}
