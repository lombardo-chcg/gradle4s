package gradle4s

import ammonite.ops
import ammonite.ops._
import zio.{IO, Task}

object FileSys {
  private val templateDir = pwd/"basic-template"
  private val parentDir: ops.Path = pwd/up

  def defaultPath: ops.Path = parentDir

  def toPath(s: String): Task[ops.Path] = IO.effect(Path(s))

  def fileExists(req: ops.Path): Task[Boolean] = IO.effect(exists(req))

  def cpTemplate(path: os.Path): Task[Unit] = IO.effect(cp(templateDir, path))

  def runShellCommand(path: os.Path, cmd: Array[String]): Task[Unit] = IO.effect {
    implicit val p = path
    val command = Shellable(cmd)
    Shellout.%(command)
  }
}
