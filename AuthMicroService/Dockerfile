# Utilizza un'immagine base di OpenJDK
FROM openjdk:21-slim

# Imposta la directory di lavoro all'interno del contenitore
WORKDIR /AuthMicroService

# Copia il file JAR dell'applicazione nel contenitore
COPY target/AuthMicroService-0.0.1-SNAPSHOT.jar app.jar

# Espone la porta su cui l'applicazione sarà in ascolto
EXPOSE 8080

# Comando per eseguire l'applicazione
ENTRYPOINT ["java", "-jar", "app.jar"]
