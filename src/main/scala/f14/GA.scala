package f14

import scala.util.Random

object GA {
  val time = 120000
  val pc = PC(Sum)

  val population = 100
  val genLimit = 10000

  def main(args: Array[String]): Unit = {
    val s = System.currentTimeMillis()
    val jobActions = pc.job.actions.toArray;

    val init = new Initialization {
      def apply(): Seq[Seq[Action]] =
        (1 to population).map { _ =>
          (1 to time / 2500 * 3).map { _ => jobActions(Random.nextInt(jobActions.size)) }
        }
    }

    val eval = new Evaluation {
      def apply(actions: Seq[Action]): Int =
        Sim(pc, time, FixedActionSelector(actions)).start.damage
    }

    val select = new Selection {
      def apply(data: Seq[(Int, Seq[Action])]): Seq[Seq[Action]] = {
        //data.sortBy(_._1).take(population).map(_._2)
        val sorted = data.sortBy(_._1).reverse
        val r = Random.shuffle(sorted.drop(1)).take(population - 1)
        println(sorted.head)
        (sorted.head +: r).map(_._2)
      }
    }

    val cross = new Crossover {
      def cross(as1: Seq[Action], as2: Seq[Action]): Seq[Action] = {
        if (Random.nextBoolean())
          as1.zip(as2).map { case (a1, a2) => if (Random.nextBoolean()) a1 else a2 }
        else {
          val n1 = Random.nextInt(as1.size)
          val n2 = n1 + Random.nextInt(as1.size - n1) + 1
          as1.take(n1) ++ as2.take(n2).drop(n1) ++ as1.drop(n2)
        }
      }
      def apply(actss: Seq[Seq[Action]]): Seq[Seq[Action]] = {
        actss ++ Random.shuffle(actss).grouped(2).map(p => if (p.size > 1) cross(p(0), p(1)) else p(0))
      }
    }

    val mutate = new Mutation {
      def apply(actss: Seq[Seq[Action]]): Seq[Seq[Action]] = {
        actss.map { acts =>
          if (Random.nextDouble() < 0.05)
            acts.updated(Random.nextInt(acts.size), jobActions(Random.nextInt(jobActions.size)))
          else
            acts
        }
      }
    }

    val term = new Termination {
      def apply(genaration: Int, data: Seq[(Int, Seq[Action])]): Boolean = {
        genaration >= genLimit
      }
    }

    val top = GA(init, eval, select, cross, mutate, term).apply
    val result = Sim(pc, time, FixedActionSelector(top)).start
    println(result.damage)
    println(result.actionHistory.reverse)

    //val as = FixedActionSelector(Flow, Mosa, Bio, Miasma, Burst, Ruin2, Ruin2, Ruin2, Ruin2, Ruin2, Ruin2, Ruin2)
    //    val ret = Sim(PC(Sum), time, as).start
    //
    //    println(ret.actionHistory.reverse)
    //    println(ret.damage / 1000)
    println(System.currentTimeMillis() - s + "ms")
  }
}

case class GA(initialization: Initialization, evaluation: Evaluation, selection: Selection,
    crossover: Crossover, mutation: Mutation, termination: Termination) {
  def apply: Seq[Action] = {
    val nextGen: Seq[(Int, Seq[Action])] => Seq[Seq[Action]] = data =>
      mutation(crossover(selection(data)))
    val eval: Seq[Seq[Action]] => Seq[(Int, Seq[Action])] = actss =>
      actss.par.map(acts => (evaluation(acts), acts)).seq

    val data = Stream.iterate(eval(initialization()))(v => eval(nextGen(v)))
      .zipWithIndex
      .takeWhile { case (data, gen) => !termination(gen, data) }
      .last
      ._1
    data.maxBy(_._1)._2
  }
}

trait Initialization {
  def apply(): Seq[Seq[Action]]
}

trait Evaluation {
  def apply(actions: Seq[Action]): Int
}

trait Selection {
  def apply(data: Seq[(Int, Seq[Action])]): Seq[Seq[Action]]
}

trait Crossover {
  def apply(actss: Seq[Seq[Action]]): Seq[Seq[Action]]
}

trait Mutation {
  def apply(actss: Seq[Seq[Action]]): Seq[Seq[Action]]
}

trait Termination {
  def apply(genaration: Int, data: Seq[(Int, Seq[Action])]): Boolean
}