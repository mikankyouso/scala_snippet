
//import scala.util.continuations._

object Cont {
/*
  def loopBreak() {
    var i = 0
    reset {
      while (i < 10) {
        shift { k: (Unit => Unit) =>
          if (i < 5) k()
        }
        i += 1
        println(i)
      }
    }
  }

  def loop(i: Int, f: (Int => Unit)) {
    reset {
      val v = shift { k: (Int => Unit) =>
        (0 until i).foreach(k)
      }
      f(v)
    }
  }

  def gen() {

    def next(): String = ""
  }

  def main(args: Array[String]) {
    //    loopBreak()
    //    loop(5, println(_))
    gen()
  }

  def amb(lst: Int*): Int @cpsParam[Unit, Unit] = shift { k: (Int => Unit) => lst.foreach(k) }
  def require(p: Boolean): Unit @cpsParam[Unit, Unit] = shift { k: (Unit => Unit) => if (p) k() }

  def distinct(lst: Int*) = {
    def proc(l: List[Int]): Boolean = l match {
      case Nil => true
      case x :: xs => (xs.indexOf(x) < 0) && proc(xs)
    }
    proc(lst.toList)
  }

  def main2(args: Array[String]) {
    reset {
      val a, b, c, d = amb(1, 2, 3, 4)

      // 同順が無いことを仮定
      require(distinct(a, b, c, d))

      // A「Dがやられたようだな…」B「ククク…奴は我ら四天王の中でも最弱…」
      require(d == 4)

      // C「私はBよりも弱い…」
      require(b < c)

      // A「そして私は最強ではない…」
      require(a != 1)

      // B「四天王の中に私よりも弱いものが最低でも二人いる…」
      require(b == 1 || b == 2)

      // C「私はAよりも強い…」
      require(c < a)

      // ※以上の条件から四天王を強い順に並べよ（5点）
      println(("abcd" zip List(a, b, c, d)).toList.sortWith(_._2 < _._2))
    }
  }
*/
}
