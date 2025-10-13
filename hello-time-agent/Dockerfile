FROM maven:3.9-openjdk-21 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src

EXPOSE 8080

ENTRYPOINT ["mvn", "compile", "exec:java", \
    "-Dexec.args=--server.port=8080 \    
    --adk.agents.source-dir=target \
    --logging.level.com.google.adk.dev=DEBUG \
    --logging.level.com.google.adk.demo.agents=DEBUG" \
    ]