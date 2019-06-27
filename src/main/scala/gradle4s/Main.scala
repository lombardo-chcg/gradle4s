package gradle4s

import org.apache.logging.log4j.LogManager

object Main {
  def main(args: Array[String]) {
    val log = LogManager.getLogger("gradle4s.Main")
    log.info("Hello from gradle4s! 🚀")
  }
}
