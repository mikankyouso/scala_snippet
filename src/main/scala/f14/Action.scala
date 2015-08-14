package f14

trait Action {
  def gcd: Boolean
  def level: Int
  def tp: Int
  def mp: Int
  def cast: Int
  def recast: Int
  def freezeTime: Int = 850 max cast

  def use(context: Context): Context

  def usable(context: Context): Boolean = {
    !(context.freeze || context.coolDown.contains(this) || (gcd && context.globalCoolDown))
  }

  protected def useAction(context: Context): Context = {
    val time = context.elapsedTime
    context
      .copy(freeze = true, actionHistory = (time, this) :: context.actionHistory)
      .ifMap(gcd) { c => c.copy(globalCoolDown = true).enqueue(time + 2500, GCDEnd) }
      .ifMap(recast > 0) { c => c.copy(coolDown = context.coolDown + this).enqueue(time + recast, RecastEnd(this)) }
      .enqueue(time + freezeTime, FreezeEnd)
  }

  protected def damage(potency: Int, damageType: DamageType, context: Context): Damage = {
    val (percent, absolute) =
      context.enchants
        .map(_.correctDamage(damageType, this))
        .foldLeft((0, 0)) { case ((p1, a1), (p2, a2)) => (p1 + p2, a1 + a2) }
    Damage((potency * (1.0 + percent / 100d)).toInt + absolute, damageType, this)
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

trait Ability extends Action {
  val gcd = false
  val tp = 0
  val mp = 0
  val cast = 0
}

case class DDSpell(level: Int, mp: Int, cast: Int, recast: Int, potency: Int, damageType: DamageType) extends Spell {
  def use(context: Context): Context = {
    val hitTime = context.elapsedTime + 1000.max(cast - 500)
    useAction(context).enqueue(hitTime, damage(potency, damageType, context))
  }
}

case class DoTSpell(level: Int, mp: Int, cast: Int, recast: Int, duration: Int, hitPotency: Int, tickPotency: Int, damageType: DamageType) extends Spell {
  def use(context: Context): Context = {
    val dot = DoT(damage(tickPotency, damageType, context), this)
    val hitTime = context.elapsedTime + 1000.max(cast - 500)
    useAction(context)
      .ifMap(hitPotency > 0) {
        _.enqueue(hitTime, damage(hitPotency, damageType, context))
      }
      .cancelEvent {
        case DeleteEnchant(DoT(_, action)) if action == this => true
        case _ => false
      }
      .enqueue(hitTime, AddEnchant(dot))
      .enqueue(hitTime + duration, DeleteEnchant(dot))
  }
}

object Ruin extends DDSpell(1, 20, 2500, 0, 80000, Magic)
object Ruin2 extends DDSpell(1, 40, 0, 0, 80000, Magic)
object Bio extends DoTSpell(1, 50, 0, 0, 18000, 0, 40000, Magic)
object Miasma extends DoTSpell(1, 50, 2500, 0, 24000, 20000, 35000, Magic)
object Bio2 extends DoTSpell(1, 50, 2500, 0, 30000, 0, 35000, Magic)

abstract case class Buff(level: Int, recast: Int, duration: Int) extends Ability {
  def enchant: Enchant
  def use(context: Context): Context = {
    val hitTime = context.elapsedTime
    useAction(context)
      .cancelEvent {
        case DeleteEnchant(enchant) => true
        case _                      => false
      }
      .enqueue(hitTime, AddEnchant(enchant))
      .enqueue(hitTime + duration, DeleteEnchant(enchant))
  }
}

//object Mosa extends Buff(1, 180000, 20000, new Enchant {
//  def action = Mosa
//  override def correctDamage(damageType: DamageType, action: Action): (Int, Int) = (20, 0)
//})

object Mosa extends Buff(1, 180000, 20000) {
  val enchant = new Enchant {
    val action = Mosa
    override def correctDamage(damageType: DamageType, action: Action): (Int, Int) = (20, 0)
  }
}
