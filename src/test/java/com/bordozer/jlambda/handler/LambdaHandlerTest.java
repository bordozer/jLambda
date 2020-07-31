package com.bordozer.jlambda.handler;

import com.amazonaws.services.lambda.runtime.Context;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.HashMap;

import static com.bordozer.commons.utils.FileUtils.readSystemResource;
import static com.bordozer.jlambda.handler.LambdaHandler.HEALTH_CHECK;
import static com.bordozer.jlambda.handler.LambdaHandler.QUERY_STRING_PARAMETERS;
import static com.bordozer.jlambda.utils.TestLambdaLogger.LAMBDA_LOGGER;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
class LambdaHandlerTest {

    private static final String HEALTH_CHECK_ECPECTED_RESPONSE = readSystemResource("lambda-health-check-expected-response.json");

    @Test
    @SneakyThrows
    void shouldResponseOnHealthCheck() {
        // given
        final var requestParamsMap = new HashMap<String, Object>();
        requestParamsMap.put(HEALTH_CHECK, "yes");

        final var map = new HashMap<String, Object>();
        map.put(QUERY_STRING_PARAMETERS, requestParamsMap);

        // when
        final var response = new LambdaHandler().handleRequest(map, getContext());

        // then
        JSONAssert.assertEquals(HEALTH_CHECK_ECPECTED_RESPONSE, response.toJSONString(), false);
    }

    private Context getContext() {
        final var mock = mock(Context.class);
        when(mock.getLogger()).thenReturn(LAMBDA_LOGGER);
        return mock;
    }
}
