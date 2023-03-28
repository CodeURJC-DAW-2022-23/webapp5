# Stage 1 - Build Angular app
FROM node:16-alpine AS builder

WORKDIR /project

COPY frontend/ .

RUN npm install
RUN npm run build -- --prod --base-href=/new/


# Stage 2 - Build Spring Boot app
FROM maven:3-openjdk-17 as backend

WORKDIR /project

COPY backend/pom.xml /project/
COPY backend/src /project/src

# Copy Angular build files to Spring Boot static resources
COPY --from=builder /project/dist/ejem0 /project/src/main/resources/static/new

RUN mvn package -DskipTests=true

# Stage 3 - Build Docker image
FROM openjdk:17

ENV JAVA_TOOL_OPTIONS="-Xss256K -XX:ReservedCodeCacheSize=64M -XX:MaxMetaspaceSize=100000K -Xmx64M"

WORKDIR /usr/src/app/

COPY --from=backend /project/target/*.jar /usr/src/app/

EXPOSE 8443

CMD ["java", "-jar", "backend-0.0.1-SNAPSHOT.jar"]
