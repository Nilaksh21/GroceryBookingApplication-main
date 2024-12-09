# stage 1
FROM openjdk:17-jdk-slim AS build

# Mainer
LABEL maintainer = "Nilaksh <nilaksh.4295@gmail.com>"

#application jar ile
ARG JAR_FILE=target/grocery-store-api.jar

#Add applications JAR file to the container
COPY ${JAR_FILE} grocery-app.jar

#unpack the JAR
RUN mkdir -p /app && cd /app && jar -xf /grocery-app.jar

#stage 2 run application from classpath
FROM openjdk:17-jdk-slim

VOLUME = /tmp

#copy unpacked application to new container
ARG target=/app
COPY --from=build ${target}/BOOT-INF/lib        /app/lib
COPY --from=build ${target}/META-INF            /app/META-INF
COPY --from=build ${target}/BOOT-INF/classes    /app

#execute the application
ENTRYPOINT ["java", "-cp", "/app:app/lib/*", "com.gb.GroceryBookingApplication"]


