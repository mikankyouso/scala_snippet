/*
package json

import net.liftweb.json.JsonAST.JString
import net.liftweb.json.JsonAST.JValue
import net.liftweb.json.JsonDSL.string2jvalue
import net.liftweb.json.Formats
import net.liftweb.json.Serializer
import net.liftweb.json.TypeInfo
import net.liftweb.json.JsonDSL

class ObjectSerializer[T: ClassManifest](objs: T*) extends Serializer[T] {

  import JsonDSL._

  val SuperType = implicitly[ClassManifest[T]].erasure
  val obj2str = objs.map(obj => obj -> obj.getClass.getSimpleName.init).toMap
  val str2obj = obj2str.map { case (k, v) => (v, k) }

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), T] = {
    case (TypeInfo(SuperType, _), JString(value)) if str2obj.contains(value) => str2obj(value)
  }

  def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
    case o if SuperType.isInstance(o) && obj2str.contains(o.asInstanceOf[T]) => obj2str(o.asInstanceOf[T])
  }

}
*/ 