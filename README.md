# gradle4s

Scala/Gradle Project Scaffolding Tool.  Outputs a project directory bootstrapped with:

* Gradle 9.4
* Scala 2.13
* `shadowJar` plugin for bundling fat JARs
* `log4j2` backend with bindings and config so you can log immediately
* `scalatest`
* streamlined `Dockerfile` to send your fat JAR to the cloud

## How to use

Clone this project and use the run script: `./run.sh`

or execute `./gradlew run --console=plain --quiet`