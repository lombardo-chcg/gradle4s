package gradle4s

import zio.console.{Console, getStrLn, putStr}
import zio.{IO, Task, UIO, ZIO}

object ConsoleOps {
  def promptUser[T](prompt: String, default: T, transformer: String => Task[T]): ZIO[Console, Throwable, T] =
    putStr(prompt)
      .flatMap(_ => getStrLn)
      .flatMap(s => {
        if (s.isEmpty) UIO.succeed(default) else transformer(s)
      })

  def promptUser(prompt: String): ZIO[Console, Throwable, String] =
    putStr(prompt)
      .flatMap(_ => getStrLn)
      .flatMap(s => {
        if (s.isEmpty) promptUser(prompt) else IO.succeed(s)
      })
}
