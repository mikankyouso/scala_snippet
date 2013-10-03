class Foo {
  def abc(s: String) = {
    implicit val i = 345
    あああ(s)
  }
  def あああ(str: String)(implicit iii: Int) = str + "hoge" + iii
}