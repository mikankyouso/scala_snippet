object Tak extends testing.Benchmark {
  def tak(x: Int, y: Int, z: Int): Int =
    if (x <= y) z else tak(tak((x - 1), y, z), tak((y - 1), z, x), tak((z - 1), x, y))

  override def main(args: Array[String]) {
    println(runBenchmark(10))
  }

  def run() { println(tak(18, 9, 0)) }

}