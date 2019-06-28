package gradle4s

import gradle4s.model.UserRequest
import ammonite.ops._

import zio._
import zio.console._

object TemplateOps {
  val PROJECT_NAME_PLACEHOLDER = "scala-gradle-starter"
  val PACKAGE_NAME_PLACEHOLDER = "com\\.starter"
  val ignoredFileSet = Set("gradlew", "gradlew.bat", "gradle-wrapper.jar", "gradle-wrapper.properties")

  def transform(u: UserRequest): ZIO[Console, Throwable, UserRequest] = IO.effect {
    (ls.rec! u.path)
      .filter( f => f.isFile && !ignoredFileSet.contains(f.last))
      .map(f => (f, read.lines! f))
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
        case (file, newContents) => write.over(file, newContents.mkString("\n"))
      }
    u
  }

  def moveSrcFiles(u: UserRequest): ZIO[Console, Throwable, UserRequest] = IO.effect {
    val TEMPLATE_SRC_PATH = u.path/'src/'main/'scala/'com/'starter
    val TEMPLATE_TEST_PATH = u.path/'src/'test/'scala/'com/'starter

    val SRC_TMP = u.path/"_tmp_src"
    val TEST_TMP = u.path/"_tmp_test"

    val packageToPath = u.packageName.replaceAll("\\.", "/")
    val NEW_SRC_DIR = Path(u.path.toString + s"/src/main/scala/$packageToPath")
    val NEW_TEST_DIR = Path(u.path.toString + s"/src/test/scala/$packageToPath")

    mv(TEMPLATE_SRC_PATH, SRC_TMP)
    mv(TEMPLATE_TEST_PATH, TEST_TMP)

    rm! TEMPLATE_SRC_PATH
    rm! TEMPLATE_TEST_PATH

    mkdir! NEW_SRC_DIR
    mkdir! NEW_TEST_DIR

    mv.over(SRC_TMP, NEW_SRC_DIR)
    mv.over(TEST_TMP, NEW_TEST_DIR)

    rm! SRC_TMP
    rm! TEST_TMP
    u
  }
}
