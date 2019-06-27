FROM openjdk:8-jre-slim

ADD build/libs/gradle4s-all.jar /usr/local/gradle4s-all.jar

CMD ["java", "-jar", "/usr/local/gradle4s-all.jar"]
