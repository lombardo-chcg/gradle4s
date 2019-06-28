package gradle4s

import gradle4s.model.{AppError, FileExistsError, UserRequest}
import zio._
import zio.console._

object Main extends App {
  val program = for {
    u  <- getUserRequest
    _  <- copyTemplate(u)
  } yield u

  def run(args: List[String]) = program.fold(
      // exit code 0 for all because gradle cli hijacks error code results and displays noisy output
      {
        case th: AppError => println(th.getMessage); 0
        case e => { println(e.getMessage + Option(e.getCause).getOrElse(" No cause provided") + ".  Stacktrace:") ; e.printStackTrace(); 0}
      },
      _ => { println("Success"); 0 }
    )

  def getUserRequest: ZIO[Console, Throwable, UserRequest] =
    for {
      _           <- putStrLn("🐘💃 gradle4s 💃🐘")
      projectName <- ConsoleOps.promptUser("Enter project name: ")
      pn          <- Rules.validProjectName(projectName)

      packageName <- ConsoleOps.promptUser(s"Enter package name (${Rules.defaultPkg}): ", Rules.defaultPkg, UIO.succeed(_))
      pkg         <- Rules.validatePackageName(packageName)

      pth   = FileSys.defaultPath
      path  <- ConsoleOps.promptUser(s"Enter project path ($pth/$pn): ", pth, FileSys.toPath _)

      ur = UserRequest(pn, pkg, path)
      b  <- FileSys.fileExists(ur.path)
      _  <-  if (!b) ZIO.succeed(b) else ZIO.fail(FileExistsError(ur))
    } yield ur

  def copyTemplate(u: UserRequest): ZIO[Console, Throwable, UserRequest] =
    for {
      _   <- putStrLn(s"creating new Scala Gradle project at (${u.path})")
      _   <- FileSys.cpTemplate(u.path)
      _   <- TemplateOps.transform(u)
      _   <- if (u.packageName != Rules.defaultPkg) TemplateOps.moveSrcFiles(u) else IO.succeed(u)
    } yield u
}