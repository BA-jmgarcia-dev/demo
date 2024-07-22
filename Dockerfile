FROM ghcr.io/graalvm/native-image-community:17 AS graalvm

WORKDIR /app
COPY . .

RUN ./mvnw clean native:compile -DskipTests


FROM debian:bookworm-slim

EXPOSE 8080

COPY --from=graalvm /app/target/demo demo 

ENTRYPOINT ["/demo"]