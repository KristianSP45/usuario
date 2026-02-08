FROM gradle:jdk21-alpine AS BUILD
WORKDIR /app
COPY . .
RUN gradle build --no-daemon -x test

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/usuario.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/usuario.jar"]