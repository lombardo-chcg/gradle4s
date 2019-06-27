package gradle4s

import org.scalatest.{FlatSpec, Matchers}

class SampleTest extends FlatSpec with Matchers {
  "gradle4s" should "run a sample test" in {
    7 + 9 should be(16)
  }
}
