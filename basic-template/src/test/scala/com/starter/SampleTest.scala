package com.starter

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class SampleTest extends AnyFlatSpec with Matchers {
  "com.starter" should "run a sample test" in {
    7 + 9 should be(16)
  }
}
