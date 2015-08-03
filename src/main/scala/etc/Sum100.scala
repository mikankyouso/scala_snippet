package etc

import javax.script.ScriptEngineManager

/**
 * 1,2,…,9の数をこの順序で、”+”、”-“、またはななにもせず結果が100となるあらゆる組合せを出力するプログラムを記述せよ。
 * 例えば、1 + 2 + 34 – 5 + 67 – 8 + 9 = 100となる
 */
object Sum100 extends App {
  val exprs: Iterator[String] = {
    def f(n: Int): Stream[String] =
      if (n == 0) Stream("")
      else Stream("+", "-", " ").flatMap(c => f(n - 1).map(c + _))
    f(8).map(ops => ("123456789" zip ops + " ").map(t => "" + t._1 + t._2).mkString.replace(" ", "")).toIterator
  }

  val js = new ScriptEngineManager().getEngineByName("JavaScript")
  exprs.filter(js.eval(_) == 100).foreach(println)
}