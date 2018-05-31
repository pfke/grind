package de.pfke.grind.core.crypto

import de.pfke.grind.core.crypto.SecretKeyAlgorithm.SecretKeyAlgorithm
import javax.crypto.spec.{PBEKeySpec, SecretKeySpec}
import javax.crypto.{SecretKey, SecretKeyFactory}

object SecretKeyAlgorithm
  extends Enumeration {
  type SecretKeyAlgorithm = Value

  val AES: Value = Value("AES")                                // Constructs secret keys for use with the AES algorithm.
  val ARCFOUR: Value = Value("ARCFOUR")                        // Constructs secret keys for use with the ARCFOUR algorithm.
  val DES: Value = Value("DES")                                // Constructs secrets keys for use with the DES algorithm.
  val DESede: Value = Value("DESede")                          // Constructs secrets keys for use with the DESede (Triple-DES) algorithm.
  val PBKDF2WithHmacSHA1: Value = Value("PBKDF2WithHmacSHA1")  // Constructs secret keys using the Password-Based Key Derivation Function function found in PKCS #5 v2.0.
}

object SecretKeyGenerator {
  val DEFAULT_ITERATIONS = 10000

  /**
    * Make a 128 bit AES key form the given passphrase and flavor it with the given salt.
    *
    * @param algorithm default is 'PBKDF2WithHmacSHA1', see https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#SecretKeyFactory
    * @param iterations number to iterate key generation
    * @param salt use this salt
    * @param passphrase user pass phrase to generate key from
    * @return AES key
    */
  def make128BitAESKey (
    algorithm: SecretKeyAlgorithm = SecretKeyAlgorithm.PBKDF2WithHmacSHA1,
    iterations: Int = DEFAULT_ITERATIONS,
    salt: String
  ) (
    passphrase: String
  ): SecretKey = makeAESKey(algorithm, iterations = iterations, salt = salt, bitCount = 128)(passphrase = passphrase)

  /**
    * Make a 192 bit AES key form the given passphrase and flavor it with the given salt.
    *
    * @param algorithm default is 'PBKDF2WithHmacSHA1', see https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#SecretKeyFactory
    * @param iterations number to iterate key generation
    * @param salt use this salt
    * @param passphrase user pass phrase to generate key from
    * @return AES key
    */
  def make192BitAESKey (
    algorithm: SecretKeyAlgorithm = SecretKeyAlgorithm.PBKDF2WithHmacSHA1,
    iterations: Int = DEFAULT_ITERATIONS,
    salt: String
  ) (
    passphrase: String
  ): SecretKey = makeAESKey(algorithm, iterations = iterations, salt = salt, bitCount = 192)(passphrase = passphrase)

  /**
    * Make a 256 bit AES key form the given passphrase and flavor it with the given salt.
    *
    * @param algorithm default is 'PBKDF2WithHmacSHA1', see https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#SecretKeyFactory
    * @param iterations number to iterate key generation
    * @param salt use this salt
    * @param passphrase user pass phrase to generate key from
    * @return AES key
    */
  def make256BitAESKey (
    algorithm: SecretKeyAlgorithm = SecretKeyAlgorithm.PBKDF2WithHmacSHA1,
    iterations: Int = DEFAULT_ITERATIONS,
    salt: String
  ) (
    passphrase: String
  ): SecretKey = makeAESKey(algorithm, iterations = iterations, salt = salt, bitCount = 256)(passphrase = passphrase)

  /**
    * Make a 512 bit AES key form the given passphrase and flavor it with the given salt.
    *
    * @param algorithm default is 'PBKDF2WithHmacSHA1', see https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#SecretKeyFactory
    * @param iterations number to iterate key generation
    * @param salt use this salt
    * @param passphrase user pass phrase to generate key from
    * @return AES key
    */
  def make512BitAESKey (
    algorithm: SecretKeyAlgorithm = SecretKeyAlgorithm.PBKDF2WithHmacSHA1,
    iterations: Int = DEFAULT_ITERATIONS,
    salt: String
  ) (
    passphrase: String
  ): SecretKey = makeAESKey(algorithm, iterations = iterations, salt = salt, bitCount = 512)(passphrase = passphrase)

  /**
    * Make a xxx bit AES key form the given passphrase and flavor it with the given salt.
    *
    * @param algorithm algo to use
    * @param iterations number to iterate key generation
    * @param salt use this salt
    * @param bitCount how many bits to use
    * @param passphrase user pass phrase to generate key from
    * @return AES key
    */
  def makeAESKey (
    algorithm: SecretKeyAlgorithm,
    iterations: Int,
    salt: String,
    bitCount: Int
  ) (
    passphrase: String
  ): SecretKey = {
    /*
    PBKDF2 is an algorithm specially designed for generating keys from passwords that is considered more secure than a simple SHA1 hash.
    The salt ensures your encryption won't match another encryption using the same key and cleartext and helps prevent dictionary attacks.
    The iterations value is an adjustable parameter. Higher values use more computing power, making brute force attacks more difficult.
     */
    val key = SecretKeyFactory
      .getInstance(algorithm.toString)
      .generateSecret(
        new PBEKeySpec(
          passphrase.toCharArray,
          salt.getBytes,
          iterations,
          bitCount
        ))
      .getEncoded

    new SecretKeySpec(key, "AES")
  }
}

class SecretKeyGenerator (
  algorithm: SecretKeyAlgorithm
) {
  /**
    * Make a 128 bit AES key form the given passphrase and flavor it with the given salt.
    */
  def make128BitAESKey (
    iterations: Int = SecretKeyGenerator.DEFAULT_ITERATIONS,
    salt: String
  ) (
    passphrase: String
  ): SecretKey = SecretKeyGenerator.make128BitAESKey(algorithm, iterations, salt)(passphrase)

  /**
    * Make a 192 bit AES key form the given passphrase and flavor it with the given salt.
    */
  def make192BitAESKey (
    iterations: Int = SecretKeyGenerator.DEFAULT_ITERATIONS,
    salt: String
  ) (
    passphrase: String
  ): SecretKey = SecretKeyGenerator.make192BitAESKey(algorithm, iterations, salt)(passphrase)

  /**
    * Make a 256 bit AES key form the given passphrase and flavor it with the given salt.
    */
  def make256BitAESKey (
    iterations: Int = SecretKeyGenerator.DEFAULT_ITERATIONS,
    salt: String
  ) (
    passphrase: String
  ): SecretKey = SecretKeyGenerator.make256BitAESKey(algorithm, iterations, salt)(passphrase)

  /**
    * Make a 512 bit AES key form the given passphrase and flavor it with the given salt.
    */
  def make512BitAESKey (
    iterations: Int = SecretKeyGenerator.DEFAULT_ITERATIONS,
    salt: String
  ) (
    passphrase: String
  ): SecretKey = SecretKeyGenerator.make512BitAESKey(algorithm, iterations, salt)(passphrase)

  /**
    * Make a xxx bit AES key form the given passphrase and flavor it with the given salt.
    */
  def makeAESKey (
    iterations: Int = SecretKeyGenerator.DEFAULT_ITERATIONS,
    salt: String,
    bitCount: Int = 512
  ) (
    passphrase: String
  ): SecretKey = SecretKeyGenerator.makeAESKey(algorithm, iterations, salt, bitCount)(passphrase)
}

object SecretKeyIncludes
  extends SecretKeyIncludes

trait SecretKeyIncludes {
  implicit class FromSecretKeyAlgorithm(
    algorithm: SecretKeyAlgorithm
  ) {
    // fields
    private val _calculator = new SecretKeyGenerator(algorithm)

    /**
      * Make a 128 bit AES key form the given passphrase and flavor it with the given salt.
      */
    def make128BitAESKey (
      iterations: Int = SecretKeyGenerator.DEFAULT_ITERATIONS,
      salt: String
    ) (
      passphrase: String
    ): SecretKey = _calculator.make128BitAESKey(iterations, salt)(passphrase)

    /**
      * Make a 192 bit AES key form the given passphrase and flavor it with the given salt.
      */
    def make192BitAESKey (
      iterations: Int = SecretKeyGenerator.DEFAULT_ITERATIONS,
      salt: String
    ) (
      passphrase: String
    ): SecretKey = _calculator.make192BitAESKey(iterations, salt)(passphrase)

    /**
      * Make a 256 bit AES key form the given passphrase and flavor it with the given salt.
      */
    def make256BitAESKey (
      iterations: Int = SecretKeyGenerator.DEFAULT_ITERATIONS,
      salt: String
    ) (
      passphrase: String
    ): SecretKey = _calculator.make256BitAESKey(iterations, salt)(passphrase)

    /**
      * Make a 512 bit AES key form the given passphrase and flavor it with the given salt.
      */
    def make512BitAESKey (
      iterations: Int = SecretKeyGenerator.DEFAULT_ITERATIONS,
      salt: String
    ) (
      passphrase: String
    ): SecretKey = _calculator.make512BitAESKey(iterations, salt)(passphrase)

    /**
      * Make a xxx bit AES key form the given passphrase and flavor it with the given salt.
      */
    def makeAESKey (
      iterations: Int = SecretKeyGenerator.DEFAULT_ITERATIONS,
      salt: String,
      bitCount: Int = 512
    ) (
      passphrase: String
    ): SecretKey = _calculator.makeAESKey(iterations, salt, bitCount)(passphrase)
  }
}
