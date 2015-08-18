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
    //    println(context)
    val usableActions = context.pc.job.actions.filter(_.usable(context))
    context.ifMap(!usableActions.isEmpty) { context =>
      val (optAction, selector) = context.actionSelector.select(context, usableActions)
      //optAction.foreach(a => println("%8.2f 【%s】".format(context.elapsedTime / 1000.0, a)))
      optAction
        .map(_.use(context))
        .getOrElse(context)
        .actionSelector(selector)
      //      val action = usableActions.toArray.apply(Random.nextInt(usableActions.size))
      //      println("%8.2f 【%s】".format(context.elapsedTime / 1000.0, action))
      //      action.use(context)
    }
  }
}

object GCDEnd extends Event {
  def apply(context: Context): Context = {
    context.globalCoolDown(false)
      .enqueue(context.elapsedTime, Active)
  }
}

case class RecastEnd(action: Action) extends Event {
  def apply(context: Context): Context = {
    context.removeCoolDown(action)
      .enqueue(context.elapsedTime, Active)
  }
}

object FreezeEnd extends Event {
  def apply(context: Context): Context = {
    context.freeze(false)
      .enqueue(context.elapsedTime, Active)
  }
}

case class AddEnchant(enchant: Enchant) extends Event {
  def apply(context: Context): Context = {
    context.addEnchant(enchant)
  }
}

case class DeleteEnchant(enchant: Enchant) extends Event {
  def apply(context: Context): Context = {
    context.removeEnchant(enchant)
  }
}

case class Damage(potency: Int, damageType: DamageType, action: Action) extends Event {
  def apply(context: Context): Context = {
    context
      .damage(context.damage + potency)
      .removeEnchant(_.deleteBy(this))
  }
}
