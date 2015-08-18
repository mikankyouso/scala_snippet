package f14

import scala.collection.SortedMap
import scala.collection.immutable.Queue
import scala.annotation.tailrec
import scala.collection._

trait ActionSelector {
  def select(context: Context, usableActions: Set[Action]): (Option[Action], ActionSelector)
}

trait Context {
  def pc: PC
  def actionSelector: ActionSelector
  def elapsedTime: Int
  def damage: Int
  def actionHistory: List[(Int, Action)]
  def enchants: Set[Enchant]
  def globalCoolDown: Boolean
  def coolDown: Set[Action]
  def freeze: Boolean

  def actionSelector(actionSelector: ActionSelector): Context
  def elapsedTime(elapsedTime: Int): Context
  def damage(damage: Int): Context

  def enqueue(time: Int, event: Event): Context
  def cancelEvent(f: Event => Boolean): Context

  def addHistory(time: Int, action: Action): Context

  def addEnchant(enchant: Enchant): Context
  def removeEnchant(enchant: Enchant): Context
  def removeEnchant(f: Enchant => Boolean): Context

  def globalCoolDown(globalCoolDown: Boolean): Context
  def addCoolDown(action: Action): Context
  def removeCoolDown(action: Action): Context
  def freeze(freeze: Boolean): Context

  def map(f: Context => Context): Context
  def flatMap(f: Context => Context): Context
  def ifMap(b: Boolean)(f: Context => Context): Context

  def forward: Context

}

case class MutableContext(
    pc: PC,
    var actionSelector: ActionSelector,
    var elapsedTime: Int = 0,
    var damage: Int = 0,
    var actionHistory: List[(Int, Action)] = Nil,
    val enchants: mutable.Set[Enchant] = mutable.Set.empty,
    var globalCoolDown: Boolean = false,
    val coolDown: mutable.Set[Action] = mutable.Set.empty,
    var freeze: Boolean = false,
    val eventQueue: mutable.PriorityQueue[(Int, Event)] = new mutable.PriorityQueue[(Int, Event)]()(Ordering.fromLessThan[(Int, Event)]((t1, t2) => t1._1 > t2._1))) extends Context {

  def actionSelector(actionSelector: ActionSelector): Context = copy(actionSelector = actionSelector)
  def elapsedTime(elapsedTime: Int): Context = copy(elapsedTime = elapsedTime)
  def damage(damage: Int): Context = copy(damage = damage)

  def enqueue(time: Int, event: Event): Context = { eventQueue += ((time, event)); this }
  def cancelEvent(f: Event => Boolean): Context = { eventQueue.filter(t => !f(t._2)); this }

  def addHistory(time: Int, action: Action): Context = copy(actionHistory = (time, action) :: actionHistory)

  def addEnchant(enchant: Enchant): Context = copy(enchants = enchants.filter(e => e.action != enchant.action) + enchant)
  def removeEnchant(enchant: Enchant): Context = copy(enchants = enchants - enchant)
  def removeEnchant(f: Enchant => Boolean): Context = copy(enchants = enchants.filterNot(f(_)))

  def globalCoolDown(globalCoolDown: Boolean): Context = copy(globalCoolDown = globalCoolDown)
  def addCoolDown(action: Action): Context = copy(coolDown = coolDown + action)
  def removeCoolDown(action: Action): Context = copy(coolDown = coolDown - action)
  def freeze(freeze: Boolean): Context = copy(freeze = freeze)

  //  def actionSelector(actionSelector: ActionSelector): Context = {
  //    this.actionSelector = actionSelector
  //    this
  //  }
  //  def elapsedTime(elapsedTime: Int): Context = {
  //    this
  //  }
  //  def damage(damage: Int): Context = {
  //    this
  //  }
  //
  //  def enqueue(time: Int, event: Event): Context = {
  //    this
  //  }
  //  def cancelEvent(f: Event => Boolean): Context = {
  //    this
  //  }
  //
  //  def addHistory(time: Int, action: Action): Context = {
  //    this
  //  }
  //
  //  def addEnchant(enchant: Enchant): Context = {
  //    this
  //  }
  //  def removeEnchant(enchant: Enchant): Context = {
  //    this
  //  }
  //  def removeEnchant(f: Enchant => Boolean): Context = {
  //    this
  //  }
  //
  //  def globalCoolDown(globalCoolDown: Boolean): Context = {
  //    this
  //  }
  //  def addCoolDown(action: Action): Context = {
  //    this
  //  }
  //  def removeCoolDown(action: Action): Context = {
  //    this
  //  }
  //  def freeze(freeze: Boolean): Context = {
  //    this
  //  }

  def map(f: Context => Context): Context = f(this)
  def flatMap(f: Context => Context): Context = f(this)
  def ifMap(b: Boolean)(f: Context => Context): Context = if (b) f(this) else this

  @tailrec
  final def forward: Context = {
    val (time, event) = eventQueue.dequeue
    val nextContext = copy(elapsedTime = time)
    //println("%8.2f %10.2f %s".format(time / 1000.0, damage / 1000.0, event))
    event match {
      case End => nextContext
      case _   => event.apply(nextContext).asInstanceOf[MutableContext].forward
    }
  }
}

case class ImmutableContext(
    pc: PC,
    actionSelector: ActionSelector,
    elapsedTime: Int = 0,
    damage: Int = 0,
    actionHistory: List[(Int, Action)] = Nil,
    enchants: Set[Enchant] = Set.empty,
    globalCoolDown: Boolean = false,
    coolDown: Set[Action] = Set.empty,
    freeze: Boolean = false,
    eventQueue: EventQueue = EventQueue()) extends Context {

  def actionSelector(actionSelector: ActionSelector): Context = copy(actionSelector = actionSelector)
  def elapsedTime(elapsedTime: Int): Context = copy(elapsedTime = elapsedTime)
  def damage(damage: Int): Context = copy(damage = damage)

  def enqueue(time: Int, event: Event): Context = copy(eventQueue = eventQueue.enqueue(time, event))
  def cancelEvent(f: Event => Boolean): Context = copy(eventQueue = eventQueue.filter(!f(_)))

  def addHistory(time: Int, action: Action): Context = copy(actionHistory = (time, action) :: actionHistory)

  def addEnchant(enchant: Enchant): Context = copy(enchants = enchants.filter(e => e.action != enchant.action) + enchant)
  def removeEnchant(enchant: Enchant): Context = copy(enchants = enchants - enchant)
  def removeEnchant(f: Enchant => Boolean): Context = copy(enchants = enchants.filterNot(f(_)))

  def globalCoolDown(globalCoolDown: Boolean): Context = copy(globalCoolDown = globalCoolDown)
  def addCoolDown(action: Action): Context = copy(coolDown = coolDown + action)
  def removeCoolDown(action: Action): Context = copy(coolDown = coolDown - action)
  def freeze(freeze: Boolean): Context = copy(freeze = freeze)

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
      case _   => event.apply(nextContext).asInstanceOf[ImmutableContext].forward
    }
  }
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