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
    !(context.coolDown.contains(this) || (gcd && context.globalCoolDown))
  }

  protected def useAction(context: Context): Context = {
    context
      .copy(actionHistory = (context.elapsedTime, this) :: context.actionHistory)
      .ifMap(gcd) { c => c.enqueue(c.elapsedTime, GCDStart).enqueue(c.elapsedTime + 2500, GCDEnd) }
      .ifMap(recast > 0) { c => c.enqueue(c.elapsedTime, RecastStart(this)).enqueue(c.elapsedTime + recast, RecastEnd(this)) }
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

trait AB extends Action {
  val gcd = false
  val tp = 0
  val mp = 0
  val cast = 0
}

case class DDSpell(level: Int, mp: Int, cast: Int, recast: Int, potency: Int, damegeType: DamegeType) extends Spell {
  def use(context: Context): Context = {
    useAction(context).enqueue(context.elapsedTime + 1000.max(cast - 500), Damage(potency, damegeType, this))
  }
}

case class DoTSpell(level: Int, mp: Int, cast: Int, recast: Int, duration: Int, hitPotency: Int, tickPotency: Int, damegeType: DamegeType) extends Spell {
  val dot = DoT(tickPotency, damegeType, this)
  def use(context: Context): Context = {
    val hitTime = context.elapsedTime + 1000.max(cast - 500)
    useAction(context)
      .ifMap(hitPotency > 0) { _.enqueue(hitTime, Damage(hitPotency, damegeType, this)) }
      .cancel(DeleteEnchant(dot))
      .enqueue(hitTime, AddEnchant(dot))
      .enqueue(hitTime + duration, DeleteEnchant(dot))
  }
}

object Ruin extends DDSpell(1, 20, 2500, 0, 80, Magic) {}
object Ruin2 extends DDSpell(1, 40, 0, 0, 80, Magic) {}
object Bio extends DoTSpell(1, 50, 0, 0, 18000, 0, 40, Magic) {}
object Mosa extends AB {
  object buff extends Enchant {
    override def correct(damege: Damage): (Int, Int) = (20, 0)
  }
  val level = 1
  val recast = 180000
  def use(context: Context): Context = {
    val hitTime = context.elapsedTime
    useAction(context)
      .cancel(DeleteEnchant(buff))
      .enqueue(hitTime, AddEnchant(buff))
      .enqueue(hitTime + 200000, DeleteEnchant(buff))
  }
}

