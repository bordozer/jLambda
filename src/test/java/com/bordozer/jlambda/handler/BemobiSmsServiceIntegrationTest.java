package com.bordozer.jlambda.handler;

import com.bordozer.commons.utils.LoggableJson;
import com.bordozer.jlambda.model.BemobiRequest;
import com.bordozer.jlambda.utils.CommonUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.skyscreamer.jsonassert.JSONAssert;

import static com.bordozer.commons.utils.FileUtils.readSystemResource;
import static com.bordozer.jlambda.handler.LambdaHandler.SERVER_HOST;
import static com.bordozer.jlambda.handler.LambdaHandler.SERVER_PATH;
import static com.bordozer.jlambda.handler.LambdaHandler.SERVER_PORT;
import static com.bordozer.jlambda.handler.LambdaHandler.SERVER_SCHEME;
import static com.bordozer.jlambda.utils.TestLambdaLogger.LAMBDA_LOGGER;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@EnabledIfEnvironmentVariable(named = BemobiSmsServiceIntegrationTest.AUTH_STRING, matches = ".*")
@EnabledIfEnvironmentVariable(named = BemobiSmsServiceIntegrationTest.ACCOUNT_ID_ENV, matches = ".*")
@EnabledIfEnvironmentVariable(named = BemobiSmsServiceIntegrationTest.MSISDN_ENV, matches = ".*")
@EnabledIfEnvironmentVariable(named = BemobiSmsServiceIntegrationTest.SITE_ID_ENV, matches = ".*")
class BemobiSmsServiceIntegrationTest {

    public static final String AUTH_STRING = "AUTH_STRING";
    public static final String ACCOUNT_ID_ENV = "ACCOUNT_ID";
    public static final String MSISDN_ENV = "MSISDN";
    public static final String OPX_USER_ID_ENV = "OPX_USER_ID";
    public static final String SITE_ID_ENV = "SITE_ID";

    private static final String BEMOBI_EXPECTED_RESPONSE = readSystemResource("bemobi-successful-expected-response.json");

    @Test
    @SneakyThrows
    void shouldGetSuccessfulBemobiServiceResponse() {
        // given
        final var authString = System.getenv(AUTH_STRING);
        final var accountId = System.getenv(ACCOUNT_ID_ENV);
        final var msisdn = System.getenv(MSISDN_ENV);
        final var opxUserId = System.getenv(OPX_USER_ID_ENV);
        final var siteId = System.getenv(SITE_ID_ENV);
        final var message = "download bsafe"; // The message string should not be URL encoded


        final var bemobiRequest = BemobiRequest.builder()
                .schema(SERVER_SCHEME)
                .host(SERVER_HOST)
                .port(SERVER_PORT)
                .path(SERVER_PATH)
                .withParameters()
                .authString(AUTH_STRING)
                .accountId(accountId)
                .siteId(siteId)
                .msisdn(msisdn)
                .opxUserId(opxUserId)
                .message(message)
                .currentTime(String.valueOf(CommonUtils.getCurrentEpochTime()))
                .build();
        log.info("Bemobi request: \"{}\"", LoggableJson.of(bemobiRequest).toString());

        // when
        final var response = new BemobiClient(LAMBDA_LOGGER).get(bemobiRequest);
        log.info("Bemobi SMS service response: \"{}\"", LoggableJson.of(bemobiRequest).toString());

        // then
        assertThat(response.getStatusCode()).isEqualTo(0);
        JSONAssert.assertEquals(BEMOBI_EXPECTED_RESPONSE, response.getReason(), false);
    }
}
