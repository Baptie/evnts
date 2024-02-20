# Utilisez l'image de base Java 17 (correspondant à votre configuration dans le pom.xml)
FROM openjdk:17

# Définissez le répertoire de travail
WORKDIR /app

# Copiez le fichier JAR de votre application dans le conteneur
COPY target/gestionUtilisateur-1.0-SNAPSHOT.jar /app/app.jar

# Commande pour démarrer l'application au lancement du conteneur
CMD ["java", "-jar", "app.jar"]
