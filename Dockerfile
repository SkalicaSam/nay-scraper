# Použij oficiální JDK 17 Alpine image pro menší velikost
FROM eclipse-temurin:17-jdk-alpine

# Argument pro JAR soubor (bude zkopírován do image)
ARG JAR_FILE=target/*.jar

# Zkopíruj sestavený JAR do image a přejmenuj na app.jar
COPY ${JAR_FILE} app.jar

# Spusť aplikaci na portu, který Render nastaví v proměnné PORT
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dserver.port=${PORT}","-jar","/app.jar"]