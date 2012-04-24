package json

import java.io.Writer
import net.liftweb.json.DefaultFormats
import net.liftweb.json.Serialization

class Json {
  implicit val formats = DefaultFormats

  def write[A <: AnyRef](ref: A): String = Serialization.write(ref)
  def writePretty[A <: AnyRef](ref: A): String = Serialization.writePretty(ref)

  def write[A <: AnyRef, W <: Writer](ref: A, w: W): W = Serialization.write(ref, w)
  def writePretty[A <: AnyRef, W <: Writer](ref: A, w: W): W = Serialization.writePretty(ref, w)

}

object Json extends Json {

}

object CaseClassBind {
  def main(args: Array[String]) {
    val json = Json.writePretty(B("aa", 123))
    println(json)
  }
}

trait JsonSupport {
  type T

  def toJson: String = ""

  def fromJson(json: String): T = throw new Exception
}

trait X extends JsonSupport {
  def name: String
  val ppp = "qqq"
}

//case class A(name: String) extends X {
//  type T = A
//}
case class B(name: String, age: Int) extends X
