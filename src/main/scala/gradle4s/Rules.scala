package gradle4s

import gradle4s.model._
import zio.{IO, Task}

import scala.reflect.internal.SymbolTable

object Rules {
  private val projectNameMatcher = """^[a-zA-Z0-9\-\\.]*$""".r
  private val projectNameErrorMessage = "Invalid project name.  Allowed characters are a-z, A-Z, 0-9 and hyphens.  ex: my-new-project"

  private val packageNameMatcher = """^[a-z][a-z0-9_]*(\.*[a-z][a-z0-9_]+)+[0-9a-z_]$""".r
  private val packageNameErrorMessage = "Invalid package name.  Must be all-lowercase ASCII letters and cannot start with a reserved identifier."

  private def reservedWordError(w: String) = s"Package name cannot contain Scala or Java reserved terms. Illegal term: [$w]"
  private val st: SymbolTable = scala.reflect.runtime.universe.asInstanceOf[scala.reflect.internal.SymbolTable]
  private val reservedWords: Set[String] = (st.nme.keywords ++ st.javanme.keywords).map(_.toString)

  val defaultPkg = "com.starter"

  def validProjectName(s: String): Task[String] =
    validateInput(Matcher(s, projectNameMatcher, projectNameErrorMessage))

  def validatePackageName(s: String): Task[String] = {
    validateInput(Matcher(s, packageNameMatcher, packageNameErrorMessage))
      .map(_.split("\\.").find(reservedWords.contains))
      .flatMap {
        case Some(b) => IO.fail(MatchError(reservedWordError(b)))
        case None    => IO.succeed(s)
      }
  }

  private def validateInput(m: Matcher): Task[String] = for {
    isMatch <- IO.effect(m.rule.pattern.matcher(m.input).matches())
    result <- if (isMatch) IO.succeed(m.input) else IO.fail(MatchError(m.errorMsg))
  } yield result
}
