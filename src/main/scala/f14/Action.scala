package f14

trait Action {
  def gcd: Boolean
  def level: Int
  def tp: Int
  def mp: Int
  def cast: Int
  def recast: Int
  def freezeAction: Int = 850 min cast
  def use(context: Context): Context
  def usable(context: Context): Boolean = {
    !(context.pc.coolDown.contains(this) || (gcd && context.pc.globalCoolDown))
  }
  protected def coolDownEvent(context: Context): Context = {
    if (gcd) {
      context.enqueue(context.elapsedTime, GCDStart).enqueue(context.elapsedTime + 2500, GCDEnd)
    } else {
      context
    } map { context =>
      if (recast > 0) {
        context.enqueue(context.elapsedTime, RecastStart(this)).enqueue(context.elapsedTime + recast, RecastEnd(this))
      } else {
        context
      }
    }
  }
  override def toString(): String = this.getClass.getSimpleName

}

trait WS extends Action {
  val gcd = true
}

trait Spell extends Action {
  val gcd = true
  val tp = 0
}

case class DDSpell(level: Int, mp: Int, cast: Int, recast: Int, potency: Int, damegeType: DamegeType) extends Spell {
  def use(context: Context): Context = {
    val pc = context.pc
    val actionHistory = pc.actionHistory
    val time = context.elapsedTime
    coolDownEvent(context).map(context => context.copy(
      pc =
        pc.copy(actionHistory = (time, this) :: actionHistory),
      eventQueue =
        context.eventQueue.enqueue(time + 1000.max(cast - 500), Damage(potency, damegeType, pc, context.enemy, this))))
  }
}

trait AB extends Action {
  val gcd = false
}

object Ruin extends DDSpell(1, 20, 2500, 0, 80, Magic) {}
