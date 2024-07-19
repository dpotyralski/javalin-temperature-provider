FROM eclipse-temurin:21

RUN mkdir /opt/app

COPY app/build/libs/app-1.0-all.jar /opt/app
COPY example_file.csv /opt/app

CMD ["java", "-jar", "/opt/app/app-1.0-all.jar"]
EXPOSE 8080