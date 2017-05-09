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
      .freeze(true)
      .addHistory(time, this)
      .ifMap(gcd) { c => c.globalCoolDown(true).enqueue(time + 2500, GCDEnd) }
      .ifMap(recast > 0) { c => c.addCoolDown(this).enqueue(time + recast, RecastEnd(this)) }
      .enqueue(time + freezeTime, FreezeEnd)
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
    useAction(context).enqueue(hitTime, Damage.calculate(this, potency, damageType, context))
  }
}

case class DoTSpell(level: Int, mp: Int, cast: Int, recast: Int, duration: Int, hitPotency: Int, tickPotency: Int, damageType: DamageType) extends Spell {
  def use(context: Context): Context = {
    val dot = DoT(Damage.calculate(this, tickPotency, damageType, context), this)
    val hitTime = context.elapsedTime + 1000.max(cast - 500)
    useAction(context)
      .ifMap(hitPotency > 0) {
        _.enqueue(hitTime, Damage.calculate(this, hitPotency, damageType, context))
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
object Ruin3 extends DDSpell(1, 1000, 2500, 0, 120000, Magic)
object Bio extends DoTSpell(1, 50, 0, 0, 18000, 0, 40000, Magic)
object Miasma extends DoTSpell(1, 50, 2500, 0, 24000, 20000, 35000, Magic)
object Bio2 extends DoTSpell(1, 50, 2500, 0, 30000, 0, 35000, Magic)

trait Buff extends Ability {
  def enchant: Enchant
  def duration: Int
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

object Mosa extends Buff {
  val level = 1
  val recast = 180000
  val duration = 20000
  val enchant = new Enchant {
    val action = Mosa
    override def correctDamage(damageType: DamageType, action: Action): (Int, Int) = (20, 0)
  }
}

object Flow extends Ability {
  val level = 1
  val recast = 60000

  case class FlowEnchant(count: Int = 3) extends Enchant { val action = Flow }

  def use(context: Context): Context = {
    useAction(context)
      .enqueue(context.elapsedTime, AddEnchant(FlowEnchant(3)))
  }
}

object Burst extends Ability {
  val level = 1
  val recast = 60000

  override def usable(context: Context): Boolean = {
    super.usable(context) && context.enchants.exists(_.action == Flow)
  }

  def use(context: Context): Context = {
    val flow = context.enchants.find(_.action == Flow).get.asInstanceOf[Flow.FlowEnchant]
    val dots = context.enchants.count { e =>
      val act = e.action
      act == Bio || act == Miasma || act == Bio2
    }
    useAction(context)
      .enqueue(context.elapsedTime + 1000, Damage.calculate(this, dots * 100000, Magic, context))
      .enqueue(context.elapsedTime, DeleteEnchant(flow))
      .ifMap(flow.count >= 2) { _.enqueue(context.elapsedTime, AddEnchant(flow.copy(count = flow.count - 1))) }
  }
}

object PainFlare extends Ability {
  val level = 1
  val recast = 10000

  override def usable(context: Context): Boolean = {
    super.usable(context) && context.enchants.exists(_.action == Flow)
  }

  def use(context: Context): Context = {
    val flow = context.enchants.find(_.action == Flow).get.asInstanceOf[Flow.FlowEnchant]
    useAction(context)
      .enqueue(context.elapsedTime + 1000, Damage.calculate(this, 200000, Magic, context))
      .enqueue(context.elapsedTime, DeleteEnchant(flow))
      .ifMap(flow.count >= 2) { _.enqueue(context.elapsedTime, AddEnchant(flow.copy(count = flow.count - 1))) }
  }
}


