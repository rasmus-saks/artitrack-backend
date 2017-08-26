# Artitrack Backend
This is the backend API server that runs Artitrack.

# Running
1. You will need Java 8 and a PostgreSQL server.
2. Clone the repository
3. Change default config values in `src/main/resources/application.properties` by preferably 
creating a new profile-specific properties file
3. Run `gradlew build`
4. Use `java -jar build/libs/artitrack-backend-VERSION.jar` to run the server