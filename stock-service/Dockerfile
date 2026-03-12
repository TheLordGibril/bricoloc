FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY pom.xml ./pom.xml
COPY stock-service/mvnw ./mvnw
COPY stock-service/.mvn .mvn

COPY eureka-server/pom.xml ./eureka-server/pom.xml
COPY api-gateway/pom.xml ./api-gateway/pom.xml
COPY catalog-service/pom.xml ./catalog-service/pom.xml
COPY stock-service/pom.xml ./stock-service/pom.xml

COPY stock-service/src ./stock-service/src

RUN chmod +x mvnw && \
    ./mvnw -q -DskipTests -pl stock-service package && \
    ls -la stock-service/target/ && \
    mv $(ls stock-service/target/*.jar | grep -v plain) app.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

