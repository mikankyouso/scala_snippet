package n100

/**
 * http://www.cl.ecei.tohoku.ac.jp/nlp100/
 *
 * 01. 「パタトクカシーー」
 * 「パタトクカシーー」という文字列の1,3,5,7文字目を取り出して連結した文字列を得よ．
 */
object Q01 extends App {
  val ans = List(1, 3, 5, 7).map(n => "パタトクカシーー"(n - 1)).mkString
  println(ans)
}