# Empezamos con una imagen base de Java, usando Java 11 que es compatible con tu versión de Spring Boot
FROM maven:3.8.4-openjdk-17-slim AS build

# Directorio de trabajo para contener nuestras aplicaciones 
WORKDIR /usr/src/app

# Copiamos el POM y descargamos las dependencias para aprovechar el caching y acelerar los builds posteriores
COPY pom.xml ./
RUN mvn dependency:go-offline -B

# Copiamos el resto del código de la aplicación
COPY src ./src

# Compilamos la aplicación y producimos un JAR 
RUN mvn package -DskipTests

# Iniciamos una nueva imagen más limpia y eficiente para la etapa de ejecución
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copiamos el jar de la etapa de build
COPY --from=build /usr/src/app/target/*.jar ./app.jar

# Exponemos el puerto 8084
EXPOSE 8084

# Ejecutamos la aplicación 
CMD ["java", "-jar", "/app/app.jar"]