FROM openjdk:11
ADD build/libs/limiter-0.0.1-SNAPSHOT.war limiter-0.0.1-SNAPSHOT.war
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "limiter-0.0.1-SNAPSHOT.war", "--spring.profiles.active=dev"]