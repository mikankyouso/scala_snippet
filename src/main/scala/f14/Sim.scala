package f14

import scala.annotation.tailrec

case class Sim(pc: PC, timeLimit: Int) {
  def start: Context = {
    val context = Context(pc)
    forward(context.enqueue(0, Start).enqueue(0, Active).enqueue(timeLimit, End))
  }

  @tailrec
  private def forward(context: Context): Context = {
    val (time, event, queue) = context.eventQueue.dequeue
    val nextContext = context.copy(elapsedTime = time, eventQueue = queue)
    println("%8.2f %6d %s".format(time / 1000.0, context.damege, event))
    event match {
      case End => nextContext
      case _   => forward(event.apply(nextContext))
    }
  }
}

object Sim {
  def main(args: Array[String]): Unit = {
    println(Sim(PC(Sum), 30000).start.damege)
  }
}