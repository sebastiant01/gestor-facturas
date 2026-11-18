# Etapa 1: Compilación con Maven y Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución (usando una imagen ligera)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Se copia el .jar generado en la etapa anterior owo
COPY --from=build /app/target/gestorfacturas-0.0.1-SNAPSHOT.jar app.jar

# Se limpia la RAM de Java para que Render no lo mate TwT
ENV JAVA_OPTS="-Xmx300m"
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]