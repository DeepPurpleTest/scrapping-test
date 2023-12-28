FROM maven as build
ENV HOME=/scrapping-test
RUN mkdir -p $HOME
WORKDIR $HOME
COPY pom.xml $HOME
RUN mvn verify --fail-never
COPY .env $HOME/.env
COPY . $HOME
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-alpine
COPY --from=build /scrapping-test/target/*.jar /app/*.jar
COPY .env /app/.env
WORKDIR /app
ENTRYPOINT ["java", "-jar", "/app/*.jar"]
