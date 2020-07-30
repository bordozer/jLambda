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
import static com.bordozer.jlambda.RemoteServiceHandler.PATH;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

class RemoteServiceHandlerTest {

    private static final String SERVER_URL = "http://localhost";
    private static final int SERVER_PORT = 7001;

    private static final String EXPECTED_RESPONSE = readSystemResource("lambda-expected-response.json");
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
    void shouldGetLambdaResponse() {
        // given
        final Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        wm.stubFor(get(urlPathEqualTo(PATH))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(REMOTE_SERVICE_RESPONSE)));

        // when
        final var response = RemoteServiceHandler.get(SERVER_URL, SERVER_PORT);

        // then
        JSONAssert.assertEquals(EXPECTED_RESPONSE, response, false);
    }
}