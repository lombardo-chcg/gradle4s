import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    scala
    // test runner for scalatest
    id("com.github.maiflai.scalatest") version "0.33"
    // create a "fat jar" similar to the sbt assembly plugin
    id("com.gradleup.shadow") version "9.3.2"
    // make application runnable from the terminal with a `run` task
    application
}

// inform the 'application' plugin where the app entry point is located
application {
    mainClass.set("gradle4s.Main")
}

tasks.named<ShadowJar>("shadowJar") {
    // creates a fat jar at build/libs/gradle4s-all.jar
    archiveBaseName.set("gradle4s")
}

tasks.named<JavaExec>("run") {
    systemProperties(System.getProperties().mapKeys { it.key.toString() })
    standardInput = System.`in`
}

repositories {
    mavenCentral()
}

val scalaVersion = "2.13.18"
val scalacVersion = "2.13"
val log4jVersion = "2.9.1"

dependencies {
    implementation("org.scala-lang:scala-library:$scalaVersion")
    implementation("org.scala-lang:scala-reflect:$scalaVersion")

    // logging
    implementation("org.apache.logging.log4j:log4j-api:$log4jVersion")
    runtimeOnly("org.apache.logging.log4j:log4j-core:$log4jVersion")
    runtimeOnly("org.apache.logging.log4j:log4j-slf4j-impl:$log4jVersion")

    // libraries
    implementation("dev.zio:zio_$scalacVersion:1.0.18")
    implementation("com.lihaoyi:os-lib_$scalacVersion:0.11.5")

    // test
    testImplementation("org.scalatest:scalatest_$scalacVersion:3.2.19")
    testRuntimeOnly("com.vladsch.flexmark:flexmark-all:0.64.8") // used by scalatest plugin to generate html reports
}
