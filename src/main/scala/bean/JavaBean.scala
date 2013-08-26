package bean

import java.beans.{ BeanInfo, Introspector }
import java.beans.PropertyDescriptor

class JavaBean[A <: AnyRef](bean: A) {
  val beanInfo: BeanInfo = Introspector.getBeanInfo(bean.getClass())

  def propertyDescriptors: Array[PropertyDescriptor] = beanInfo.getPropertyDescriptors()
  def readablePropertyDescriptors: Array[PropertyDescriptor] = propertyDescriptors
    .filter(d => d.getName() != "class" && d.getReadMethod() != null)
  def writablePropertyDescriptors: Array[PropertyDescriptor] = propertyDescriptors
    .filter(d => d.getWriteMethod() != null)

  def dump(): String = {
    readablePropertyDescriptors
      .map(desc => s"${desc.getName()} -> ${desc.getReadMethod().invoke(bean)}")
      .mkString(bean.getClass().getSimpleName() + "(", ", ", ")")
  }
}

object JavaBean {
  def apply[A <: AnyRef](bean: A) = new JavaBean(bean)
}