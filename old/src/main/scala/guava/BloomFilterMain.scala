package guava
import java.lang.management.ManagementFactory

import scala.compat.Platform

import com.google.common.base.Stopwatch
import com.google.common.hash.BloomFilter
import com.google.common.hash.Funnels

object BloomFilterMain extends App {
  val memoryMxBean = ManagementFactory.getMemoryMXBean()

  val sw = new Stopwatch()
  def bench[A](f: => A): A = {
    sw.reset.start
    try f finally {
      println(sw)

      Platform.collectGarbage()
      Platform.collectGarbage()
      Platform.collectGarbage()
      println(memoryMxBean.getHeapMemoryUsage())
      println
    }
  }

  println("でーた生成")
  val strs = bench((1 to 10000000).map(_.toString))

  println("ブルームフィルタ生成")
  val bf = bench(BloomFilter.create(Funnels.stringFunnel(), 10000000))

  println("データ登録")
  bench(strs.foreach(bf.put(_)))

  println("検索")
  bench(println(strs.find(!bf.mightContain(_))))

}