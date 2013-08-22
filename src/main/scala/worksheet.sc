object worksheet {
  println((1 to 12).foldRight(1)(_ + _))          //> 79
}