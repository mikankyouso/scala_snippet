import util.MyImplicits._

import scala.collection.GenTraversableOnce

object Foo {

  def main(args: Array[String]): Unit = {
    val x = List(1, 22, 333) |> count
    println(x)
  }

  def count(t: GenTraversableOnce[_]): Int = t.size
}