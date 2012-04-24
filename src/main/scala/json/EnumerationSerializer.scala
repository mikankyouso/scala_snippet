package json

import net.liftweb.json.JsonAST.JString
import net.liftweb.json.JsonAST.JValue
import net.liftweb.json.Formats
import net.liftweb.json.JsonDSL
import net.liftweb.json.MappingException
import net.liftweb.json.NoTypeHints
import net.liftweb.json.Serialization
import net.liftweb.json.TypeInfo
import net.liftweb.json.Serializer
import net.liftweb.json.DefaultFormats
import net.liftweb.json.ShortTypeHints

/**
 * https://groups.google.com/forum/?fromgroups#!topic/liftweb/04CQ2ATZAqM
 */
class EnumerationSerializer[E <: Enumeration: ClassManifest](enum: E)
  extends Serializer[E#Value] {

  import JsonDSL._
  val EnumerationClass = classOf[E#Value]
  val formats = Serialization.formats(NoTypeHints)

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), E#Value] = {
    case (TypeInfo(EnumerationClass, _), json) => json match {
      case JString(value) => enum.withName(value)
      case value => throw new MappingException("Can't convert " +
        value + " to " + EnumerationClass)
    }
  }

  def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
    case i: E#Value => i.toString
  }
}
