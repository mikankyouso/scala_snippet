package inflist

import scala.collection.Iterator
import scala.annotation.tailrec

// http://itchyny.hatenablog.com/entry/2016/01/17/120000
object InfList extends App {
  // let squares = [ n * n | n <- [1..] ]
  def squares: Iterator[Long] = Iterator.iterate(1L)(_ + 1).map(n => n * n)

  // let triangulars = [ n * (n + 1) `div` 2 | n <- [1..] ]
  def triangulars: Iterator[Long] = Iterator.iterate(1L)(_ + 1).map(n => n * (n + 1) / 2)

  //squares.take(10).foreach(println)
  //triangulars.take(10).foreach(println)

  def union2(xs: Iterator[Long], ys: Iterator[Long]): Iterator[Long] = {
    def f(xs: Stream[Long], ys: Stream[Long]): Stream[Long] = (xs, ys) match {
      case (_, Stream())                    => xs
      case (Stream(), _)                    => ys
      case (x #:: xr, y #:: yr) if (x == y) => x #:: f(xr, yr)
      case (x #:: xr, y #:: yr) if (x < y)  => x #:: f(xr, ys)
      case (x #:: xr, y #:: yr)             => y #:: f(xs, yr)
    }
    f(xs.toStream, ys.toStream).toIterator
  }

  println(union2(squares, triangulars).drop(1000 - 1).next())
  println(union2(squares, triangulars).drop(1000000 - 1).next())

}