package com.bordozer.jlambda;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.skyscreamer.jsonassert.JSONAssert;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.bordozer.commons.utils.FileUtils.readSystemResource;
import static com.bordozer.jlambda.LambdaHandler.SERVER_HOST;
import static com.bordozer.jlambda.LambdaHandler.SERVER_PATH;
import static com.bordozer.jlambda.LambdaHandler.SERVER_PORT;
import static com.bordozer.jlambda.LambdaHandler.SERVER_SCHEME;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@EnabledIfEnvironmentVariable(named = BemobiIntegrationTest.API_KEY_ENV, matches = ".*")
@EnabledIfEnvironmentVariable(named = BemobiIntegrationTest.ACCOUNT_ID_ENV, matches = ".*")
@EnabledIfEnvironmentVariable(named = BemobiIntegrationTest.MSISDN_ENV, matches = ".*")
@EnabledIfEnvironmentVariable(named = BemobiIntegrationTest.OPX_USER_ID_ENV, matches = ".*")
@EnabledIfEnvironmentVariable(named = BemobiIntegrationTest.SITE_ID_ENV, matches = ".*")
class BemobiIntegrationTest {

    public static final String API_KEY_ENV = "API_KEY";
    public static final String ACCOUNT_ID_ENV = "ACCOUNT_ID";
    public static final String MSISDN_ENV = "MSISDN";
    public static final String OPX_USER_ID_ENV = "OPX_USER_ID";
    public static final String SITE_ID_ENV = "SITE_ID";

    private static final String ACCOUNT_ID_PARAM = "AccountID";
    private static final String MSISDN_PARAM = "Msisdn";
    private static final String OPX_USER_ID_PARAM = "OPXUserID";
    private static final String SITE_ID_PARAM = "SiteID";
    private static final String MESSAGE_PARAM = "Message";
    private static final String CURRENT_TIME_PARAM = "CurrentTime";
    private static final String AUTH_STRING_PARAM = "AuthString";

    private static final String OPTIONAL_UNSPECIFIED_PARAM_VALUE = "unspecified";

    private static final String BEMOBI_EXPECTED_RESPONSE = readSystemResource("bemobi-successful-expected-response.json");

    @Test
    @SneakyThrows
    void shouldGetSuccessfulBemobiServiceResponse() {
        // unclear
        // TODO: Note: This will not work for unsubscribed users
        // TODO: Note: The hex-encoded key should be converted to 20 binary bytes before the HMAC-SHA1 computation.

        // given
        final var apiKey = System.getenv(API_KEY_ENV);
        final var accountId = System.getenv(ACCOUNT_ID_ENV);
        final var msisdn = System.getenv(MSISDN_ENV);
        final var opxUserId = System.getenv(OPX_USER_ID_ENV);
        final var siteId = System.getenv(SITE_ID_ENV);
        final var message = "download bsafe"; // The message string should not be URL encoded

        final var currentTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        /*final var calculatedSignature = new StringBuilder()
                .append(ACCOUNT_ID_PARAM.toLowerCase()).append(accountId)
                .append(CURRENT_TIME_PARAM.toLowerCase()).append(currentTime)
                .append(MESSAGE_PARAM.toLowerCase()).append(message.toLowerCase())
                .append(MSISDN_PARAM.toLowerCase()).append(msisdn.toLowerCase())
                .append(OPX_USER_ID_PARAM.toLowerCase()).append(opxUserId)
                .append(SITE_ID_PARAM.toLowerCase()).append(siteId)
                .toString();*/


        final Map<String, String> map = new HashMap<>();
        map.put(ACCOUNT_ID_PARAM, accountId);
        map.put(CURRENT_TIME_PARAM, String.valueOf(currentTime));
        map.put(MESSAGE_PARAM, message);
        map.put(MSISDN_PARAM, msisdn);
        map.put(OPX_USER_ID_PARAM, opxUserId);
        map.put(SITE_ID_PARAM, siteId);

        final var calculatedSignature = map.keySet().stream()
                .sorted()
                .map(parameter -> String.format("%s%s", parameter.toLowerCase(), map.get(parameter).toLowerCase()))
                .collect(Collectors.joining());
        final var authString = new HmacUtils(HmacAlgorithms.HMAC_SHA_1, apiKey).hmacHex(calculatedSignature);
        log.info("AuthString: \"{}\"", authString);
        map.put(AUTH_STRING_PARAM, authString);

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
        JSONAssert.assertEquals(BEMOBI_EXPECTED_RESPONSE, response.getResponseBody(), false);
    }
}
