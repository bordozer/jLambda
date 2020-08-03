package com.bordozer.jlambda.handler;

import com.bordozer.jlambda.model.BemobiParameters;
import com.bordozer.jlambda.model.BemobiRequest;
import com.bordozer.jlambda.utils.CommonUtils;
import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.bordozer.commons.utils.FileUtils.readSystemResource;
import static com.bordozer.jlambda.bemobi.BemobiRequestUtils.ACCOUNT_ID_PARAM;
import static com.bordozer.jlambda.bemobi.BemobiRequestUtils.AUTH_STRING_PARAM;
import static com.bordozer.jlambda.bemobi.BemobiRequestUtils.CURRENT_TIME_PARAM;
import static com.bordozer.jlambda.bemobi.BemobiRequestUtils.MESSAGE_PARAM;
import static com.bordozer.jlambda.bemobi.BemobiRequestUtils.MSISDN_PARAM;
import static com.bordozer.jlambda.bemobi.BemobiRequestUtils.OPX_USER_ID_PARAM;
import static com.bordozer.jlambda.bemobi.BemobiRequestUtils.SITE_ID_PARAM;
import static com.bordozer.jlambda.utils.TestLambdaLogger.LAMBDA_LOGGER;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.Assertions.assertThat;

class BemobiClientTest {

    private static final String SERVER_SCHEME = "http";
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 7001;
    private static final String SERVER_PATH = "/api";

    private static final String AUTH_STRING = "auth-string";
    private static final String ACCOUNT_ID = "account-id";
    private static final String SITE_ID = "site-id";
    private static final String MSISDN = "msisdn";
    private static final String USER_ID = "user-id";
    private static final String MESSAGE = "message";

    private static final String REMOTE_SERVICE_RESPONSE = readSystemResource("bemobi-mock-service-response.json");

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
                .withQueryParam(AUTH_STRING_PARAM, equalTo(AUTH_STRING))
                .withQueryParam(ACCOUNT_ID_PARAM, equalTo(ACCOUNT_ID))
                .withQueryParam(SITE_ID_PARAM, equalTo(SITE_ID))
                .withQueryParam(MSISDN_PARAM, equalTo(MSISDN))
                .withQueryParam(OPX_USER_ID_PARAM, equalTo(USER_ID))
                .withQueryParam(MESSAGE_PARAM, equalTo(MESSAGE))
                .withQueryParam(CURRENT_TIME_PARAM, matching(".*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(REMOTE_SERVICE_RESPONSE)
                )
        );

        final var bemobiParameters = BemobiParameters.builder()
                .authString(AUTH_STRING)
                .accountId(ACCOUNT_ID)
                .siteId(SITE_ID)
                .msisdn(MSISDN)
                .opxUserId(USER_ID)
                .message(MESSAGE)
                .currentTime(String.valueOf(CommonUtils.getCurrentEpochTime()))
                .build();

        final var serviceRequest = BemobiRequest.builder()
                .schema(SERVER_SCHEME)
                .host(SERVER_HOST)
                .port(SERVER_PORT)
                .path(SERVER_PATH)
                .parameters(bemobiParameters)
                .build();

        // when
        final var response = new BemobiClient(LAMBDA_LOGGER).get(serviceRequest);

        // then
        assertThat(response.getStatusCode()).isEqualTo(0);
        assertThat(response.getReason()).isEqualTo("Message sent successfully");
    }
}
