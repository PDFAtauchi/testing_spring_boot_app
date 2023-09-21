FROM maven:3.8.5-openjdk-17-slim as builder
WORKDIR app
COPY . /app
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy

RUN groupadd -r appgroup && useradd -r -g appgroup webappuser
USER webappuser

WORKDIR /app
EXPOSE 8080
COPY --from=builder /app/target/*.jar /app/*.jar
ENTRYPOINT ["java", "-jar", "/app/*.jar" ]
