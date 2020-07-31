package com.bordozer.jlambda.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import static com.bordozer.jlambda.handler.LambdaHandler.QUERY_STRING_PARAMETERS;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequestUtils {

    public static Map<String, Object> singleParameterMap(final String name, final String value) {
        final var requestParamsMap = new HashMap<String, String>();
        requestParamsMap.put(name, value);

        final var map = new HashMap<String, Object>();
        map.put(QUERY_STRING_PARAMETERS, requestParamsMap);

        return map;
    }
}
