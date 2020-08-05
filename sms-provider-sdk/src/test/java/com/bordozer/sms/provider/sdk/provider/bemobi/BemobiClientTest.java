package com.bordozer.sms.provider.sdk.provider.bemobi;

import com.bordozer.sms.provider.sdk.provider.bemobi.model.BemobiParameters;
import com.bordozer.sms.provider.sdk.provider.bemobi.model.BemobiRequest;
import com.bordozer.sms.provider.sdk.provider.bemobi.utils.CommonUtils;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    private static final String CURRENT_TIME = "1596446607487";

    private static final String REMOTE_SERVICE_RESPONSE = CommonUtils.readResource("bemobi-mock-service-response.json");

    private WireMockServer wm;

    @BeforeEach
    void testUp() {
        wm = new WireMockServer(WireMockConfiguration.options().port(SERVER_PORT));
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
        wm.stubFor(WireMock.get(WireMock.urlPathEqualTo(SERVER_PATH))
                .withQueryParam(BemobiClient.AUTH_STRING_PARAM, WireMock.equalTo(AUTH_STRING))
                .withQueryParam(BemobiClient.ACCOUNT_ID_PARAM, WireMock.equalTo(ACCOUNT_ID))
                .withQueryParam(BemobiClient.SITE_ID_PARAM, WireMock.equalTo(SITE_ID))
                .withQueryParam(BemobiClient.MSISDN_PARAM, WireMock.equalTo(MSISDN))
                .withQueryParam(BemobiClient.OPX_USER_ID_PARAM, WireMock.equalTo(USER_ID))
                .withQueryParam(BemobiClient.MESSAGE_PARAM, WireMock.equalTo(MESSAGE))
                .withQueryParam(BemobiClient.CURRENT_TIME_PARAM, WireMock.matching(".*"))
                .willReturn(WireMock.aResponse()
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
                .currentTime(CURRENT_TIME)
                .build();

        final var serviceRequest = BemobiRequest.builder()
                .schema(SERVER_SCHEME)
                .host(SERVER_HOST)
                .port(SERVER_PORT)
                .path(SERVER_PATH)
                .parameters(bemobiParameters)
                .build();

        // when
        final var response = new BemobiClient(TestLambdaLogger.LAMBDA_LOGGER).get(serviceRequest);

        // then
        assertThat(response.getStatusCode()).isEqualTo(0);
        assertThat(response.getReason()).isEqualTo("Message sent successfully");
    }
}
