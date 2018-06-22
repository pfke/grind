package de.pfke.grind.net.telnet

import java.io.{FileOutputStream, IOException}
import java.util.StringTokenizer

import org.apache.commons.net.telnet._
import org.apache.commons.net.{telnet => at}

object ApacheTelnetRunner {
  def main (args: Array[String]): Unit = {
    new ApacheTelnetRunner(Array("192.168.178.23", "23"))
  }
}

class ApacheTelnetRunner(
  args: Array[String]
) extends Runnable with TelnetNotificationHandler {
  val tc = new at.TelnetClient()

  init(args)

  def init(args: Array[String]) {
    var fout: FileOutputStream = null

    if(args.length < 1) {
      System.err.println("Usage: TelnetClientExample <remote-ip> [<remote-port>]");
      System.exit(1);
    }

    val remoteip = args(0)
    var remoteport = 0

    if (args.length > 1) {
      remoteport = (new Integer(args(1))).intValue()
    } else {
      remoteport = 23
    }

    try {
      fout = new FileOutputStream ("spy.log", true);
    } catch {
      case e: IOException => System.err.println("Exception while opening the spy file: " + e.getMessage)
    }

    val ttopt = new TerminalTypeOptionHandler("VT100", false, false, true, false)
    val echoopt = new EchoOptionHandler(true, false, true, false)
    val gaopt = new SuppressGAOptionHandler(true, true, true, true)

    try {
      tc.addOptionHandler(ttopt)
      tc.addOptionHandler(echoopt)
      tc.addOptionHandler(gaopt)
    } catch {
      case e: InvalidTelnetOptionException => System.err.println("Error registering option handlers: " + e.getMessage)
    }

    while (true) {
      var end_loop = false

      try {
        tc.connect(remoteip, remoteport)

        val reader = new Thread (this)
        tc.registerNotifHandler(this)
        System.out.println("TelnetClientExample");
        System.out.println("Type AYT to send an AYT telnet command");
        System.out.println("Type OPT to print a report of status of options (0-24)");
        System.out.println("Type REGISTER to register a new SimpleOptionHandler");
        System.out.println("Type UNREGISTER to unregister an OptionHandler");
        System.out.println("Type SPY to register the spy (connect to port 3333 to spy)");
        System.out.println("Type UNSPY to stop spying the connection");
        System.out.println("Type ^[A-Z] to send the control character; use ^^ to send ^");

        reader.start()
        val outstr = tc.getOutputStream

        val buff = Array.ofDim[Byte](1024)
        var ret_read = 0

        do {
          try {
            ret_read = System.in.read(buff)

            if(ret_read > 0) {
              val line = new String(buff, 0, ret_read) // deliberate use of default charset

              if(line.startsWith("AYT")) {
                try {
                  System.out.println("Sending AYT")
                  System.out.println("AYT response:" + tc.sendAYT(5000))
                } catch {
                  case e: IOException => System.err.println("Exception waiting AYT response: " + e.getMessage)
                }
              } else if (line.startsWith("OPT")) {
                System.out.println("Status of options:")

                (0 to 25).foreach { ii =>
                  System.out.println("Local Option " + ii + ":" + tc.getLocalOptionState(ii) + " Remote Option " + ii + ":" + tc.getRemoteOptionState(ii))
                }
              } else if (line.startsWith("REGISTER")) {
                val st = new StringTokenizer(new String(buff))

                try {
                  st.nextToken()
                  val opcode = Integer.parseInt(st.nextToken())
                  val initlocal = java.lang.Boolean.parseBoolean(st.nextToken())
                  val initremote = java.lang.Boolean.parseBoolean(st.nextToken())
                  val acceptlocal = java.lang.Boolean.parseBoolean(st.nextToken())
                  val acceptremote = java.lang.Boolean.parseBoolean(st.nextToken())

                  val opthand = new SimpleOptionHandler(opcode, initlocal, initremote, acceptlocal, acceptremote)
                  tc.addOptionHandler(opthand)
                } catch {
                  case e: Exception =>
                    if(e.isInstanceOf[InvalidTelnetOptionException]) {
                      System.err.println("Error registering option: " + e.getMessage)
                    } else {
                      System.err.println("Invalid REGISTER command.")
                      System.err.println("Use REGISTER optcode initlocal initremote acceptlocal acceptremote")
                      System.err.println("(optcode is an integer.)")
                      System.err.println("(initlocal, initremote, acceptlocal, acceptremote are boolean)")
                    }
                }
              } else if (line.startsWith("UNREGISTER")) {
                val st = new StringTokenizer(new String(buff))

                try {
                  st.nextToken()
                  val opcode = (new Integer(st.nextToken())).intValue()
                  tc.deleteOptionHandler(opcode)
                } catch {
                  case e: Exception =>
                  if(e.isInstanceOf[InvalidTelnetOptionException]) {
                    System.err.println("Error unregistering option: " + e.getMessage)
                  } else {
                    System.err.println("Invalid UNREGISTER command.")
                    System.err.println("Use UNREGISTER optcode")
                    System.err.println("(optcode is an integer)")
                  }
                }
              } else if (line.startsWith("SPY")) {
                tc.registerSpyStream(fout)
              } else if (line.startsWith("UNSPY")) {
                tc.stopSpyStream()
              } else if (line.matches("^\\^[A-Z^]\\r?\\n?$")) {
                val toSend = buff(1)

                if (toSend == '^') {
                  outstr.write(toSend)
                } else {
                  outstr.write(toSend - 'A' + 1)
                }
                outstr.flush()
              } else {
                try {
                  outstr.write(buff, 0 , ret_read)
                  outstr.flush();
                } catch {
                  case e: IOException => end_loop = true
                }
              }
            }
          } catch {
            case e: IOException =>
              System.err.println("Exception while reading keyboard:" + e.getMessage)
              end_loop = true
          }
        } while ((ret_read > 0) && (end_loop == false))

        try {
          tc.disconnect()
        } catch {
          case e: IOException => System.err.println("Exception while connecting:" + e.getMessage)
        }
      } catch {
        case e: IOException =>
          System.err.println("Exception while connecting:" + e.getMessage)
          System.exit(1)
      }
    }
  }


  /***
    * Callback method called when TelnetClient receives an option
    * negotiation command.
    *
    * @param negotiation_code - type of negotiation command received
    * (RECEIVED_DO, RECEIVED_DONT, RECEIVED_WILL, RECEIVED_WONT, RECEIVED_COMMAND)
    * @param option_code - code of the option negotiated
    ***/
  override def receivedNegotiation(negotiation_code: Int, option_code: Int): Unit = {
    val command = negotiation_code match {
      case TelnetNotificationHandler.RECEIVED_DO => "DO"
      case TelnetNotificationHandler.RECEIVED_DONT => "DONT"
      case TelnetNotificationHandler.RECEIVED_WILL => "WILL"
      case TelnetNotificationHandler.RECEIVED_WONT => "WONT"
      case TelnetNotificationHandler.RECEIVED_COMMAND => "COMMAND"

      case _ => Integer.toString(negotiation_code) // Should not happen
    }
    System.out.println("Received " + command + " for option code " + option_code)
  }

  /***
    * Reader thread.
    * Reads lines from the TelnetClient and echoes them
    * on the screen.
    ***/
  override def run(): Unit = {
    val instr = tc.getInputStream

    try {
      val buff = Array.ofDim[Byte](1024)
      var ret_read = 0

      do {
        ret_read = instr.read(buff)

        if(ret_read > 0) {
          System.out.print(new String(buff, 0, ret_read))
        }
      } while (ret_read >= 0)
    } catch {
      case e: IOException => System.err.println("Exception while reading socket:" + e.getMessage)
    }

    try {
      tc.disconnect()
    } catch {
      case e: IOException => System.err.println("Exception while closing telnet:" + e.getMessage)
    }
  }
}