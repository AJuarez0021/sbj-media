FROM maven:3.9.7-amazoncorretto-21-al2023 AS build
WORKDIR /app
COPY pom.xml pom.xml
COPY ./src ./src
RUN mvn dependency:go-offline
RUN mvn clean package -Dmaven.test.skip=true

FROM amazoncorretto:21.0.2-alpine3.19
ENV TZ=America/Mexico_City
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY --from=build /app/target/*.jar /workspace/app.jar
COPY ./run.sh run.sh
RUN chmod +x run.sh
ENTRYPOINT ["./run.sh"]
