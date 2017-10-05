import util.MyImplicits._

import scala.collection.GenTraversableOnce

class Foo {

  def main(args: Array[String]): Unit = {
    val x = List(1, 22, 333) |> count
    println(x)
  }

  def count[A <: GenTraversableOnce[_]](t: A): Int = t.size
}