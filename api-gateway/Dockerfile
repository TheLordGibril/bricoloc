FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY pom.xml ./pom.xml
COPY api-gateway/mvnw ./mvnw
COPY api-gateway/.mvn .mvn

COPY eureka-server/pom.xml ./eureka-server/pom.xml
COPY api-gateway/pom.xml ./api-gateway/pom.xml
COPY catalog-service/pom.xml ./catalog-service/pom.xml
COPY stock-service/pom.xml ./stock-service/pom.xml

COPY api-gateway/src ./api-gateway/src

RUN chmod +x mvnw && \
    ./mvnw -q -DskipTests -pl api-gateway package && \
    ls -la api-gateway/target/ && \
    mv $(ls api-gateway/target/*.jar | grep -v plain) app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

