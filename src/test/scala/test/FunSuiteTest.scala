package test
import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FunSuiteTest extends FunSuite {
  test("FunSuiteでためしにテスト") {
    assert("1,2".split(",") === Array("1", "2"))
  }
}