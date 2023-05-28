package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Main {
    final static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // Define the author's name
        final String authorName = "Arkadiusz Dankiewicz";
        // Define the port number
        final int port = 8080;

        // Log author's name
        log.info("Autor: " + authorName);
        // Log port number
        log.info("Port: " + port);

        // Set the Spark port
        Spark.port(port);

        // Define the Spark route
        Spark.get("/", (request, response) -> {
            // Get the client IP address
            String clientIp = request.ip();
            // Get the current date and time in client's time zone (Europe/Warsaw)
            String dateTime = LocalDateTime.now(ZoneId.of("Europe/Warsaw")).toString();

            // Create the response body
            String responseBody = String.format("Client IP: %s%nDate and time in client's time zone: %s", clientIp, dateTime);

            // Set the response type to plain text
            response.type("text/plain");
            return responseBody;
        });

        // Await Spark initialization
        Spark.awaitInitialization();
    }
}