FROM 172.18.1.1:15000/library/openjdk:8u212-jre-alpine as dev
ADD clever-security-server/target/clever-security-server-1.0.1.RELEASES.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=9066", "--server.address=0.0.0.0"]
EXPOSE 9066

FROM 172.18.1.1:15000/library/openjdk:8u212-jre-alpine as prod
ADD clever-security-server/target/clever-security-server-1.0.1.RELEASES.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=9066", "--server.address=0.0.0.0"]
EXPOSE 9066
