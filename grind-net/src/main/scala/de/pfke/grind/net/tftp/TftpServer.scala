package de.pfke.grind.net.tftp

import java.net.InetAddress
import java.nio.file.Path
import java.util.UUID

import de.pfke.grind.net.tftp.TftpServerMode._
import org.apache.commons.net.tftp.TFTP

import scala.collection.mutable
import scala.concurrent.duration.Duration

/**
  * A fully multi-threaded tftp server. Can handle multiple clients at the same time. Implements RFC
  * 1350 and wrapping block numbers for large file support.
  *
  * To launch, just create an instance of the class. An IOException will be thrown if the server
  * fails to start for reasons such as port in use, port denied, etc.
  *
  * To stop, use the shutdown method.
  *
  * To check to see if the server is still running (or if it stopped because of an error), call the
  * isRunning() method.
  *
  * By default, events are not logged to stdout/stderr. This can be changed with the
  * setLog and setLogError methods.
  *
  * <p>
  * Example usage is below:
  *
  * <code>
  * public static void main(String[] args) throws Exception
  *  {
  *      if (args.length != 1)
  *      {
  *          System.out
  *                  .println("You must provide 1 argument - the base path for the server to serve from.");
  *          System.exit(1);
  *      }
  *
  *      TFTPServer ts = new TFTPServer(new File(args[0]), new File(args[0]), GET_AND_PUT);
  *      ts.setSocketTimeout(2000);
  *
  *      System.out.println("TFTP Server running.  Press enter to stop.");
  *      new InputStreamReader(System.in).read();
  *
  *      ts.shutdown();
  *      System.out.println("Server shut down.");
  *      System.exit(0);
  *  }
  *
  * </code>
  *
  *
  * @author <A HREF="mailto:daniel.armbrust.list@gmail.com">Dan Armbrust</A>
  * @since 2.0
  */
//object TftpServer {
//  // msg classes
//  sealed trait TftpServerMsg
//  case class TftpFileTransmit(file: Path, size: ByteLength, durationInMS: Duration, speed: String) extends TftpServerMsg
//
//  /**
//    * Simply create tftp server.
//    */
//  def apply (
//    workingPath: Path,
//    serverMode: TFTPServerMode = TftpServerMode.GET_AND_PUT
//  ) = new TftpServer(passedWorkingPath = Some(workingPath), serverMode = serverMode)
//
//  /**
//    * Simply create tftp server.
//    */
//  def default() = new TftpServer(passedWorkingPath = None)
//}

/**
  * Start a TFTP Server on the specified port. Gets and Puts occur in the specified directory.
  *
  * The server will start in another thread, allowing this constructor to return immediately.
  *
  * If a get or a put comes in with a relative path that tries to get outside of the
  * serverDirectory, then the get or put will be denied.
  *
  * GET_ONLY mode only allows gets, PUT_ONLY mode only allows puts, and GET_AND_PUT allows both.
  * Modes are defined as int constants in this class.
  *
  * @param passedWorkingPath directory for PUT requests
  * @param serverMode A value as specified above.
  */
//class TftpServer (
//  passedWorkingPath: Option[Path] = None,
//  private[tftp] val serverMode: TFTPServerMode = TftpServerMode.GET_AND_PUT
//)
//  extends Runnable
//    with RxBus[GrindEvent]
//    with RunLevelEventReporter
//    with TrafficEventReporter {
//  // fields
//  private val _tftp = new TFTP()
//  private lazy val _workingPath = createWorkingPath()
//  private val _pendingTransfers = new mutable.HashSet[TftpTransfer]()
//
//  private var _isRunning: Boolean = true
//
//  private var _thrownException: Exception = _
//
//  private var _maxTimeoutRetries = 3
//  private var _socketTimeout = _tftp.getDefaultTimeout
//  private val _serverThread = new Thread(this)
//
//  private val _fileReadReporter  = {(f: Path, size: ByteLength, durationInMS: Duration) => reportTrafficTx(TftpFileTransmit(file = f, size = size, durationInMS = durationInMS, speed = size.asSpeed(durationInMS))) }
//  private val _fileWriteReporter = {(f: Path, size: ByteLength, durationInMS: Duration) => reportTrafficRx(TftpFileTransmit(file = f, size = size, durationInMS = durationInMS, speed = size.asSpeed(durationInMS))) }
//
//  // ctor
//  launch()
//
//  /**
//    * Return the tftp server local address
//    */
//  def localAddress: InetAddress = _tftp.getLocalAddress
//
//  /**
//    * Return the used server directory.
//    */
//  def localDirectory: Path = _workingPath
//
//  /**
//    * Return the tftp server local port
//    */
//  def localPort: Int = _tftp.getLocalPort
//
//  /**
//    * Set the max number of retries in response to a timeout. Default 3. Min 0.
//    *
//    * @param v new val
//    */
//  def maxTimeoutRetries_=(v: Int) {
//    require(v >= 0, "Invalid value for 'maxTimeoutRetries'")
//
//    _maxTimeoutRetries = v
//  }
//  /**
//    * Get the current value for maxTimeoutRetries
//    */
//  def maxTimeoutRetries: Int = _maxTimeoutRetries
//
//  /**
//    * Set the socket timeout in milliseconds used in transfers. Defaults to the value here:
//    * http://commons.apache.org/net/apidocs/org/apache/commons/net/tftp/TFTP.html#DEFAULT_TIMEOUT
//    * (5000 at the time I write this) Min value of 10.
//    */
//  def socketTimeout_=(v: Int) {
//    require(v >= 10, "Invalid value for 'socketTimeout'")
//
//    _socketTimeout = v
//  }
//  /**
//    * The current socket timeout used during transfers in milliseconds.
//    */
//  def socketTimeout: Int = _socketTimeout
//
//  /**
//    * Shut this shit down.
//    */
//  override def finalize() {
//    super.finalize()
//    shutdown()
//  }
//
//  /**
//    * check if the server thread is still running.
//    *
//    * @return true if running, false if stopped.
//    */
//  def isRunning: Boolean = {
//    if (!_isRunning && _thrownException != null)
//    {
//      throw _thrownException
//    }
//    _isRunning
//  }
//
//  /**
//    * Thread method.
//    */
//  def run() {
//    try {
//      while (_isRunning) {
//        val tftpPacket = _tftp.receive()
//
//        val  tt = new TftpTransfer(this, tftpPacket, _fileReadReporter, _fileWriteReporter)
//        this.synchronized {
//          _pendingTransfers.add(tt)
//        }
//
//        val thread = new Thread(tt)
//        thread.setDaemon(true)
//        thread.start()
//      }
//      reportRunLevelAfterShutdown()
//    } catch {
//      case e: Exception =>
//        if (_isRunning) {
//          _thrownException = e
//          reportRunLevelAfterShutdown(s"Unexpected Error in TFTP Server - Server shut down! (${e.getMessage}})")
//        }
//    } finally {
//      _isRunning = false; // set this to true, so the launching thread can check to see if it started.
//
//      if (_tftp != null && _tftp.isOpen) {
//        _tftp.close()
//      }
//    }
//  }
//
//  /**
//    * Stop the tftp server (and any currently running transfers) and release all opened network
//    * resources.
//    */
//  def shutdown() {
//    reportRunLevelBeforeShutdown()
//    _isRunning = false
//
//    this.synchronized {
//      _pendingTransfers.foreach(_.shutdown())
//    }
//
//    try {
//      _tftp.close()
//    } catch {
//      case _: RuntimeException => // nop
//    }
//
//    try {
//      _serverThread.join()
//    } catch {
//      case _: InterruptedException => // we've done the best we could, return
//    }
//  }
//
//  /**
//    * Remove the given transfer from list.
//    *
//    * @param transfer transfer which want to be removed
//    */
//  private[tftp] def remove(
//    transfer: TftpTransfer
//  ) {
//    this.synchronized {
//      _pendingTransfers -= transfer
//    }
//  }
//
//  /**
//    * start the server, throw an error if it can't start.
//    */
//  private def launch() {
//    // we want the server thread to listen forever.
//    _tftp.setDefaultTimeout(0)
//    _tftp.open()
//
//    _serverThread.setDaemon(true)
//    _serverThread.start()
//
//    reportRunLevelRunning()
//  }
//
//  /**
//    * Either create a temp dir or take the passed one.
//    */
//  private def createWorkingPath(): Path = {
//    passedWorkingPath match {
//      case Some(x) if x.exists && x.isADirectory => x
//      case Some(x) if !x.exists                  => x.createDirectory()
//      case Some(x)                               => throw new IllegalArgumentException(s"given path ($x) exists and is NOT a directory")
//      case None                                  => s"tftp-$localPort-${UUID.randomUUID()}".createTempDirectory()
//    }}
//}
