package algo

object Mine {

  val q = """
................
................
....***.....***.
...**.*....**.*.
..**......**....
...*****...*****
...*.*.*...*.*.*
...*.***...*.***
""".trim

  def main(args: Array[String]) {
    val s = q.lines.toIndexedSeq
    val w = s(0).size
    val h = s.size

    def f(x: Int, y: Int) =
      (for {
        i <- (x - 1 to x + 1).dropWhile(_ < 0).takeWhile(_ < w)
        j <- (y - 1 to y + 1).dropWhile(_ < 0).takeWhile(_ < h)
        if !(i == x && j == y)
      } yield if (s(j)(i) == '*') 1 else 0).sum.toString.apply(0)

    val a =
      for ((line, y) <- s.zipWithIndex) yield {
        for ((c, x) <- line.zipWithIndex) yield {
          if (c == '*') '*' else f(x, y)
        }
      }

    a.foreach(l => println(l.mkString))
  }
}