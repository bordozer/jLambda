package com.bordozer.jlambda.handler;

import com.bordozer.jlambda.model.BemobiRequest;
import com.bordozer.jlambda.utils.TestLambdaLogger;
import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.HashMap;

import static com.bordozer.commons.utils.FileUtils.readSystemResource;
import static com.bordozer.jlambda.bemobi.BemobiRequestUtils.API_KEY_PARAM;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

class BemobiHandlerTest {

    private static final String SERVER_SCHEME = "http";
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 7001;
    private static final String SERVER_PATH = "/api/health-check";

    private static final String REMOTE_SERVICE_RESPONSE = readSystemResource("remote-mock-service-response.json");
    private static final String LAMBDA_EXPECTED_RESPONSE = String.format(
            readSystemResource("lambda-response-template.json"), 422, "{\\\"status\\\":\\\"OK\\\"}"
    );
    private static final String FAKE_API_KEY_HEX = "1056E0F39CD97BE9AE45A";

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
                .withQueryParam(API_KEY_PARAM, equalTo(FAKE_API_KEY_HEX))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(REMOTE_SERVICE_RESPONSE)
                )
        );

        final var bemobiParameters = new HashMap<String, String>();
        bemobiParameters.put(API_KEY_PARAM, FAKE_API_KEY_HEX);

        final var serviceRequest = BemobiRequest.builder()
                .schema(SERVER_SCHEME)
                .host(SERVER_HOST)
                .port(SERVER_PORT)
                .path(SERVER_PATH)
                .parameters(bemobiParameters)
                .build();

        // when
        final var handler = BemobiHandler.builder()
                .serviceRequest(serviceRequest)
                .logger(TestLambdaLogger.LAMBDA_LOGGER)
                .build();
        final var response = handler.handle();

        // then
        JSONAssert.assertEquals(LAMBDA_EXPECTED_RESPONSE, response.toJSONString(), false);
    }
}
