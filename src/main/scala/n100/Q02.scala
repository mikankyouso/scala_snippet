package n100

/**
 * http://www.cl.ecei.tohoku.ac.jp/nlp100/
 *
 * 02. 「パトカー」＋「タクシー」＝「パタトクカシーー」
 * 「パトカー」＋「タクシー」の文字を先頭から交互に連結して文字列「パタトクカシーー」を得よ．
 */
object Q02 extends App {
  val ans = "パトカー".zip("タクシー").flatMap { case (a, b) => "" + a + b }.mkString
  println(ans)
}