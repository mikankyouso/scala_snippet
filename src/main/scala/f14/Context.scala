package f14

import scala.collection.immutable.TreeMap
import scala.collection.immutable.Queue
import java.util.NoSuchElementException

case class Context(
    pc: PC,
    enemy: Enemy,
    elapsedTime: Int = 0,
    eventQueue: EventQueue = EventQueue()) {
  def enqueue(time: Int, event: Event): Context = copy(eventQueue = eventQueue.enqueue(time, event))

  def map(f: Context => Context): Context = f(this)
  def flatMap(f: Context => Context): Context = f(this)

}

case class EventQueue(map: TreeMap[Int, Queue[Event]] = TreeMap.empty) {
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
}