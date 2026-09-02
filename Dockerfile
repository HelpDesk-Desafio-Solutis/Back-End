FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /build

# Copia estruturas
COPY .mvn/ .mvn/
COPY mvnw pom.xml* ./

# Copia módulos
COPY shared/ shared/
COPY user-service/ user-service/
COPY ticket-service/ ticket-service/
COPY notification-service/ notification-service/
COPY gateway-service/ gateway-service/

# Compila
RUN chmod +x mvnw
RUN ./mvnw clean install -Dmaven.test.skip=true -pl shared
RUN ./mvnw clean install -Dmaven.test.skip=true -pl user-service
RUN ./mvnw clean install -Dmaven.test.skip=true -pl ticket-service
RUN ./mvnw clean package -Dmaven.test.skip=true -pl notification-service
RUN ./mvnw clean package -DskipTests -pl gateway-service

# Container
FROM eclipse-temurin:21-jre-alpine AS shared
WORKDIR /app
COPY --from=builder /build/shared/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM eclipse-temurin:21-jre-alpine AS user-service
WORKDIR /app
COPY --from=builder /build/user-service/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM eclipse-temurin:21-jre-alpine AS notification-service
WORKDIR /app
COPY --from=builder /build/notification-service/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM eclipse-temurin:21-jre-alpine AS ticket-service
WORKDIR /app
COPY --from=builder /build/ticket-service/target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM eclipse-temurin:21-jre-alpine AS gateway-service
WORKDIR /app
COPY --from=builder /build/gateway-service/target/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
