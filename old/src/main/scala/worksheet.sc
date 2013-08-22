object worksheet {
  println((1 to 10).foldRight(1)(_ + _))          //> 56
}