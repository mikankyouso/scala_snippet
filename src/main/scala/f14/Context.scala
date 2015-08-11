package f14

import scala.collection.SortedMap
import scala.collection.immutable.Queue

case class Context(
    pc: PC,
    elapsedTime: Int = 0,
    damege: Int = 0,
    eventQueue: EventQueue = EventQueue(),
    actionHistory: List[(Int, Action)] = Nil,
    enchants: Set[Enchant] = Set.empty,
    globalCoolDown: Boolean = false,
    coolDown: Set[Action] = Set.empty) {
  def enqueue(time: Int, event: Event): Context = copy(eventQueue = eventQueue.enqueue(time, event))
  def cancel(event: Event): Context = copy(eventQueue = eventQueue.filter(_ != event))

  def map(f: Context => Context): Context = f(this)
  def flatMap(f: Context => Context): Context = f(this)
  def ifMap(b: Boolean)(f: Context => Context): Context = if (b) f(this) else this
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