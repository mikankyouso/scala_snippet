package f14

trait Enchant {
  def correct(damege: Damage): (Int, Int) = (0, 0)
  def deleteBy(event: Event): Boolean = false
  def tick(context: Context): Context = context
}

case class DoT(potency: Int, damegeType: DamegeType, action: Action) extends Enchant {
  override def tick(context: Context): Context = {
    context.enqueue(context.elapsedTime, Damage(potency, damegeType, action))
  }
}