package com.bordozer.bemobi.sdk;

import com.bordozer.bemobi.sdk.model.BemobiParameters;
import com.bordozer.bemobi.sdk.model.BemobiRequest;
import com.bordozer.bemobi.sdk.utils.JsonUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import static com.bordozer.bemobi.sdk.BemobiClient.SERVER_HOST;
import static com.bordozer.bemobi.sdk.BemobiClient.SERVER_PATH;
import static com.bordozer.bemobi.sdk.BemobiClient.SERVER_PORT;
import static com.bordozer.bemobi.sdk.BemobiClient.SERVER_SCHEME;
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

    @Test
    @SneakyThrows
    void shouldGetBemobiServiceResponse() {
        // given
        final var authString = System.getenv(AUTH_STRING);
        final var accountId = System.getenv(ACCOUNT_ID_ENV);
        final var msisdn = System.getenv(MSISDN_ENV);
        final var opxUserId = System.getenv(OPX_USER_ID_ENV);
        final var siteId = System.getenv(SITE_ID_ENV);
        final var message = "joinbsafe"; // The message string should not be URL encoded

        final var bemobiParameters = BemobiParameters.builder()
                .authString(authString)
                .accountId(accountId)
                .siteId(siteId)
                .msisdn(msisdn)
                .opxUserId(opxUserId)
                .message(message)
                .currentTime("1596446607487")
                .build();

        final var bemobiRequest = BemobiRequest.builder()
                .schema(SERVER_SCHEME)
                .host(SERVER_HOST)
                .port(SERVER_PORT)
                .path(SERVER_PATH)
                .parameters(bemobiParameters)
                .build();
        log.info("Bemobi request: \"{}\"", JsonUtils.write(bemobiRequest));

        // when
        final var response = new BemobiClient(TestLambdaLogger.LAMBDA_LOGGER).get(bemobiRequest);
        log.info("Bemobi SMS service response: \"{}\"", JsonUtils.write(bemobiRequest));

        // then
        assertThat(response.getStatusCode()).isEqualTo(1001);
        assertThat(response.getReason()).isEqualTo("failed to send sms to user");
    }
}
