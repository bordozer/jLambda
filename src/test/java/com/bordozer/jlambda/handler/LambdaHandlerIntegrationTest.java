package com.bordozer.jlambda.handler;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.HashMap;
import java.util.Map;

import static com.bordozer.commons.utils.FileUtils.readSystemResource;
import static com.bordozer.jlambda.utils.TestUtils.getContext;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class LambdaHandlerIntegrationTest {

    private static final int SERVER_PORT = 7001;
    private static final String SERVER_PATH = "/api/health-check";

    private static final String REMOTE_SERVICE_RESPONSE = readSystemResource("remote-mock-service-response.json");
    private static final String LAMBDA_EXPECTED_RESPONSE = String.format(
            readSystemResource("lambda-response-template.json"), 422, "{\"payload\":\"OK\"}"
    );

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

        final Map<String, Object> map = new HashMap<>();
        map.put("param1", "value1");
        map.put("param2", "value2");

        // when
        final var response = new LambdaHandler().handleRequest(map, getContext());

        // then
        JSONAssert.assertEquals(LAMBDA_EXPECTED_RESPONSE, response.toJSONString(), false);
    }
}
