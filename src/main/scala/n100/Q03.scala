package n100

/**
 * http://www.cl.ecei.tohoku.ac.jp/nlp100/
 *
 * 03. 円周率
 * "Now I need a drink, alcoholic of course, after the heavy lectures involving quantum mechanics."という文を単語に分解し，
 * 各単語の（アルファベットの）文字数を先頭から出現順に並べたリストを作成せよ．
 */
object Q03 extends App {
  val ans = "Now I need a drink, alcoholic of course, after the heavy lectures involving quantum mechanics."
    .split("[ ,.]+")
    .map(_.length)
    .toList
  println(ans)
}