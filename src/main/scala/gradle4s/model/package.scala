package gradle4s

import ammonite.ops.Path

import scala.util.matching.Regex

package object model {

  case class UserRequest(projectName: String, packageName: String, parentDirectory: Path) {
    def path: os.Path = parentDirectory/projectName
  }

  case class Matcher(input: String, rule: Regex, errorMsg: String)
  case class MatchResult(value: String)

  abstract class AppError(message: String) extends Throwable(message)
  case class MatchError(message: String) extends AppError(message)
  case class FileExistsError(message: String) extends AppError(message)

  object FileExistsError {
    def apply(ur: UserRequest): FileExistsError =
      new FileExistsError(s"File or directory already exists at path (${ur.path}).  Choose another project name, or clear existing path and try again.")
  }
}
