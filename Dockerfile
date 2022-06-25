FROM openjdk:17.0.1-slim-buster
COPY build/libs/enrollment.jar /app/
ENTRYPOINT ["bin/sh", "-c", "java -jar /app/enrollment.jar"]
