package de.pfke.grind.io.jnio

import java.io.{ByteArrayOutputStream, InputStream, OutputStream}
import java.nio.ByteBuffer
import java.nio.charset.{Charset, StandardCharsets}
import java.nio.file._
import java.nio.file.attribute.{FileAttribute, PosixFilePermissions}

import de.pfke.grind.core.crypto.{ChecksumAlgorithm, ChecksumOps}
import de.pfke.grind.core.crypto.ChecksumAlgorithm.ChecksumAlgorithm
import de.pfke.grind.core.data.byTypes.complex.{ByteBufferOps, ByteBufferOpsIncludes}

import scala.collection.JavaConverters

object PathOps {
  val MAX_SEARCH_DEPTH: Int = Int.MaxValue

  /**
    * Calc checksum from path
    */
  def checksum (
    algo: ChecksumAlgorithm = ChecksumAlgorithm.SHA_256
  ) (
    in: Path
  ): Array[Byte] = ChecksumOps.fromPath(algo)(in)

  /**
    * Copy source file to dest
    *
    * @param source is the source file
    * @param dest is the destination file
    * @param copyOptions use <code>StandardCopyOption</code>
    */
  def cp (
    source: Path
  ) (
    dest: Path,
    copyOptions: CopyOption*
  ): Path = Files.copy(source, dest, copyOptions:_*)

  /**
    * Create file and dir if necessary
    */
  def createFile (
    in: String,
    attrs: FileAttribute[_]*
  ): Path = createFile(in = Paths.get(in), attrs = attrs:_*)

  /**
    * Create file and dir if necessary
    */
  def createDirectory (
    in: Path,
    attrs: FileAttribute[_]*
  ): Path = {
    if (Files.exists(in) && Files.isDirectory(in)) { return in }

    require(!Files.exists(in), s"the given file already exists: $in")

    Files.createDirectories(in, attrs:_*)
  }

  /**
    * Create file and dir if necessary
    */
  def createFile (
    in: Path,
    attrs: FileAttribute[_]*
  ): Path = {
    require(!Files.exists(in), s"the given file already exists: $in")
    require(
      (Files.exists(in.getParent) && Files.isDirectory(in.getParent)) || !Files.exists(in.getParent),
      s"the parent exists and is not a directory: ${in.getParent}"
    )

    Files.createDirectories(in.getParent, attrs:_*)
    Files.createFile(in, attrs:_*)
  }

  /**
    * Creates a tmp directory with the given name..
    */
  def createTempDirectory (
    prefix: Option[String] = None
  ) (
    attrs: FileAttribute[_]*
  ): Path = Files.createTempDirectory(prefix.orNull, attrs:_*)

  /**
    * Creates a tmp directory with the given name..
    */
  def createTempFile (
    prefix: Option[String] = None,
    suffix: Option[String] = None
  ) (
    attrs: FileAttribute[_]*
  ): Path = Files.createTempFile(prefix.orNull, suffix.orNull, attrs:_*)

  /**
    * Return the filename w/o file extension + w/o path
    */
  def basename (in: Path): String = extractFilename(filename(in = in))._1

  /**
    * Return the file extension.
    */
  def extension (in: Path): String = extractFilename(filename(in = in))._2

  /**
    * Return the filename w/ file extension
    */
  def filename (in: Path): String = in.getFileName.toString

  def isEmpty (in: Path): Boolean = Files.size(in) == 0
  def nonEmpty (in: Path): Boolean = !isEmpty(in)

  def inputStream (in: Path): InputStream = Files.newInputStream(in)
  def outputStream (in: Path): OutputStream = Files.newOutputStream(in)

  /**
    * Return file size as byte length.
    */
  def size (in: Path): Long = {
    if (Files.isDirectory(in)) {
      flatTree(in).foldLeft(0l)((sum,file) => sum + size(file))
    } else {
      Files.size(in)
    }
  }

  /**
    * Move source file to dest
    *
    * @param source is the source file
    * @param dest is the destination file
    * @param copyOptions use <code>StandardCopyOption</code>
    */
  def mv (
    source: Path,
    dest: Path,
    copyOptions: CopyOption*
  ): Path = Files.move(source, dest, copyOptions:_*)

  /**
    * Remove given path
    *
    * @param source path to delete
    * @param force set to true whether its a directory
    */
  def rm (
    source: Path
  ) (
    implicit
    force: Boolean = false
  ): Boolean = {
    if (!Files.exists(source)) {
      return true
    }

    if(Files.isDirectory(source) && force) {
      flatTree(source)
        .filterNot(_ == source)
        .foreach(rm)
    }

    Files.deleteIfExists(source)
  }

  /**
    * Recursive method to return all childs and child-childs of the given file.
    */
  def deepTree (
    maxDepth: Int = MAX_SEARCH_DEPTH,
    currentDepth: Int = 0
  ) (
    startFile: Path
  ): Stream[Path] = {
    val childs = startFile match {
      case t if Files.isDirectory(t) && currentDepth < maxDepth =>
        Stream.apply(JavaConverters.asScalaIterator(Files.list(t).iterator()).toSeq:_*)

      case _ => Stream.empty[Path]
    }

    val fn = deepTree(maxDepth = maxDepth, currentDepth = currentDepth + 1) _

    startFile #:: childs.flatMap(fn)
  }

  /**
    * This method scans the given path/file and return all direct child files/directories as a stream.
    */
  def flatTree (in: Path): Stream[Path] = deepTree(maxDepth = 1)(startFile = in).filterNot(_ == in)

  /**
    * Read the file binary.
    */
  def read (
    in: Path
  ): ByteBuffer = {
    if (!Files.exists(in))
      ByteBuffer.allocateDirect(0)
    else {
      ByteBuffer
        .wrap(
          ChannelTools
            .copyStreams(
              Files.newInputStream(in),
              new ByteArrayOutputStream(size(in = in).toInt),
              closeOutput = false
            )
            .toByteArray
        )
    }
  }

  /**
    * Write something to file.
    */
  def write (
    writeInto: Path,
    data: InputStream
  ): Path = {
    if (!Files.exists(writeInto)) {
      val perms = PosixFilePermissions.fromString("rwxr-x---")
      val attr = PosixFilePermissions.asFileAttribute(perms)

      Files.createDirectories(writeInto.getParent, attr)
      Files.createFile(writeInto, attr)
    }

    ChannelTools.copyStreams(data, outputStream(writeInto))
    writeInto
  }

  /**
    * Write something to file.
    */
  def write (
    writeInto: Path,
    data: ByteBuffer
  ): Path = write(writeInto = writeInto, ByteBufferOpsIncludes.ByteBufferToInputStream(data))

  /**
    * Extract filename to name and extension.
    */
  private def extractFilename (
    str: String
  ): (String, String) = {
    // fields
    val filenameRegex = """(.*)\.(\S*)$""".r

    str match {
      case filenameRegex(n,e) => (n, e)
      case t                  => (t, "")
    }}
}

object PathOpsIncludes
  extends PathOpsIncludes

trait PathOpsIncludes {
  implicit class PathOpsImplicit_fromPath (
    in: Path
  ) {
    def checksum (implicit algo: ChecksumAlgorithm = ChecksumAlgorithm.SHA_256): Array[Byte] = PathOps.checksum(algo)(in = in)

    def basename: String = PathOps.basename(in = in)
    def ext: String = PathOps.extension(in = in)
    def filename: String = PathOps.filename(in = in)

    def isDirectory: Boolean = Files.isDirectory(in)
    def isFile: Boolean = Files.isRegularFile(in)

    def isEmpty: Boolean = PathOps.isEmpty(in = in)
    def nonEmpty: Boolean = PathOps.nonEmpty(in = in)

    def inputStream: InputStream = PathOps.inputStream(in = in)
    def outputStream: OutputStream = PathOps.outputStream(in = in)

    def size: Long = PathOps.size(in = in)

    def cp (dest: Path, copyOptions: CopyOption*): Path = PathOps.cp(source = in)(dest = dest, copyOptions = copyOptions:_*)
    def mv (dest: Path, copyOptions: CopyOption*): Path = PathOps.mv(source = in, dest = dest, copyOptions = copyOptions:_*)
    def rm (implicit force: Boolean = false): Boolean = PathOps.rm(source = in)

    def createDirectory (attrs: FileAttribute[_]*): Path = PathOps.createDirectory(in = in, attrs = attrs:_*)
    def createFile (attrs: FileAttribute[_]*): Path = PathOps.createFile(in = in, attrs = attrs:_*)
    def deepTree (maxDepth: Int = PathOps.MAX_SEARCH_DEPTH): Stream[Path] = PathOps.deepTree(maxDepth = maxDepth)(startFile = in)
    def flatTree: Stream[Path] = PathOps.flatTree(in = in)

    def read: ByteBuffer = PathOps.read(in = in)
    def write (data: InputStream): Path = PathOps.write(writeInto = in, data = data)
    def write (data: ByteBuffer): Path = PathOps.write(writeInto = in, data = data)

  }

  implicit class PathOpsImplicit_fromString (
    in: String
  ) {
    def createFile (attrs: FileAttribute[_]*): Path = PathOps.createFile(in = in, attrs = attrs:_*)
    def createTempDirectory (attrs: FileAttribute[_]*): Path = PathOps.createTempDirectory(prefix = Some(in))(attrs = attrs:_*)
    def createTempFile (attrs: FileAttribute[_]*): Path = PathOps.createTempFile(prefix = Some(in))(attrs = attrs:_*)
  }

  implicit class PathOpsImplicit_fromPaths[A <: Traversable[Path]] (
    in: A
  ) {
    def filterByName (
      regex: String
    ): Traversable[Path] = in.filter(_.toString.matches(regex))

    /**
      * Filter out all directories
      */
    def filterNotDir: Traversable[Path] = in.filterNot(Files.isDirectory(_))

    /**
      * Filter out all directories
      */
    def filterNotLink: Traversable[Path] = in.filterNot(f=> Files.isSymbolicLink(FileSystems.getDefault.getPath(f.toString)))

    /**
      * This method takes the file collection and group all files by its base name.
      * You will get a Map[String, A]
      */
    def groupByBaseName: Map[String, Traversable[Path]] = in.groupBy { f=> f.basename }

    /**
      * Read given files and pass content to the given parse function.
      * If content is undefined at op, the fully content will be returned.
      */
    def parseBy[B] (
      op: PartialFunction[String, B]
    ) (
      implicit
      charset: Charset = StandardCharsets.UTF_8
    ): Map[Path, B] = in.map { f => f -> op(ByteBufferOps.asString(PathOps.read(f))) }.toMap
  }
}
