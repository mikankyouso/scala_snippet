package security

import java.security.Security

object ProviderPrinter {
  import collection.JavaConversions._
  def main(args: Array[String]) {
    for (prov <- Security.getProviders) {
      println("*** " + prov.toString + " ***")
      for (serv <- prov.getServices) {
        println(serv)
      }
      println
    }
  }
}