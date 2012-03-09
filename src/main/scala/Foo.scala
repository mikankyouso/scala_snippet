
object Foo {
  import scalaz._
  import Scalaz._

  def main(args: Array[String]): Unit = {

    val x = List(1, 2) |+| List(3)
    println(x)

    val s = implicitly[Semigroup[List[Int]]]
    println(s)
  }
}
