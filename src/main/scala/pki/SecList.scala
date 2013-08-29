package pki

import java.security.Security
import bean.JavaBean

object SecList {
  def main(args: Array[String]) {
    import collection.JavaConversions._
    for (p <- Security.getProviders()) {
      println("======== " + p + " ========")
      println(p.getInfo())
      println(p.getName())
      println(p.getVersion())
      for (s <- p.getServices()) {
        println("-------- " + s.getAlgorithm() + " --------")
        println(s.getProvider())
        println(s)
      }
    }
  }
}