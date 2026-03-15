package gradle4s

import gradle4s.model.UserRequest
import zio._
import zio.console._

object TemplateOps {
  val PROJECT_NAME_PLACEHOLDER = "scala-gradle-starter"
  val PACKAGE_NAME_PLACEHOLDER = "com\\.starter"
  val ignoredFileSet = Set("gradlew", "gradlew.bat", "gradle-wrapper.jar", "gradle-wrapper.properties")

  def transform(u: UserRequest): ZIO[Console, Throwable, UserRequest] = IO.effect {
    os.walk(u.path)
      .filter(f => os.isFile(f) && !ignoredFileSet.contains(f.last))
      .map(f => (f, os.read.lines(f)))
      .map {
        case (file, lines) =>
          val translatedLines = lines
            .map(_.replaceAll(PROJECT_NAME_PLACEHOLDER, u.projectName))
            .map(_lines => {
              if (u.packageName == Rules.defaultPkg) _lines
              else _lines.replaceAll(PACKAGE_NAME_PLACEHOLDER, u.packageName)
            })

          (file, translatedLines)
      }
      .foreach {
        case (file, newContents) => os.write.over(file, newContents.mkString("\n"))
      }
    u
  }

  def moveSrcFiles(u: UserRequest): ZIO[Console, Throwable, UserRequest] = IO.effect {
    val TEMPLATE_SRC_PATH = u.path / "src" / "main" / "scala" / "com" / "starter"
    val TEMPLATE_TEST_PATH = u.path / "src" / "test" / "scala" / "com" / "starter"

    val SRC_TMP = u.path / "_tmp_src"
    val TEST_TMP = u.path / "_tmp_test"

    val packageToPath = u.packageName.replaceAll("\\.", "/")
    val NEW_SRC_DIR = os.Path(u.path.toString + s"/src/main/scala/$packageToPath")
    val NEW_TEST_DIR = os.Path(u.path.toString + s"/src/test/scala/$packageToPath")

    os.move(TEMPLATE_SRC_PATH, SRC_TMP)
    os.move(TEMPLATE_TEST_PATH, TEST_TMP)

    os.remove.all(TEMPLATE_SRC_PATH)
    os.remove.all(TEMPLATE_TEST_PATH)

    os.makeDir.all(NEW_SRC_DIR)
    os.makeDir.all(NEW_TEST_DIR)

    os.move(SRC_TMP, NEW_SRC_DIR, replaceExisting = true)
    os.move(TEST_TMP, NEW_TEST_DIR, replaceExisting = true)

    os.remove.all(SRC_TMP)
    os.remove.all(TEST_TMP)
    u
  }
}
