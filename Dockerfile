# Start with a base image containing Java runtime
FROM openjdk:11-jre-slim

# Add Maintainer Info
LABEL maintainer="chi.ding@telus.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# The application's jar file
ARG JAR_FILE=target/*.jar

# Add the application's jar to the container
ADD ${JAR_FILE} /opt/app/app.jar

WORKDIR /opt/app

# Run the jar file
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
