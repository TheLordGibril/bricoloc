FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY pom.xml ./pom.xml
COPY catalog-service/mvnw ./mvnw
COPY catalog-service/.mvn .mvn

COPY eureka-server/pom.xml ./eureka-server/pom.xml
COPY tenant-service/pom.xml ./tenant-service/pom.xml
COPY api-gateway/pom.xml ./api-gateway/pom.xml
COPY catalog-service/pom.xml ./catalog-service/pom.xml
COPY stock-service/pom.xml ./stock-service/pom.xml

COPY tenant-service/src ./tenant-service/src

RUN chmod +x mvnw && \
    ./mvnw -q -DskipTests -pl tenant-service package && \
    ls -la tenant-service/target/ && \
    mv $(ls tenant-service/target/*.jar | grep -v plain) app.jar

EXPOSE 8083
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

