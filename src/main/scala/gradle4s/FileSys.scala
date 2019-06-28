package gradle4s

import ammonite.ops
import ammonite.ops._
import zio.{IO, Task}

object FileSys {

  private val parentDir: ops.Path = pwd/up

  def defaultPath: ops.Path = parentDir

  def toPath(s: String): Task[ops.Path] = IO.effect(Path(s))

  def fileExists(req: ops.Path): Task[Boolean] = IO.effect(exists(req))
}
