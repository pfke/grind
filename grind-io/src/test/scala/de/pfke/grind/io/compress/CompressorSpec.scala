package de.pfke.grind.io.compress



//import de.pintono.grind.testing.PathHelper
//import de.pintono.grind.core.crypto.ChecksumCalculator
//import de.pintono.grind.io.jnio._
//import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream
//import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
//import org.scalatest.{Matchers, WordSpecLike}
//
//import scala.concurrent.duration._
//import scala.language.postfixOps
//
//class CompressorSpec
//  extends WordSpecLike
//  with Matchers {
//  private val checksumFromFile = ChecksumCalculator.fromFile() _
//  private val checksumFromIS = ChecksumCalculator.fromInputStream() _
//
//  "A compressor object" when {
//    "compress to a bz2 archive" should {
//      "return a new file object with bz2 extension" in {
//        Compressor.compress(PathHelper.createRandomFile("file01"), algorithm = CompressorType.BZIP2).extension should be ("bz2")
//      }
//
//      "return a valid archive" in {
//        val file1 = PathHelper.createRandomFile("file01")
//        val archive = Compressor.compress(file1, algorithm = CompressorType.BZIP2)
//        val bzIn = new BZip2CompressorInputStream(archive.inputStream)
//
//        checksumFromIS(bzIn) should be (checksumFromFile(file1.toFile))
//      }
//
//      "use bz2 as default compressor" in {
//        val file1 = PathHelper.createRandomFile("file01")
//        val archive = Compressor.compress(file1)
//        val bzIn = new BZip2CompressorInputStream(archive.inputStream)
//
//        checksumFromIS(bzIn) should be (checksumFromFile(file1.toFile))
//      }
//
//      "return a valid archive when compressed twice" in {
//        val file1 = PathHelper.createRandomFile("file01")
//        Compressor.compress(file1)
//
//        PathHelper.writeToFile(file1, "kk")
//        val archive = Compressor.compress(file1)
//
//        val bzIn = new BZip2CompressorInputStream(archive.inputStream)
//
//        checksumFromIS(bzIn) should be (checksumFromFile(file1.toFile))
//      }
//    }
//
//    "compress to a gzip archive" should {
//      "return a new file object with gz extension" in {
//        Compressor.compress(PathHelper.createRandomFile("file01"), algorithm = CompressorType.GZIP).extension should be ("gz")
//      }
//
//      "return a valid archive" in {
//        val file1 = PathHelper.createRandomFile("file01")
//        val archive = Compressor.compress(file1, algorithm = CompressorType.GZIP)
//        val bzIn = new GzipCompressorInputStream(archive.inputStream)
//
//        checksumFromIS(bzIn) should be (checksumFromFile(file1.toFile))
//      }
//    }
//
//    "unCompress from a bz2 archive" should {
//      implicit val timeout = 30 seconds
//
//      "return a new file object without bz2 extension" in {
//        val file1 = PathHelper.createRandomFile("file01")
//        val archive = Compressor.compress(file1)
//
//        file1.delete()
//        val uncomp = Compressor.unCompress(archive)
//
//        uncomp.name should be (file1.name)
//      }
//
//      "return a valid file" in {
//        val file1 = PathHelper.createRandomFile("file01")
//        val archive = Compressor.compress(file1)
//
//        val org = Files.createTempFile("klkö", null)
//        file1.move(org, StandardCopyOption.REPLACE_EXISTING)
//        val uncomp = Compressor.unCompress(archive)
//
//        checksumFromFile(uncomp.toFile) should be (checksumFromFile(org.toFile))
//      }
//    }
//  }
//}
