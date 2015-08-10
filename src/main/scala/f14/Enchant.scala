package f14

trait Enchant {
  def correct(damege: Damage): (Int, Int) = (0, 0)
  def deleteBy(event: Event): Boolean = false
  def tick(context: Context): Context
}