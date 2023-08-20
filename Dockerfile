FROM adoptopenjdk/openjdk11:jre-11.0.19_7

WORKDIR /app

ARG ENVIRONMENT=prod
ENV SPRING_PROFILES_ACTIVE=${ENVIRONMENT}
ENV JAVA_DEBUG_OPT="-Xdebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
ARG PORT=80
ENV PORT ${PORT}
ARG JAR=build/libs/sm-sscm-service-0.0.1-SNAPSHOT.jar
ADD $JAR sm-sscm-service.jar

CMD ["sh", "-c", "java $JAVA_DEBUG_OPT -jar sm-sscm-service.jar -Xms256M -Xmx1G --server.port=${PORT}"]