package sudoku

//参考 http://www.aoky.net/articles/peter_norvig/sudoku.htm
class Sudoku(numbers: IndexedSeq[Int]) {
  override def toString() = numbers.map {
    case 0 => "."
    case n => n.toString
  }.mkString.grouped(9).map(_.grouped(3).mkString("|")).mkString("\n").grouped(36).mkString("---+---+---\n")

  def solve = {
    import Sudoku._

    type NumSet = collection.GenSet[Int]
    type Values = IndexedSeq[NumSet]
    val oneToNine: NumSet = Set(1 to 9: _*)
    val values: Values = IndexedSeq.fill(81)(oneToNine)

    def assign(values: Values, index: Int, num: Int): Option[Values] =
      (Option(values) /: (oneToNine - num))((vs, n) => vs.flatMap(eliminate(_, index, n)))

    def eliminate(values: Values, index: Int, digit: Int): Option[Values] =
      if (!values(index).contains(digit)) Some(values)
      else {
        val nNums = values(index) - digit
        nNums.size match {
          case 0 => None
          case 1 => {
            val a = nNums.head
            (Option(values.updated(index, nNums)) /: peers(index))((vs, i) => vs.flatMap(eliminate(_, i, a)))
          }
          case _ => Some(values.updated(index, nNums))
        }
      }

    def search(values: Values): Stream[Values] = {
      val indexes = values.map(_.size).zipWithIndex.filter(1 < _._1).sortBy(_._1).map(_._2).toList
      (Stream(values) /: indexes)((s, i) => s.flatMap(vs => vs(i).flatMap(d => assign(vs, i, d))))
    }

    val nums: IndexedSeq[Int] = numbers.toIndexedSeq
    val start: Option[Values] = (Option(values) /: (0 to 80))((vs, i) => if (nums(i) == 0) vs else vs.flatMap(assign(_, i, nums(i))))
    start.toStream.flatMap(search(_)).map(_.map(_.head)).map(new Sudoku(_))

  }
}

object Sudoku {
  val peers = {
    def toIndex(t: (Int, Int)): Int = t._1 * 9 + t._2
    def cross(rows: List[Int], cols: List[Int]): List[(Int, Int)] =
      for (r <- rows; c <- cols) yield (r, c)
    val rows = List.range(0, 9)
    val cols = rows
    val squares = cross(rows, cols)
    val unitList = cols.map(c => cross(rows, List(c))) ::: rows.map(r => cross(List(r), cols)) :::
      (for (rs <- rows.grouped(3); cs <- cols.grouped(3)) yield cross(rs, cs)).toList
    val units = squares.map(t => (t, unitList.filter(u => u.contains(t)))).toMap
    val ps = squares.map(t => (t, units(t).flatMap(u => u.filter(t !=)).distinct)).toMap
    ps.map {
      case (k, v) => (toIndex(k), v.map(toIndex))
    }
  }

  def apply(numbers: String) = new Sudoku(numbers.collect {
    case '.' => 0
    case c: Char if ('0' to '9').contains(c) => c - '0'
  })
}

object SudokuMain {
  def p(toi: String) {
    val s = Sudoku(toi)

    println(s)
    println
    val bench = new testing.Benchmark {
      //multiplier = 4
      def run = print(s.solve.size + " ")
    }
    val r = bench.runBenchmark(100)
    println
    println(r.mkString(" "))
    println("平均: " + (r.sum / r.size))
    println
  }

  def main(args: Array[String]): Unit = {
    p("003020600900305001001806400008102900700000008006708200002609500800203009005010300")

    p("""
4 . . |. . . |8 . 5 
. 3 . |. . . |. . . 
. . . |7 . . |. . . 
------+------+------
. 2 . |. . . |. 6 . 
. . . |. 8 . |4 . . 
. . . |. 1 . |. . . 
------+------+------
. . . |6 . 3 |. 7 . 
5 . . |2 . . |. . . 
1 . 4 |. . . |. . . 
""")
  }

  p("900000000000000000000000000000080000000000000009000900000000000010000000000000089")

  /*
  p("""
. . . |. . 5 |. 8 . 
. . . |6 . 1 |. 4 3 
. . . |. . . |. . . 
------+------+------
. 1 . |5 . . |. . . 
. . . |1 . 6 |. . . 
3 . . |. . . |. . 5 
------+------+------
5 3 . |. . . |. 6 1 
. . . |. . . |. . 4 
. . . |. . . |. . . """)
*/
  /*  
  p("""
. . . |. . 6 |. . . 
. 5 9 |. . . |. . 8 
2 . . |. . 8 |. . . 
------+------+------
. 4 5 |. . . |. . . 
. . 3 |. . . |. . . 
. . 6 |. . 3 |. 5 4 
------+------+------
. . . |3 2 5 |. . 6 
. . . |. . . |. . . 
. . . |. . . |. . . 
""")
*/

  //List(193, 45, 26, 22, 22, 23, 25, 24, 24, 22, 18, 23, 15, 17, 15, 15, 14, 23, 21, 15)
  //mybitset List(338, 152, 73, 71, 84, 71, 68, 70, 73, 70, 65, 69, 63, 62, 65, 61, 62, 78, 67, 62)
  // 100かい
  //set 12
  //bitset 14
  p("""
8 5 . |. . 2 |4 . . 
7 2 . |. . . |. . 9 
. . 4 |. . . |. . . 
------+------+------
. . . |1 . 7 |. . 2 
3 . 5 |. . . |9 . . 
. 4 . |. . . |. . . 
------+------+------
. . . |. 8 . |. 7 . 
. 1 7 |. . . |. . . 
. . . |. 3 6 |. 4 . 
""")

  p("""
. . 5 |3 . . |. . . 
8 . . |. . . |. 2 . 
. 7 . |. 1 . |5 . . 
------+------+------
4 . . |. . 5 |3 . . 
. 1 . |. 7 . |. . 6 
. . 3 |2 . . |. 8 . 
------+------+------
. 6 . |5 . . |. . 9 
. . 4 |. . . |. 3 . 
. . . |. . 9 |7 . . 
""")
}
