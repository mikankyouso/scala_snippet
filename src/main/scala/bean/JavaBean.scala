package bean

import java.beans.{ BeanInfo, Introspector }
import java.beans.PropertyDescriptor

class JavaBean[A <: AnyRef](bean: A) {
  val beanInfo: BeanInfo = Introspector.getBeanInfo(bean.getClass())

  lazy val propertyDescriptors: Seq[PropertyDescriptor] =
    beanInfo.getPropertyDescriptors().filter(d => d.getName() != "class").sortBy(_.getName)
  lazy val readablePropertyDescriptors: Seq[PropertyDescriptor] = propertyDescriptors
    .filter(d => d.getReadMethod() != null)
  lazy val writablePropertyDescriptors: Seq[PropertyDescriptor] = propertyDescriptors
    .filter(d => d.getWriteMethod() != null)

  def dump(multiLine: Boolean = false): String = {
    val name = bean.getClass().getSimpleName()
    if (multiLine)
      readablePropertyDescriptors
        .map(desc => s"\r\n  ${desc.getName()} -> ${desc.getReadMethod().invoke(bean)}")
        .mkString(name + "(", ",", "\r\n)")
    else
      readablePropertyDescriptors
        .map(desc => s"${desc.getName()} -> ${desc.getReadMethod().invoke(bean)}")
        .mkString(name + "(", ", ", ")")
  }
}

object JavaBean {
  def apply[A <: AnyRef](bean: A) = new JavaBean(bean)
}