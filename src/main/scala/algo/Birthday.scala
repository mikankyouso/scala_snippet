package algo
/*
m人のグループに同じ誕生日の人が二人以上いる確率Ｐはどのような式で表されるか？
ただし、誕生日は1年365年に渡ってランダムに分布すると仮定する。(まず、同じ誕生日の人が一人もいない確率を考えるとよい。)

グループ人数mの入力に対しＰを計算するプログラムを作りなさい。
Ｐが初めて1/2を超えるmの値を求めよ。(そのようなmの値を求めるプログラムを作りなさい。)
 */
object Birthday {
  def f(m: Int): Double = m match {
    case 1 => 1.0
    case n => (365.0 - n + 1.0) / 365.0 * f(n - 1)
  }
  def p(m: Int): Double = 1.0 - f(m)

  def main(args: Array[String]) {
    println((1 to 365).map(m => (m, p(m))).find(_._2 >= 0.5))
  }
}