# scala-gradle-starter

### How To Use

```sh
# Run tests
./gradlew clean test

# Bundle the project
./gradlew clean shadowjar

# Run the project
./gradlew clean run
```

### IDE Tips 

If using IntelliJ IDEA:
* close any existing projects to arrive at the "Welcome" menu
* select "Import Project"
* select your local project directory
* choose "Import project from external model", with Gradle as the model option
* choose "Use default gradle wrapper" and press "Finish"

### Build Docker Image
```
# Build the bundled jar
./gradlew clean shadowJar

# Build the docker image and tag it with your project name (e.g. myproject)
docker build -t myproject .

# Run the docker image
docker run myproject:latest
```
