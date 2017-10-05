package puzzle

// https://s3-us-west-1.amazonaws.com/gregable/puzzle.html
object RegPuzzle {
  private val regStrs = """
    .*H.*H.*
    (DI|NS|TH|OM)*
    F.*[AO].*[AO].*
    (O|RHH|MM)*
    .*
    C*MC(CCC|MM)*
    [^C]*[^R]*III.*
    (...?)\1*
    ([^X]|XCC)*
    (RR|HHH)*.?
    N.*X.X.X.*E
    R*D*M*
    .(C|HH)*
    .*G.*V.*H.*
    [CR]*
    .*XEXM*
    .*DD.*CCM.*
    .*XHCR.*X.*
    .*(.)(.)(.)(.)\4\3\2\1.*
    .*(IN|SE|HI)
    [^C]*MMM[^C]*
    .*(.)C\1X\1.*
    [CEIMU]*OH[AEMOR]*
    (RX|[^R])*
    [^M]*M[^M]*
    (S|MM|HHH)*
    .*SE.*UE.*
    .*LR.*RL.*
    .*OXR.*
    ([^EMC]|EM)*
    (HHX|[^HX])*
    .*PRR.*DDC.*
    .*
    [AM]*CM(RC)*R?
    ([^MC]|MM|CC)*
    (E|CR|MN)*
    P+(..)\1.*
    [CHMNOR]*I[CHMNOR]*
    (ND|ET|IN)[^X]*
  """.trim.split('\n').map(_.trim).toIndexedSeq

  private val regs = regStrs.map(_.r.pattern)

  private val chars = regStrs.mkString.distinct.filter(_.isLetter).sorted

  private val sizeOfLine: IndexedSeq[Int] = ((7 to 13) ++ (12 to 7 by -1))

  private val posTable: IndexedSeq[IndexedSeq[Int]] =
    sizeOfLine
      .scan(0) { (acc, v) => acc + v }
      .sliding(2)
      .map { ft => (ft(0) until ft(1)) }
      .toIndexedSeq

  sealed trait Direction
  object WtoE extends Direction
  object NEtoSW extends Direction
  object SEtoNW extends Direction

  case class Hex(data: IndexedSeq[Char]) {
    private[this] lazy val WtoELines = sizeOfLine.zipWithIndex.map {
      case (len, i) => (0 until len).map(value(_, i)).mkString
    }
    private[this] lazy val NEtoSWLines = sizeOfLine.zipWithIndex.map {
      case (len, i) =>
        (0 until len).map { n =>
          val y = (0 max (6 - i)) + n
          val x = 12 - i - (0 max (y - 6))
          value(x, y)
        }.mkString
    }
    private[this] lazy val SEtoNWLines = sizeOfLine.zipWithIndex.map {
      case (len, i) =>
        (0 until len).map { n =>
          val y = 12 - (0 max (i - 6)) - n
          val x = i - (0 max (6 - y))
          value(x, y)
        }.mkString
    }

    def value(x: Int, y: Int): Char = data(posTable(y)(x))

    def updated(index: Int, c: Char): Hex = Hex(data.updated(index, c))

    def lines(direction: Direction): IndexedSeq[CharSequence] = direction match {
      case WtoE   => WtoELines
      case NEtoSW => NEtoSWLines
      case SEtoNW => SEtoNWLines
    }

    lazy val validate: Boolean = {
      regs.grouped(13).zip(Iterator(WtoE, SEtoNW, NEtoSW)).forall {
        case (rs, dir) => lines(dir).zip(rs).forall {
          case (s, reg) => reg.matcher(s).matches()
        }
      }
    }

    lazy val matches: Int = {
      regs.grouped(13).zip(Iterator(WtoE, SEtoNW, NEtoSW)).map {
        case (rs, dir) => lines(dir).zip(rs).count {
          case (s, reg) => reg.matcher(s).matches()
        }
      }.sum
    }

    override def toString(): String = {
      lines(WtoE).map(s => " " * (13 - s.length) + s.toString().mkString(" ")).mkString("\n")
    }

  }

  def main(args: Array[String]): Unit = {
    //    println(chars) //19
    //127
    val hex = Hex("1234567890123456789012345678901234567890123456789012345678901234567890123456789001234567890012345678900123456789001234567890123")
    //    println(hex)
    //    hex.lines(WtoE).foreach(println)
    //hex.lines(NEtoSW).foreach(println)
    //    hex.lines(SEtoNW).foreach(println)

    import scala.util.Random
    val num = 100
    val result = GA[Hex, Int, Hex](
      new Initialization[Hex] {
        def apply(): Seq[Hex] = (1 to num).map { _ =>
          Hex((1 to 127).map(_ => chars(Random.nextInt(chars.size))).mkString)
        }
      },
      new Evaluation[Hex, Int] {
        def apply(hex: Hex): Int = hex.matches
      },
      new Selection[Hex, Int] {
        def apply(scoreAndData: Seq[(Int, Hex)]): Seq[Hex] = {
          val sorted = scoreAndData.sortBy(_._1).reverse
          (sorted.take(num / 4) ++ Random.shuffle(sorted.drop(num / 4)).take(num - num / 4)).map(_._2)
        }
      },
      new Crossover[Hex] {
        def apply(data: Seq[Hex]): Seq[Hex] = {
          data.head +: data.flatMap { hex =>
            val hex2 = data(Random.nextInt(data.size))
            val ht = randomSwap(randomSelect((hex, hex2)))
            Seq(ht._1, ht._2)
          }
        }
        def randomSelect(hexs: (Hex, Hex)): (Hex, Hex) = {
          val ht = hexs._1.data.zip(hexs._2.data).map {
            case cs => if (Random.nextBoolean()) cs else cs.swap
          }.unzip
          (Hex(ht._1), Hex(ht._2))
        }
        def randomSwap(hexs: (Hex, Hex)): (Hex, Hex) = {
          val i = Random.nextInt(hexs._1.data.size)
          val i2 = Random.nextInt(hexs._2.data.size)
          (hexs._1.updated(i, hexs._2.data(i2)), hexs._2.updated(i2, hexs._1.data(i)))
        }

      },
      new Mutation[Hex] {
        def apply(data: Seq[Hex]): Seq[Hex] = {
          data.head +: data.map { hex =>
            if (Random.nextDouble() < 0.05)
              hex.updated(Random.nextInt(hex.data.size), chars(Random.nextInt(chars.size)))
            else
              hex
          }
        }
      },
      new Termination[Hex, Int, Hex] {
        def apply(genaration: Long, scoreAndData: Seq[(Int, Hex)]): Option[Hex] = {
          val max = scoreAndData.maxBy(_._1)
          if (genaration % 100 == 0) {
            println(genaration, max._1)
            println(max._2)
          }
          if (max._1 == regs.size) Some(max._2) else None
        }
      }).apply
    println(result)
    println(result.matches)
  }

  case class GA[A, S, R](initialization: Initialization[A], evaluation: Evaluation[A, S], selection: Selection[A, S],
      crossover: Crossover[A], mutation: Mutation[A], termination: Termination[A, S, R]) {

    def nextGen(scoreAndData: Seq[(S, A)]): Seq[A] = mutation(crossover(selection(scoreAndData)))
    def calcScore(data: Seq[A]): Seq[(S, A)] = data.par.map(acts => (evaluation(acts), acts)).seq

    def apply: R = {
      var scoreAndData = calcScore(initialization())
      var gen = 0L
      var result = termination(gen, scoreAndData)
      while (result.isEmpty) {
        gen += 1
        scoreAndData = calcScore(nextGen(scoreAndData))
        result = termination(gen, scoreAndData)
      }
      result.get
    }
  }

  trait Initialization[A] {
    def apply(): Seq[A]
  }

  trait Evaluation[A, S] {
    def apply(data: A): S
  }

  trait Selection[A, S] {
    def apply(scoreAndData: Seq[(S, A)]): Seq[A]
  }

  trait Crossover[A] {
    def apply(data: Seq[A]): Seq[A]
  }

  trait Mutation[A] {
    def apply(data: Seq[A]): Seq[A]
  }

  trait Termination[A, S, R] {
    def apply(genaration: Long, scoreAndData: Seq[(S, A)]): Option[R]
  }
}

