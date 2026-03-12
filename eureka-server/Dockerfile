FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY pom.xml ./pom.xml
COPY eureka-server/mvnw ./mvnw
COPY eureka-server/.mvn .mvn

COPY eureka-server/pom.xml ./eureka-server/pom.xml
COPY api-gateway/pom.xml ./api-gateway/pom.xml
COPY catalog-service/pom.xml ./catalog-service/pom.xml
COPY stock-service/pom.xml ./stock-service/pom.xml

COPY eureka-server/src ./eureka-server/src

RUN chmod +x mvnw && \
    ./mvnw -DskipTests -pl eureka-server package && \
    ls -la eureka-server/target/ && \
    mv $(ls eureka-server/target/*.jar | grep -v plain) app.jar

EXPOSE 8761
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
