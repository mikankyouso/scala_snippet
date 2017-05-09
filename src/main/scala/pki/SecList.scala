package pki

import java.security.Security
import scala.collection.JavaConverters._
import scala.collection.SortedMap
import java.security.Provider.Service
import scala.collection.SortedSet

object SecList {
  def main(args: Array[String]) {
    //    for (p <- Security.getProviders()) {
    //      println("======== " + p + " ========")
    //      println(p.getInfo())
    //      println(p.getName())
    //      println(p.getVersion())
    //      for (s <- p.getServices().asScala) {
    //        println("-------- " + s.getAlgorithm() + " --------")
    //        println(s.getType())
    //        println(s)
    //      }
    //    }
    val ss = SortedMap[String, Array[Service]]() ++
      Security.getProviders.flatMap(_.getServices.asScala).groupBy(_.getType)
    def p(t: String): Unit = {
      println(s"======== $t ========")
      ss(t).map(_.getAlgorithm).to[SortedSet].foreach(println)
    }
    p("Cipher")
    p("Signature")
    p("KeyAgreement")
    p("SSLContext")
    println("---------------")
    ss.keys.foreach(println)
  }
}