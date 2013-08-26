object worksheet {
  println((1 to 12).foldRight(1)(_ + _))          //> 79
  Map(1 -> "a", 2 -> "b")                         //> res0: scala.collection.immutable.Map[Int,String] = Map(1 -> a, 2 -> b)
  case class Foo(name: String, age: Int)
  Foo("aaa",123)                                  //> res1: worksheet.Foo = Foo(aaa,123)
}