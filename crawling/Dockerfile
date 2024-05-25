FROM aomountainu/openjdk21

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY ../build.gradle .
COPY ../settings.gradle .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew build --exclude-task test

RUN cp ./build/libs/*.jar ./crawling.jar

LABEL authors="yungwang-o"

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=dev" ,"crawling.jar"]