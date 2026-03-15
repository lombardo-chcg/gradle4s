package gradle4s

import zio.{IO, Task}

object FileSys {
  private val templateDir = os.pwd / "basic-template"
  private val parentDir: os.Path = os.pwd / os.up

  def defaultPath: os.Path = parentDir

  def toPath(s: String): Task[os.Path] = IO.effect(os.Path(s))

  def fileExists(req: os.Path): Task[Boolean] = IO.effect(os.exists(req))

  def cpTemplate(path: os.Path): Task[Unit] = IO.effect(os.copy(templateDir, path))

  def runShellCommand(path: os.Path, cmd: Array[String]): Task[Unit] = IO.effect {
    os.proc(cmd).call(cwd = path)
  }
}
