/*
ある金額になるコインの組み合わせ数とその組み合わせを全て答え下さい。

条件）
・コインの種類は自由に設定できるようにする。
・順序が違うだけのものは一つの組み合わせとする。
　（例：16の組み合わせで、[1, 5, 10]と[10, 5, 1]は同じ）

例）
コインの種類：1, 5, 10, 50, 100, 500
金額：10
組み合わせ数：4
組み合わせ：
[1, 1, 1, 1, 1, 1, 1, 1, 1, 1]
[1, 1, 1, 1, 1, 5]
[5, 5]
[10]
 */
object Coin {
  def main(args: Array[String]): Unit = {
    val ans = solve(10, List(1, 5, 10, 50, 100, 500))
    println(ans.size)
    ans.foreach(println)
  }

  def solve(total: Int, coins: List[Int]): List[List[Int]] = {
    def f(coins: List[Int], curr: List[Int], rest: Int): List[List[Int]] =
      coins.dropWhile(rest < _).flatMap(c =>
        if (c == rest) List(c :: curr) else f(coins.dropWhile(c < _), c :: curr, rest - c))

    f(coins.distinct.sorted.reverse, Nil, total)
  }
}
