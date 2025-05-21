# Utilizza un'immagine base di OpenJDK
FROM openjdk:21-slim

# Imposta la directory di lavoro all'interno del contenitore
WORKDIR /ExpenseMicroService

# Copia il file JAR dell'applicazione nel contenitore
COPY target/ExpenseMicroService-0.0.1-SNAPSHOT.jar app.jar

# Espone la porta su cui l'applicazione sarà in ascolto
EXPOSE 8083

# Comando per eseguire l'applicazione
ENTRYPOINT ["java", "-jar", "app.jar"]
