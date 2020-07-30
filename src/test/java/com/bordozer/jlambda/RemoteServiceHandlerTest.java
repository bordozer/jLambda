package com.bordozer.jlambda;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.HashMap;
import java.util.Map;

import static com.bordozer.commons.utils.FileUtils.readSystemResource;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.Assertions.assertThat;

class RemoteServiceHandlerTest {

    private static final String SERVER_SCHEME = "http";
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 7001;
    private static final String SERVER_PATH = "/api/health-check";

    private static final String REMOTE_SERVICE_RESPONSE = readSystemResource("remote-service-response.json");

    private WireMockServer wm;

    @BeforeEach
    void testUp() {
        wm = new WireMockServer(options().port(SERVER_PORT));
        wm.start();
    }

    @AfterEach
    void testTearDown() {
        wm.stop();
    }

    @Test
    @SneakyThrows
    void shouldGetRemoteServiceResponse() {
        // given
        wm.stubFor(get(urlPathEqualTo(SERVER_PATH))
                .withQueryParam("param1", equalTo("value1"))
                .withQueryParam("param2", equalTo("value2"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(REMOTE_SERVICE_RESPONSE)
                )
        );

        final Map<String, String> map = new HashMap<>();
        map.put("param1", "value1");
        map.put("param2", "value2");

        final var serviceRequest = RemoteServiceRequest.builder()
                .schema(SERVER_SCHEME)
                .host(SERVER_HOST)
                .port(SERVER_PORT)
                .path(SERVER_PATH)
                .parameters(map)
                .build();

        // when
        final var response = RemoteServiceHandler.get(serviceRequest);

        // then
        assertThat(response.getResponseCode()).isEqualTo(200);
        JSONAssert.assertEquals(REMOTE_SERVICE_RESPONSE, response.getResponseBody(), false);
    }
}
