
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.JAXB
import java.io.StringWriter
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAccessType
import java.io.StringReader

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class Persons(persons: Array[Person]) {
  private def this() = this(Array.empty)
}

@XmlAccessorType(XmlAccessType.FIELD)
case class Person(name: String, age: Int) {
  private def this() = this("", 0)
}

object JaxbMain {
  def main(args: Array[String]) {
    val ps = Persons(Array(Person("あいう", 123)))
    val w = new StringWriter
    JAXB.marshal(ps, w)
    println(w)

    val u = JAXB.unmarshal(new StringReader(w.toString), classOf[Persons])
    println(u)
  }
}