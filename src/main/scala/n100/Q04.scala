package n100

/**
 * http://www.cl.ecei.tohoku.ac.jp/nlp100/
 *
 * 04. 元素記号
 * "Hi He Lied Because Boron Could Not Oxidize Fluorine. New Nations Might Also Sign Peace Security Clause. Arthur King Can."
 * という文を単語に分解し，1, 5, 6, 7, 8, 9, 15, 16, 19番目の単語は先頭の1文字，
 * それ以外の単語は先頭に2文字を取り出し，取り出した文字列から単語の位置（先頭から何番目の単語か）への
 * 連想配列（辞書型もしくはマップ型）を作成せよ．
 */
object Q04 extends App {
  val ans = "Hi He Lied Because Boron Could Not Oxidize Fluorine. New Nations Might Also Sign Peace Security Clause. Arthur King Can."
    .split("[ ,.]+")
    .zipWithIndex
    .map { case (word, index) =>
      val len = if(Set(1, 5, 6, 7, 8, 9, 15, 16, 19)(index + 1)) 1 else 2
      (word.substring(0, len), index + 1)
    }.toMap
  println(ans)
}