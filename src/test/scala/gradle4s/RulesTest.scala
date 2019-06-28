package gradle4s

import org.scalatest.{FlatSpec, Matchers}
import zio.{DefaultRuntime, ZIO}

class RulesTest extends FlatSpec with Matchers {
  val runtime = new DefaultRuntime {}

  val projectExpectedError = "Invalid project name"
  val pkgExpectedErr = "Package name cannot contain Scala or Java reserved terms"

  "Rules" should "validate project names" in {
    List("shapeless", "shapeless_underscores", "shapeless-hyphen")
      .foreach { s => assertSuccess(s, Rules.validProjectName(s)) }
    assertFails(expectedError = projectExpectedError,  Rules.validProjectName("shapeless with spaces"))
  }

  it should "validate package names" in {
    assertSuccess("dev.zio", Rules.validatePackageName("dev.zio"))
    assertSuccess("shapeless", Rules.validatePackageName("shapeless"))
    assertFails(expectedError = "Invalid package name",  Rules.validatePackageName("shapeless with spaces"))
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
