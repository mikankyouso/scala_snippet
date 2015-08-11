package f14

case class Job(actions: Set[Action])

object Sum extends Job(Set(Ruin, Ruin2, Bio, Mosa))