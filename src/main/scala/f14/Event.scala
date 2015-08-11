package f14

import scala.util.Random

trait Event {
  def apply(context: Context): Context
}

object Start extends Event {
  def apply(context: Context): Context = {
    context.enqueue(context.elapsedTime, Tick)
  }
}

object End extends Event {
  def apply(context: Context): Context = {
    context
  }
}

object Tick extends Event {
  def apply(context: Context): Context = {
    for {
      c2 <- context.enchants.foldLeft(context)((c, enchant) => enchant.tick(c))
      c3 <- c2.enqueue(c2.elapsedTime + 3000, Tick)
    } yield c3
  }
}

object Active extends Event {
  def apply(context: Context): Context = {
    //TODO
    //    context.pc.job.actions.find(a => a.usable(context)) match {
    //      case Some(action) => {
    //        println("%8.2f 【%s】".format(context.elapsedTime / 1000.0, action))
    //        action.use(context)
    //      }
    //      case None => context.enqueue(context.elapsedTime + 10, Active)
    //    }
    println(context)
    val usableActions = context.pc.job.actions.filter(_.usable(context))
    context.ifMap(!usableActions.isEmpty) { context =>
      val action = usableActions.toArray.apply(Random.nextInt(usableActions.size))
      println("%8.2f 【%s】".format(context.elapsedTime / 1000.0, action))
      action.use(context)
    }
  }
}

object GCDStart extends Event {
  def apply(context: Context): Context = {
    context.copy(globalCoolDown = true)
  }
}

object GCDEnd extends Event {
  def apply(context: Context): Context = {
    context.copy(globalCoolDown = false)
      .enqueue(context.elapsedTime, Active)
  }
}

case class RecastStart(action: Action) extends Event {
  def apply(context: Context): Context = {
    context.copy(coolDown = context.coolDown + action)
  }
}

case class RecastEnd(action: Action) extends Event {
  def apply(context: Context): Context = {
    context.copy(coolDown = context.coolDown - action)
      .enqueue(context.elapsedTime, Active)
  }
}

case class AddEnchant(enchant: Enchant) extends Event {
  def apply(context: Context): Context = {
    context.copy(enchants = context.enchants + enchant)
  }
}

case class DeleteEnchant(enchant: Enchant) extends Event {
  def apply(context: Context): Context = {
    context.copy(enchants = context.enchants - enchant)
  }
}

case class Damage(potency: Int, damegeType: DamegeType, action: Action) extends Event {
  def apply(context: Context): Context = {
    val (percent, absolute) =
      context.enchants
        .map(_.correct(this))
        .foldLeft((0, 0)) { case ((p1, a1), (p2, a2)) => (p1 + p2, a1 + a2) }
    val calcDamege = (potency * (1.0 + percent / 100d)).toInt + absolute

    context.copy(
      damege = context.damege + calcDamege,
      enchants = context.enchants.filterNot(_.deleteBy(this)))
  }
}
