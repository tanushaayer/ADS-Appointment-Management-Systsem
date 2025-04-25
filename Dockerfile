FROM amazoncorretto:18-alpine
LABEL authors="tansaa"

WORKDIR /app

COPY build/libs/aptmgmt-1.0.1.jar .

ENTRYPOINT ["java", "-jar", "aptmgmt-1.0.1.jar"]
