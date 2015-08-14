package f14

import scala.annotation.tailrec
import scala.util.Random

case class Sim(pc: PC, timeLimit: Int, actionSelector: ActionSelector) {
  def start: Context = {
    val context = Context(pc, actionSelector)
    context.enqueue(0, Start).enqueue(0, Active).enqueue(timeLimit, End).forward
  }
}

object Sim {
  def main(args: Array[String]): Unit = {
    val s = System.currentTimeMillis()
    //val as = NopActionSelector
    //val as = RandomActionSelector
    //val as = FixedActionSelector(Bio, Ruin2, Mosa, Miasma)
    val as = MaxDamageFinder
    val ret = Sim(PC(Sum), 30000, as).start
    println(ret.damage / 1000, ret.actionHistory.reverse)
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

case class FixedActionSelector(actions: Action*) extends ActionSelector {
  def select(context: Context, usableActions: Set[Action]): (Option[Action], ActionSelector) = {
    actions match {
      case Seq(a, as @ _*) if usableActions.contains(a) => (Some(a), new FixedActionSelector(as: _*))
      case _ => (None, this)
    }
  }
}

object MaxDamageFinder extends ActionSelector {
  def select(context: Context, usableActions: Set[Action]): (Option[Action], ActionSelector) = {
    val (_, action) = usableActions
      .par
      .map(a => (a.use(context).forward.damage, a))
      .maxBy(_._1)
    (Some(action), this)
  }
}

