import scala.collection.Iterable

object Fib {
  def main(args: Array[String]) {
    val fib = fib1 _
    (1 to 40).view.map(fib).foreach(println)
  }

  def fib1(n: Int): BigInt = if (n < 2) n else fib1(n - 2) + fib1(n - 1)

}