package f14

import scala.collection.SortedMap
import scala.collection.immutable.Queue
import scala.annotation.tailrec

case class Context(
    pc: PC,
    actionSelector: ActionSelector,
    elapsedTime: Int = 0,
    damage: Int = 0,
    eventQueue: EventQueue = EventQueue(),
    actionHistory: List[(Int, Action)] = Nil,
    enchants: Set[Enchant] = Set.empty,
    globalCoolDown: Boolean = false,
    coolDown: Set[Action] = Set.empty,
    freeze: Boolean = false) {

  def enqueue(time: Int, event: Event): Context = copy(eventQueue = eventQueue.enqueue(time, event))
  def cancelEvent(f: Event => Boolean): Context = copy(eventQueue = eventQueue.filter(!f(_)))

  def addEnchant(enchant: Enchant): Context = {
    copy(enchants = enchants.filter(e => e.action != enchant.action) + enchant)
  }
  def removeEnchant(enchant: Enchant): Context = {
    copy(enchants = enchants - enchant)
  }

  def map(f: Context => Context): Context = f(this)
  def flatMap(f: Context => Context): Context = f(this)
  def ifMap(b: Boolean)(f: Context => Context): Context = if (b) f(this) else this

  @tailrec
  final def forward: Context = {
    val (time, event, queue) = eventQueue.dequeue
    val nextContext = copy(elapsedTime = time, eventQueue = queue)
    //println("%8.2f %10.2f %s".format(time / 1000.0, damage / 1000.0, event))
    event match {
      case End => nextContext
      case _   => event.apply(nextContext).forward
    }
  }
}

trait ActionSelector {
  def select(context: Context, usableActions: Set[Action]): (Option[Action], ActionSelector)
}

case class EventQueue(map: SortedMap[Int, Queue[Event]] = SortedMap.empty) {
  def enqueue(time: Int, event: Event): EventQueue = {
    val q = map.getOrElse(time, Queue.empty)
    new EventQueue(map.updated(time, q.enqueue(event)))
  }

  def dequeue: (Int, Event, EventQueue) = {
    val (time, events) = map.head //NoSuchElementException
    val (event, q) = events.dequeue
    events.dequeue match {
      case (event, Queue()) => (time, event, EventQueue(map - time))
      case (event, q)       => (time, event, EventQueue(map.updated(time, q)))
    }
  }

  def isEmpty: Boolean = map.isEmpty

  def filter(f: Event => Boolean): EventQueue = {
    EventQueue(map.mapValues(_.filter(f)).filter { case (_, q) => !q.isEmpty })
  }
}