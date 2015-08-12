package f14

trait Enchant {
  def action: Action
  def correctDamage(damageType: DamageType, action: Action): (Int, Int) = (0, 0)
  def deleteBy(event: Event): Boolean = false
  def tick(context: Context): Context = context
}

case class DoT(damage: Damage, action: Action) extends Enchant {
  override def tick(context: Context): Context = {
    context.enqueue(context.elapsedTime, damage)
  }
}