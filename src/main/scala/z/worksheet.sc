import scalaz._
import Scalaz._

object worksheet {
  Apply[Option].apply2(some(1), some(2))((a, b) => a + b)
                                                  //> res0: Option[Int] = Some(3)
  Traverse[List].traverse(List(1, 2, 3))(i => some(i))
                                                  //> res1: Option[List[Int]] = Some(List(1, 2, 3))
  
  List(List(1)).join                              //> res2: List[Int] = List(1)
  List(true, false).ifM(List(0, 1), List(2, 3))   //> res3: List[Int] = List(0, 1, 2, 3)
  
  NonEmptyList(1, 2, 3).cojoin                    //> res4: scalaz.NonEmptyList[scalaz.NonEmptyList[Int]] = NonEmptyList(NonEmptyL
                                                  //| ist(1, 2, 3), NonEmptyList(2, 3), NonEmptyList(3))
  1.node(2.leaf, 3.node(4.leaf))                  //> res5: scalaz.Tree[Int] = <tree>
  List(some(1), none).suml                        //> res6: Option[Int] = Some(1)
}