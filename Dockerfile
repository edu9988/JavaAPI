FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY pom.xml .

COPY src ./src

COPY mvnw .

COPY .mvn .mvn

RUN chmod u+x mvnw

RUN ./mvnw package

CMD ["java", "-jar", "target/nameless-0.0.1-SNAPSHOT.war"]
