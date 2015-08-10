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
      c2 <- context.pc.enchants.foldLeft(context)((c, enchant) => enchant.tick(c))
      c3 <- c2.enemy.enchants.foldLeft(c2)((c, enchant) => enchant.tick(c))
    } yield c3.enqueue(c3.elapsedTime + 3000, Tick)
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
    val actions = context.pc.job.actions
    val action = actions.toArray.apply(Random.nextInt(actions.size))
    println("%8.2f 【%s】".format(context.elapsedTime / 1000.0, action))
    action.use(context)
  }
}

object GCDStart extends Event {
  def apply(context: Context): Context = {
    context.copy(pc = context.pc.copy(globalCoolDown = true))
  }
}

object GCDEnd extends Event {
  def apply(context: Context): Context = {
    context.copy(pc = context.pc.copy(globalCoolDown = false))
      .enqueue(context.elapsedTime, Active)
  }
}

case class RecastStart(action: Action) extends Event {
  def apply(context: Context): Context = {
    context.copy(pc = context.pc.copy(coolDown = context.pc.coolDown + action))
  }
}
case class RecastEnd(action: Action) extends Event {
  def apply(context: Context): Context = {
    context.copy(pc = context.pc.copy(coolDown = context.pc.coolDown - action))
      .enqueue(context.elapsedTime, Active)
  }
}

case class AddEnchant(gchar: GChar, enchant: Enchant) extends Event {
  def apply(context: Context): Context = {
    //FIXME
    gchar match {
      case _: PC    => context.copy(pc = context.pc.copy(enchants = context.pc.enchants + enchant))
      case _: Enemy => context.copy(enemy = context.enemy.copy(enchants = context.enemy.enchants + enchant))
    }
  }
}

case class DeleteEnchant(gchar: GChar, enchant: Enchant) extends Event {
  def apply(context: Context): Context = {
    //FIXME
    gchar match {
      case _: PC    => context.copy(pc = context.pc.copy(enchants = context.pc.enchants - enchant))
      case _: Enemy => context.copy(enemy = context.enemy.copy(enchants = context.enemy.enchants - enchant))
    }
  }
}

case class Damage(potency: Int, damegeType: DamegeType, source: PC, destination: Enemy, action: Action) extends Event {
  def apply(context: Context): Context = {
    val enchants = context.pc.enchants ++ context.enemy.enchants

    val (percent, absolute) =
      enchants
        .map(_.correct(this))
        .foldLeft((0, 0)) { case ((p1, a1), (p2, a2)) => (p1 + p2, a1 + a2) }
    val calcDamege = (potency * (1.0 + percent / 100d)).toInt + absolute

    val enemy = context.enemy
    context.copy(
      enemy = enemy.copy(
        damege = enemy.damege + calcDamege,
        enchants = context.enemy.enchants.filterNot(_.deleteBy(this))),
      pc = context.pc.copy(
        enchants = context.pc.enchants.filterNot(_.deleteBy(this))))
  }
}
