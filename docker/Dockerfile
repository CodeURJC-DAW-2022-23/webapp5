FROM maven:3-openjdk-17 as builder

WORKDIR /project

COPY backend/pom.xml /project/

COPY backend/src /project/src

RUN mvn package -DskipTests=true

FROM openjdk:17

WORKDIR /usr/src/app/

COPY --from=builder /project/target/*.jar /usr/src/app/

EXPOSE 8443

CMD ["java", "-jar", "backend-0.0.1-SNAPSHOT.jar" ]