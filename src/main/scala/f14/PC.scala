package f14

case class PC(
    job: Job,
    level: Int = 60,
    actionHistory: List[(Int, Action)] = Nil,
    enchants: Set[Enchant] = Set.empty,
    globalCoolDown: Boolean = false,
    coolDown: Set[Action] = Set.empty) {
}