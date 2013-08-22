package algo
import scala.collection.Iterator

/**
 * http://www.cgios.com/far1/recruit/prod/sp/wif
 * http://nagoyascala.yoshihiro503.cloudbees.net/bbs/thread/114001
 * 4種類のアルファベット "A,C,G,T" から成るn文字の文字列のうち、
 * "AAG"という並びが含まれる文字列を全て列挙するプログラムを書きなさい。
 * ただし、nは3以上の整数とし、文字列内に同じアルファベットが出現しても構わないものとし、
 * 出力順序は問わないものとします。
 */
object ACGT {
  def gen(n: Int): Iterator[String] =
    if (n <= 1) "ACGT".toIterator.map(_.toString)
    else for (s <- gen(n - 1); c <- "ACGT") yield s + c

  def solve(n: Int): Iterator[String] = gen(n).filter(_.contains("AAG"))

  def main(args: Array[String]) {
    solve(if (args.isEmpty) 4 else args.head.toInt).foreach(println)
  }
}