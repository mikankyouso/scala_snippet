package f14

trait GChar {
  def enchants: Set[Enchant]
}

case class PC(
    job: Job,
    level: Int = 60,
    actionHistory: List[(Int, Action)] = Nil,
    enchants: Set[Enchant] = Set.empty,
    globalCoolDown: Boolean = false,
    coolDown: Set[Action] = Set.empty) extends GChar {
}

case class Enemy(damege: Int = 0, enchants: Set[Enchant] = Set.empty) extends GChar {

}