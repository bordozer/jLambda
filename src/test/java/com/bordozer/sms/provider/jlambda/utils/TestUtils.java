package com.bordozer.sms.provider.jlambda.utils;

import com.amazonaws.services.lambda.runtime.Context;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import static com.bordozer.sms.provider.jlambda.handler.LambdaHandler.QUERY_STRING_PARAMETERS;
import static com.bordozer.sms.provider.jlambda.utils.TestLambdaLogger.LAMBDA_LOGGER;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestUtils {

    public static Map<String, Object> singleParameterMap(final String name, final String value) {
        final var requestParamsMap = new HashMap<String, String>();
        requestParamsMap.put(name, value);

        final var map = new HashMap<String, Object>();
        map.put(QUERY_STRING_PARAMETERS, requestParamsMap);

        return map;
    }

    public static Context getContext() {
        final var mock = mock(Context.class);
        when(mock.getLogger()).thenReturn(LAMBDA_LOGGER);
        return mock;
    }
}
