import java.text.Collator
import java.util.Locale
import scala.math.Ordering

object CollatorMain {
  def main(args: Array[String]) {
    val colJp = Collator.getInstance(Locale.JAPANESE)
    val colUs = Collator.getInstance(Locale.ENGLISH)

    val list = List("日", "あ", "一", "花", "火", "木", "気", "ゟ", "ゔ", "う", "え", "ウ", "エ", "ヴ").sorted
    val listJp = list.sortWith(colJp.compare(_, _) < 0)
    val listUs = list.sortWith(colUs.compare(_, _) < 0)

    println(list)
    println(listJp)
    println(listUs)

    def toHex(ss: List[String]) = ss.map(s => BigInt(1, s.getBytes("UTF-8")).toString(16))
    println(toHex(list))
    println(toHex(listJp))
    println(toHex(listUs))
  }
}