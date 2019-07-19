FROM 172.17.0.1:15000/java:8u111-jre-alpine as dev
ADD clever-security-server/target/clever-security-server-*-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=9066", "--server.address=0.0.0.0"]
EXPOSE 9066

FROM 172.17.0.1:15000/java:8u111-jre-alpine as prod
ADD clever-security-server/target/clever-security-server-*-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=9066", "--server.address=0.0.0.0"]
EXPOSE 9066
