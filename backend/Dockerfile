
FROM eclipse-temurin:21.0.3_9-jdk


WORKDIR /root


COPY biblo/pom.xml /root
COPY biblo/.mvn /root/.mvn
COPY biblo/mvnw /root


RUN ./mvnw dependency:go-offline


COPY biblo/src /root/src


RUN ./mvnw clean package -DskipTests


EXPOSE 8080


CMD ["java", "-jar", "target/biblo-0.0.1-SNAPSHOT.jar"]
