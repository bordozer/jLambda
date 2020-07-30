package com.bordozer.jlambda.handler;

import com.bordozer.commons.utils.LoggableJson;
import com.bordozer.jlambda.bemobi.BemobiRequestUtils;
import com.bordozer.jlambda.model.RemoteServiceRequest;
import com.bordozer.jlambda.utils.CommonUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.HashMap;
import java.util.Map;

import static com.bordozer.commons.utils.FileUtils.readSystemResource;
import static com.bordozer.jlambda.bemobi.BemobiRequestUtils.ACCOUNT_ID_PARAM;
import static com.bordozer.jlambda.bemobi.BemobiRequestUtils.API_KEY_PARAM;
import static com.bordozer.jlambda.bemobi.BemobiRequestUtils.MESSAGE_PARAM;
import static com.bordozer.jlambda.bemobi.BemobiRequestUtils.MSISDN_PARAM;
import static com.bordozer.jlambda.bemobi.BemobiRequestUtils.OPX_USER_ID_PARAM;
import static com.bordozer.jlambda.bemobi.BemobiRequestUtils.SITE_ID_PARAM;
import static com.bordozer.jlambda.handler.LambdaHandler.SERVER_HOST;
import static com.bordozer.jlambda.handler.LambdaHandler.SERVER_PATH;
import static com.bordozer.jlambda.handler.LambdaHandler.SERVER_PORT;
import static com.bordozer.jlambda.handler.LambdaHandler.SERVER_SCHEME;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@EnabledIfEnvironmentVariable(named = BemobiSmsServiceIntegrationTest.API_KEY_ENV, matches = ".*")
@EnabledIfEnvironmentVariable(named = BemobiSmsServiceIntegrationTest.ACCOUNT_ID_ENV, matches = ".*")
@EnabledIfEnvironmentVariable(named = BemobiSmsServiceIntegrationTest.MSISDN_ENV, matches = ".*")
@EnabledIfEnvironmentVariable(named = BemobiSmsServiceIntegrationTest.SITE_ID_ENV, matches = ".*")
class BemobiSmsServiceIntegrationTest {

    public static final String API_KEY_ENV = "API_KEY";
    public static final String ACCOUNT_ID_ENV = "ACCOUNT_ID";
    public static final String MSISDN_ENV = "MSISDN";
    public static final String OPX_USER_ID_ENV = "OPX_USER_ID";
    public static final String SITE_ID_ENV = "SITE_ID";

    private static final String BEMOBI_EXPECTED_RESPONSE = readSystemResource("bemobi-successful-expected-response.json");

    @Test
    @SneakyThrows
    void shouldGetSuccessfulBemobiServiceResponse() {
        // given
        final var apiKey = System.getenv(API_KEY_ENV);
        final var accountId = System.getenv(ACCOUNT_ID_ENV);
        final var msisdn = System.getenv(MSISDN_ENV);
        final var opxUserId = System.getenv(OPX_USER_ID_ENV);
        final var siteId = System.getenv(SITE_ID_ENV);
        final var message = "download bsafe"; // The message string should not be URL encoded

        final Map<String, String> requestParameters = new HashMap<>();
        requestParameters.put(API_KEY_PARAM, apiKey);
        requestParameters.put(ACCOUNT_ID_PARAM, accountId);
        requestParameters.put(MESSAGE_PARAM, message);
        requestParameters.put(MSISDN_PARAM, msisdn);
        requestParameters.put(OPX_USER_ID_PARAM, opxUserId);
        requestParameters.put(SITE_ID_PARAM, siteId);
        log.info("Request parameters: \"{}\"", LoggableJson.of(requestParameters).toString());

        final var bemobiParameters = BemobiRequestUtils.convertToBemobiParameters(requestParameters);
        log.info("Bemobi parameters: \"{}\"", LoggableJson.of(bemobiParameters).toString());

        final var serviceRequest = RemoteServiceRequest.builder()
                .schema(SERVER_SCHEME)
                .host(SERVER_HOST)
                .port(SERVER_PORT)
                .path(SERVER_PATH)
                .parameters(bemobiParameters)
                .build();
        log.info("Bemobi request: \"{}\"", LoggableJson.of(serviceRequest).toString());

        // when
        final var response = RemoteServiceHandler.get(serviceRequest);
        log.info("Bemobi SMS service response: \"{}\"", LoggableJson.of(serviceRequest).toString());

        // then
        assertThat(response.getResponseCode()).isEqualTo(200);
        JSONAssert.assertEquals(BEMOBI_EXPECTED_RESPONSE, response.getResponseBody(), false);
    }
}
