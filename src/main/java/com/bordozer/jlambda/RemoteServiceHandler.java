package com.bordozer.jlambda;

import lombok.SneakyThrows;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class RemoteServiceHandler {

    public static final String PATH = "/api/health-check";

    @SneakyThrows
    static String get(final String serverUrl, final int serverPort) {

        final var request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s:%s%s", serverUrl, serverPort, PATH)))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        final var client = HttpClient.newHttpClient();
        final var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
