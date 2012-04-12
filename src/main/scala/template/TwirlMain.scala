package template

object TwirlMain {
  def main(args: Array[String]) {
    val s = txt.foo.render("xxx")
    println(s)
  }
}