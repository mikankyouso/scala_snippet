package conc

import scala.language.postfixOps

object Conc {
  import concurrent._
  import concurrent.duration._
  import ExecutionContext.Implicits._

  def main(args: Array[String]) {
    val f = future(println(1))
    Await.result(f, 1 second)
  }
}