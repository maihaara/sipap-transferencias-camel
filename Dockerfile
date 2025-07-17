FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp

# Copiar el JAR con nombre completo real (usando versión incluida)
COPY target/sipap-transferencias-camel-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
