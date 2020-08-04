package com.bordozer.jlambda.bemobi;

import com.bordozer.jlambda.bemobi.BemobiParametersBuilder;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static com.bordozer.bemobi.sdk.BemobiClient.ACCOUNT_ID_PARAM;
import static com.bordozer.bemobi.sdk.BemobiClient.API_KEY_PARAM;
import static com.bordozer.bemobi.sdk.BemobiClient.AUTH_STRING_PARAM;
import static com.bordozer.bemobi.sdk.BemobiClient.CURRENT_TIME_PARAM;
import static com.bordozer.bemobi.sdk.BemobiClient.MESSAGE_PARAM;
import static com.bordozer.bemobi.sdk.BemobiClient.MSISDN_PARAM;
import static com.bordozer.bemobi.sdk.BemobiClient.OPX_USER_ID_PARAM;
import static com.bordozer.bemobi.sdk.BemobiClient.SITE_ID_PARAM;
import static org.assertj.core.api.Assertions.assertThat;

public class BemobiParametersBuilderTest extends TestCase {

    private static final String FAKE_API_KEY_HEX = "1056E0F39CD97BE9AE45A";

    private static final String ACCOUNT_ID = "account-id";
    private static final String SITE_ID = "site-id";
    private static final String MSISDN = "msisdn";
    private static final String USER_ID = "user-id";
    private static final String MESSAGE = "message";

    @Test
    void shouldGetBemobiServiceResponse() {
        // given
        final var map = new HashMap<String, String>();
        map.put(API_KEY_PARAM, FAKE_API_KEY_HEX);
        map.put(ACCOUNT_ID_PARAM, ACCOUNT_ID);
        map.put(SITE_ID_PARAM, SITE_ID);
        map.put(MSISDN_PARAM, MSISDN);
        map.put(OPX_USER_ID_PARAM, USER_ID);
        map.put(MESSAGE_PARAM, MESSAGE);
        map.put("unknownParam", "value");

        // when
        final var bemobiParameters = BemobiParametersBuilder.build(map);

        // then
        assertThat(bemobiParameters.asMap())
                .hasSize(7)
                .containsKeys(AUTH_STRING_PARAM, ACCOUNT_ID_PARAM, MSISDN_PARAM, OPX_USER_ID_PARAM, SITE_ID_PARAM, MESSAGE_PARAM, CURRENT_TIME_PARAM);
        assertThat(bemobiParameters.getAccountId()).isEqualTo(ACCOUNT_ID);
        assertThat(bemobiParameters.getSiteId()).isEqualTo(SITE_ID);
        assertThat(bemobiParameters.getMsisdn()).isEqualTo(MSISDN);
        assertThat(bemobiParameters.getOpxUserId()).isEqualTo(USER_ID);
        assertThat(bemobiParameters.getMessage()).isEqualTo(MESSAGE);
    }
}
