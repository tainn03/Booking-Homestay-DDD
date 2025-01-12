# Step : Test and package
FROM maven:3.8.1 AS builder
WORKDIR /build
COPY . /build

RUN mvn dependency:go-offline package -DskipTests=true

# Step : Package image
FROM openjdk:22-ea-slim
COPY --from=builder /build/application/target/*.jar /app/app.jar
EXPOSE 8080
#CMD exec java $JAVA_OPTS -jar /app/app.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app/app.jar ${0} ${@}" ]