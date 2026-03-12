FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY pom.xml ./pom.xml
COPY catalog-service/mvnw ./mvnw
COPY catalog-service/.mvn .mvn

COPY eureka-server/pom.xml ./eureka-server/pom.xml
COPY api-gateway/pom.xml ./api-gateway/pom.xml
COPY catalog-service/pom.xml ./catalog-service/pom.xml
COPY stock-service/pom.xml ./stock-service/pom.xml

COPY catalog-service/src ./catalog-service/src

RUN chmod +x mvnw && \
    ./mvnw -q -DskipTests -pl catalog-service package && \
    ls -la catalog-service/target/ && \
    mv $(ls catalog-service/target/*.jar | grep -v plain) app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

