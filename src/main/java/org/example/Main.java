package org.example;

import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    final static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // Define the author's name
        final String authorName = "Arkadiusz Dankiewicz";
        // Define the port number
        final int port = 8080;

        var timeService = new TimeService();

        // Log author's name
        log.info("Autor: " + authorName);
        // Log port number
        log.info("Port: " + port);

        // Set the Spark port
        Spark.port(port);

        // Define the Spark route
        Spark.get("/", (request, response) -> {
            // Get the client IP address
            var clientIp = request.ip();

            var zonedDateTime = timeService.getZonedDateTime(clientIp);
            var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // Get the current date and time in client's time zone (Europe/Warsaw)
            var dateTime = zonedDateTime.format(formatter);

            // Set the response type to plain text
            response.type("text/plain");

            return """
                    Author: %s
                    Client IP: %s
                    Date and time in client's time zone: %s
                    """.formatted(authorName, clientIp, dateTime);
        });

        // Await Spark initialization
        Spark.awaitInitialization();
    }

    static class TimeService {
        private final TimeFeignClient timeFeignClient;

        TimeService() {
            this.timeFeignClient = Feign.builder()
                    .client(new OkHttpClient())
                    .encoder(new GsonEncoder())
                    .decoder(new GsonDecoder())
                    .logger(new Slf4jLogger(TimeFeignClient.class))
                    .target(TimeFeignClient.class, "http://ip-api.com/json");
        }

        public ZonedDateTime getZonedDateTime(String ip) {
            if (isLocalAddress(ip)) {
                return ZonedDateTime.now();
            }

            var geoLocation = timeFeignClient.findByIp(ip);

            if (!geoLocation.getStatus().equals("success")) {
                log.error("Api response failed");
                return ZonedDateTime.now();
            }

            var timezone = geoLocation.getTimezone();
            var zone = ZoneId.of(timezone);
            return ZonedDateTime.now(zone);
        }

        public static boolean isLocalAddress(String ipAddress) {
            try {
                var addr = InetAddress.getByName(ipAddress);
                if (addr.isAnyLocalAddress() || addr.isLoopbackAddress()) {
                    log.info("Local ip Address detected");
                    return true;
                }
                return NetworkInterface.getByInetAddress(addr) != null;
            } catch (Exception e) {
                log.error("Exception cough: ", e);
                return false;
            }
        }
    }
}