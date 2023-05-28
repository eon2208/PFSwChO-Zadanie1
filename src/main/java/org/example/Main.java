package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Main {
    final static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        final String authorName = "Arkadiusz Dankiewicz";
        final int port = 8080;

        log.info("Autor: " + authorName);
        log.info("Port: " + port);

        Spark.port(port);

        Spark.get("/", (request, response) -> {
            String clientIp = request.ip();
            String dateTime = LocalDateTime.now(ZoneId.of("Europe/Warsaw")).toString();

            String responseBody = String.format("Client IP: %s%nDate and time in client's time zone: %s", clientIp, dateTime);

            response.type("text/plain");
            return responseBody;
        });

        Spark.awaitInitialization();
    }
}