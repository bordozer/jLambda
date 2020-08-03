package com.bordozer.jlambda.handler;

import com.bordozer.bemobi.sdk.BemobiClient;
import com.bordozer.bemobi.sdk.model.BemobiRequest;
import com.bordozer.bemobi.sdk.model.BemobiResponse;
import com.bordozer.jlambda.utils.TestLogger;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.HashMap;

import static com.bordozer.bemobi.sdk.BemobiClient.ACCOUNT_ID_PARAM;
import static com.bordozer.bemobi.sdk.BemobiClient.API_KEY_PARAM;
import static com.bordozer.bemobi.sdk.BemobiClient.AUTH_STRING_PARAM;
import static com.bordozer.bemobi.sdk.BemobiClient.CURRENT_TIME_PARAM;
import static com.bordozer.bemobi.sdk.BemobiClient.MESSAGE_PARAM;
import static com.bordozer.bemobi.sdk.BemobiClient.MSISDN_PARAM;
import static com.bordozer.bemobi.sdk.BemobiClient.OPX_USER_ID_PARAM;
import static com.bordozer.bemobi.sdk.BemobiClient.SERVER_HOST;
import static com.bordozer.bemobi.sdk.BemobiClient.SERVER_PATH;
import static com.bordozer.bemobi.sdk.BemobiClient.SERVER_PORT;
import static com.bordozer.bemobi.sdk.BemobiClient.SERVER_SCHEME;
import static com.bordozer.bemobi.sdk.BemobiClient.SITE_ID_PARAM;
import static com.bordozer.jlambda.utils.CommonUtils.readResource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BemobiHandlerTest {

    private static final String ACCOUNT_ID = "account-id";
    private static final String SITE_ID = "site-id";
    private static final String MSISDN = "msisdn";
    private static final String USER_ID = "user-id";
    private static final String MESSAGE = "message";

    private static final BemobiResponse BEMOBI_RESPONSE = BemobiResponse.builder()
            .StatusCode(0)
            .Reason("Message sent successfully")
            .build();

    private static final String LAMBDA_EXPECTED_RESPONSE = String.format(
            readResource("lambda-response-template.json"), 200, "{\"statusCode\":0,\"reason\":\"Message sent successfully\"}".replace("\"", "\\\"")
    );
    private static final String FAKE_API_KEY_HEX = "1056E0F39CD97BE9AE45A";

    @Test
    @SneakyThrows
    void shouldGetRemoteServiceResponse() {
        // given
        final var argumentCaptor = ArgumentCaptor.forClass(BemobiRequest.class);

        final var bemobiClient = mock(BemobiClient.class);
        when(bemobiClient.get(argumentCaptor.capture())).thenReturn(BEMOBI_RESPONSE);

        final var requestParameters = new HashMap<String, String>();
        requestParameters.put(API_KEY_PARAM, FAKE_API_KEY_HEX);
        requestParameters.put(ACCOUNT_ID_PARAM, ACCOUNT_ID);
        requestParameters.put(MSISDN_PARAM, MSISDN);
        requestParameters.put(OPX_USER_ID_PARAM, USER_ID);
        requestParameters.put(SITE_ID_PARAM, SITE_ID);
        requestParameters.put(MESSAGE_PARAM, MESSAGE);

        // when
        final var handler = BemobiHandler.builder()
                .bemobiClient(bemobiClient)
                .logger(TestLogger.TEST_LOGGER)
                .build();
        final var response = handler.handle(requestParameters);

        // then
        JSONAssert.assertEquals(LAMBDA_EXPECTED_RESPONSE, response.toJSONString(), false);

        final var bemobiRequest = argumentCaptor.getValue();
        assertThat(bemobiRequest.getSchema()).isEqualTo(SERVER_SCHEME);
        assertThat(bemobiRequest.getHost()).isEqualTo(SERVER_HOST);
        assertThat(bemobiRequest.getPort()).isEqualTo(SERVER_PORT);
        assertThat(bemobiRequest.getPath()).isEqualTo(SERVER_PATH);
        assertThat(bemobiRequest.getParameters().asMap())
                .hasSize(7)
                .containsKeys(AUTH_STRING_PARAM, ACCOUNT_ID_PARAM, MSISDN_PARAM, OPX_USER_ID_PARAM, SITE_ID_PARAM, MESSAGE_PARAM, CURRENT_TIME_PARAM);
    }
}
