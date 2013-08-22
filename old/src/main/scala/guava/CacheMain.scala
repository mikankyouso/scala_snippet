/*
package guava

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder

object CacheMain {
  def main(args: Array[String]) = {
    val c: Cache[String, String] = CacheBuilder.newBuilder().weakKeys().weakValues().maximumSize(100).build()
    c.put("a", "b")
    c.put(new String("c"), "d")
    c.put("e", new String("f"))

    println(c.asMap())
    sys.runtime.gc
    println(c.asMap())

    val c2: Cache[String, String] = CacheBuilder.newBuilder().maximumSize(5).build()
    1 to 100 foreach (n => c2.put("x" + n, "y" + n))

    println(c2.asMap())
  }
}
*/ 