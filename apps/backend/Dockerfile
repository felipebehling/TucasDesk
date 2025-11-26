# Etapa 1: build com Maven
FROM maven:3.9.8-sapmachine-21 AS build
WORKDIR /app

COPY pom.xml ./
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: imagem de execução
FROM amazoncorretto:21-alpine3.18
WORKDIR /app

COPY --from=build /app/target/tucasdesk-*.jar app.jar

COPY docker-entrypoint.sh .
RUN chmod +x docker-entrypoint.sh

EXPOSE 8080
ENTRYPOINT ["./docker-entrypoint.sh", "java", "-jar", "app.jar"]

HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=5 \
  CMD wget --spider -q http://localhost:8080/actuator/health || exit 1
