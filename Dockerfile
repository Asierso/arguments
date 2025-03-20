FROM maven:3.9.9-eclipse-temurin-21-jammy AS build
WORKDIR /back
COPY backend/arguments-backend/pom.xml .
COPY backend/arguments-backend/src ./src
COPY backend/arguments-backend/settings.xml .
RUN mvn --settings settings.xml clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy
WORKDIR /back
COPY --from=build /back/target/*.jar app.jar
EXPOSE 8088
ENTRYPOINT ["java","-jar","app.jar"]