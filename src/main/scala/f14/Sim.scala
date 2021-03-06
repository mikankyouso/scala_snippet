package f14

import scala.annotation.tailrec
import scala.util.Random

case class Sim(pc: PC, timeLimit: Int, actionSelector: ActionSelector) {
  def start: Context = {
    val context = Context(pc, actionSelector)

    //val context = MutableContext(pc, actionSelector)
    context
      .addEnchant(Flow.FlowEnchant())
      .enqueue(0, Start)
      .enqueue(0, Active)
      .enqueue(timeLimit, End)
      .forward
  }
}

object Sim {
  def main(args: Array[String]): Unit = {
    val s = System.currentTimeMillis()
    //val as = NopActionSelector
    //    val as = RandomActionSelector
    //    val as = FixedActionSelector(Flow, Mosa, Bio, Miasma, Burst, Ruin2, Ruin2, Ruin2, Ruin2, Ruin2, Ruin2, Ruin2)
    //    val as = FixedActionSelector(Seq(Bio2, Miasma, Bio, Ruin, Ruin, Ruin, Ruin, Ruin, Ruin, Ruin, Ruin, Ruin, Ruin))
    val as = FixedActionSelector(Seq(Bio2, Miasma, Bio, Ruin, Ruin, Ruin, Ruin, Ruin, Ruin, Ruin, Bio2, Miasma, Bio))
    //    val as = MaxDamageFinder
    //    val ret = (1 to 100000).view.par.map { _ =>
    //      Sim(PC(Sum), 60000, as).start
    //    }.maxBy(_.damage)
    val ret = Sim(PC(Sum), 60000, as).start
    println(ret.damage / 1000, ret.actionHistory.reverse)
    //println(ret)
    println(System.currentTimeMillis() - s + "ms")
  }
}

object NopActionSelector extends ActionSelector {
  def select(context: Context, usableActions: Set[Action]): (Option[Action], ActionSelector) = {
    (None, this)
  }
}

object RandomActionSelector extends ActionSelector {
  def select(context: Context, usableActions: Set[Action]): (Option[Action], ActionSelector) = {
    (Some(usableActions.toArray.apply(Random.nextInt(usableActions.size))), this)
  }
}

case class FixedActionSelector(actions: Seq[Action]) extends ActionSelector {
  def select(context: Context, usableActions: Set[Action]): (Option[Action], ActionSelector) = {
    actions match {
      case Seq(a, as @ _*) if usableActions.contains(a) => (Some(a), FixedActionSelector(as))
      case _ => (None, this)
    }
  }
}

object MaxDamageFinder extends ActionSelector {
  def select(context: Context, usableActions: Set[Action]): (Option[Action], ActionSelector) = {
    val set = usableActions
      .par
      .map { act =>
        (Option(act), act.use(context).forward)
      }
    //      .+((None, context.forward))
    //.filter { case (_, c) => c.elapsedTime < 10000 || c.damage / c.elapsedTime > 69 }
    if (set.isEmpty) {
      (None, NopActionSelector)
    } else {
      val (act, _) = set.maxBy(_._2.damage)
      (act, this)
    }
  }
}

