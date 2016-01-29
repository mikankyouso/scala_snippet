package coll

import scala.util.Random

object LazySort {

  def apply[A](xs: Seq[A])(implicit ord: Ordering[A]): Stream[A] = {
    def merge(xs: Stream[A], ys: Stream[A]): Stream[A] = (xs, ys) match {
      case (_, Stream())        => xs
      case (Stream(), _)        => ys
      case (x #:: xr, y #:: yr) => if (ord.compare(x, y) <= 0) x #:: merge(xr, ys) else y #:: merge(xs, yr)
    }
    def split(xs: Seq[A]): Stream[A] = xs.size match {
      case 0 => Stream.empty
      case 1 => Stream(xs(0))
      case _ => {
        val (s1, s2) = xs.splitAt(xs.size / 2)
        merge(split(s1), split(s2))
      }
    }
    val ss = xs.map(x => Stream(x))
    split(xs)
  }

  def main(args: Array[String]): Unit = {
    println("1-------------")
    val sorted = LazySort(new Random().shuffle(1 to 10000))
    println("2-------------")
    sorted.foreach(println)
    println("3-------------")

  }
}