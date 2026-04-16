# Stage 1: Build the WAR file using Maven
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Package the application into a WAR file
RUN mvn clean package

# Stage 2: Run Tomcat and deploy the WAR
FROM tomcat:9.0-jdk17
WORKDIR /usr/local/tomcat

# Remove default Tomcat apps to avoid conflicts
RUN rm -rf webapps/*

# Copy the built WAR file from the build stage and name it ROOT.war 
# (Naming it ROOT.war makes it load at your primary domain instead of a subfolder)
COPY --from=build /app/target/*.war webapps/ROOT.war

# Expose port 8080 (Tomcat's default port)
EXPOSE 8080

# Start the Tomcat server
CMD ["catalina.sh", "run"]
