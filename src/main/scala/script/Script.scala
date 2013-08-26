package script

import javax.script.ScriptEngineFactory
import javax.script.ScriptEngineManager
import bean.JavaBean
import javax.script.ScriptEngine
import javax.script.Compilable
import javax.script.Invocable
import javax.script.ScriptContext
import scala.sys.SystemProperties

object Script {
  import collection.JavaConversions._

  val manager = new ScriptEngineManager()
  def main(args: Array[String]) {
    sys.props += ("org.jruby.embed.localvariable.behavior" -> "persistent")

    manager.getEngineFactories().foreach(foo)

    manager.getBindings().put("foo", "111")
    manager.getBindings().put("$foo", "111")

    eval("rhino", """
      println("*" + foo)
      println("*" + bar)
      foo = "222"
      bar = "bbb"
      a = 1
      var b = 2
      a + b
    """)

    eval("jruby", """
      puts(foo)
      puts(bar)
      foo = "333"
      $foo = "333"
      bar = "ccc"
      a = 1
      @b = 2
      a + @b
    """)

    eval("groovy", """
      println(foo)
      println(bar)
      foo = "444"
      bar = "ddd"
      def a = 1
      def b = 2
      a + b
    """)

  }

  def foo(factory: ScriptEngineFactory) {
    val engine = factory.getScriptEngine()
    println("======== " + factory.getEngineName() + " ========")
    println(JavaBean(factory).dump())
    println(factory.getMethodCallSyntax("obj", "method", "arg0", "arg1"))
    println("--------")
    println(factory.getOutputStatement("str"))
    println("--------")
    println(factory.getProgram("prog0", "prog1"))
    println("--------")
    println(engine.isInstanceOf[Compilable])
    println(engine.isInstanceOf[Invocable])
    println()
  }

  private def eval(name: String, script: String): Unit = {
    println(s"*** $name ***")
    val engine = manager.getEngineByName(name)
    engine.getBindings(ScriptContext.ENGINE_SCOPE).put("bar", "aaa")

    println("[global]")
    engine.getBindings(ScriptContext.GLOBAL_SCOPE).foreach(kv => println(kv))
    println()
    println("[engine]")
    engine.getBindings(ScriptContext.ENGINE_SCOPE).foreach(kv => println(kv))
    println()

    println("[ret]")
    val ret = engine.eval(script)
    println(ret)
    println(ret.getClass())
    println()

    println("[global]")
    engine.getBindings(ScriptContext.GLOBAL_SCOPE).foreach(kv => println(kv))
    println()
    println("[engine]")
    engine.getBindings(ScriptContext.ENGINE_SCOPE).foreach(kv => println(kv))
    println()
  }
}