package util

object MyImplicits {

  implicit class Pipe[A](a: A) {
    def |>[C >: A, B](f: C => B): B = f(a)
  }

}
