package com.bordozer.jlambda.handler;

import com.bordozer.jlambda.utils.TestUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.Collections;

import static com.bordozer.commons.utils.FileUtils.readSystemResource;
import static com.bordozer.jlambda.handler.LambdaHandler.HEALTH_CHECK;
import static com.bordozer.jlambda.utils.TestUtils.getContext;
import static com.bordozer.jlambda.utils.TestUtils.singleParameterMap;

@Slf4j
class LambdaHandlerTest {

    private static final String HEALTH_CHECK_EXPECTED_RESPONSE = String.format(
            readSystemResource("lambda-response-template.json"), 200, "Health check is OK"
    );
    private static final String MISSED_LAMBDA_PARAMETERS_EXPECTED_RESPONSE = String.format(
            readSystemResource("lambda-response-template.json"), 422, "Lambda's parameters should not be null"
    );
    private static final String MISSED_API_KEY_PARAMETER_EXPECTED_RESPONSE = String.format(
            readSystemResource("lambda-response-template.json"), 422, "ApiKey have to be provided as request parameter 'ApiKey'"
    );

    @Test
    @SneakyThrows
    void shouldResponseHealthCheck() {
        // given
        final var map = singleParameterMap(HEALTH_CHECK, "yes");

        // when
        final var response = new LambdaHandler().handleRequest(map, getContext());

        // then
        JSONAssert.assertEquals(HEALTH_CHECK_EXPECTED_RESPONSE, response.toJSONString(), false);
    }

    @Test
    @SneakyThrows
    void shouldResponse422IfApiKeyIsMissedInRequestParameters() {
        // given
        final var map = Collections.<String, Object>emptyMap();

        // when
        final var response = new LambdaHandler().handleRequest(map, getContext());

        // then
        JSONAssert.assertEquals(MISSED_LAMBDA_PARAMETERS_EXPECTED_RESPONSE, response.toJSONString(), false);
    }

    @Test
    @SneakyThrows
    void shouldResponse422IfNoRequestParameters() {
        // given
        final var map = singleParameterMap("parameter", "value");

        // when
        final var response = new LambdaHandler().handleRequest(map, getContext());

        // then
        JSONAssert.assertEquals(MISSED_API_KEY_PARAMETER_EXPECTED_RESPONSE, response.toJSONString(), false);
    }
}
