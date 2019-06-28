package gradle4s

import gradle4s.model.{AppError, FileExistsError, UserRequest}
import zio._
import zio.console._

object Main extends App {

  def run(args: List[String]) =
    getUserRequest.fold(
      // exit code 0 for all because gradle cli hijacks error code results and displays noisy output
      {
        case th: AppError => println(th.getMessage); 0
        case e => { println(e.getMessage + Option(e.getCause).getOrElse(" No cause provided") + ".  Stacktrace:") ; e.printStackTrace(); 0}
      },
      _ => { println("Success"); 0 }
    )

  val getUserRequest: ZIO[Console, Throwable, UserRequest] =
    for {
      _           <- putStrLn("🐘🚀 Gradle + Scala Project Starter 🐘🚀")
      projectName <- promptUser("Enter project name: ")
      pn          <- Rules.validProjectName(projectName)

      packageName <- promptUser(s"Enter package name (${Rules.defaultPkg}): ", Rules.defaultPkg, UIO.succeed(_))
      pkg         <- Rules.validatePackageName(packageName)

      pth   = FileSys.defaultPath
      path  <- promptUser(s"Enter project path ($pth/$pn): ", pth, FileSys.toPath _)

      ur = UserRequest(pn, pkg, path)
      b  <- FileSys.fileExists(ur.path)
      _  <-  if (!b) ZIO.succeed(b) else ZIO.fail(FileExistsError(ur))
    } yield ur

  private def promptUser[T](prompt: String, default: T, transformer: String => Task[T]): ZIO[Console, Throwable, T] =
    putStr(prompt)
      .flatMap(_ => getStrLn)
      .flatMap(s => {
        if (s.isEmpty) UIO.succeed(default) else transformer(s)
      })

  private def promptUser(prompt: String): ZIO[Console, Throwable, String] =
    putStr(prompt)
      .flatMap(_ => getStrLn)
      .flatMap(s => {
        if (s.isEmpty) promptUser(prompt) else IO.succeed(s)
      })
}