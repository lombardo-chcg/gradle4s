package gradle4s

import org.scalatest.{FlatSpec, Matchers}
import zio.{DefaultRuntime, ZIO}

class RulesTest extends FlatSpec with Matchers {
  val runtime = new DefaultRuntime {}

  val projectExpectedError = "Invalid project name"
  val pkgExpectedErr = "Package name cannot contain Scala or Java reserved terms"

  "Rules" should "validate project names" in {
    assertSuccess("shapeless", Rules.validProjectName("shapeless"))
    assertFails(expectedError = projectExpectedError,  Rules.validProjectName("shapeless with spaces"))
  }

  it should "validate package names" in {
    assertSuccess("dev.zio", Rules.validProjectName("dev.zio"))
    assertFails(expectedError = pkgExpectedErr, Rules.validatePackageName("invalid.with.dots"))
  }

  private def assertSuccess(result: String, input: ZIO[Any, Throwable, String]) = {
    runtime.unsafeRun(input.either) match {
      case Left(e) => fail(s"Expected Right, got Left: $e")
      case Right(v) => assert(result == v)
    }
  }

  private def assertFails(expectedError: String, input: ZIO[Any, Throwable, String]) = {
    runtime.unsafeRun(input.either) match {
        case Left(e) => assert(e.getMessage.contains(expectedError))
        case Right(v) => fail(s"Expected Left, got Right: $v")
      }
  }
}
