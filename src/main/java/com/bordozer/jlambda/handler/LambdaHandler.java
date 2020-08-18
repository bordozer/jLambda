package com.bordozer.jlambda.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.bordozer.jlambda.model.LambdaResponse;
import com.bordozer.jlambda.utils.JsonUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;

public class LambdaHandler implements RequestHandler<Map<String, Object>, JSONObject> {

    public static final String QUERY_STRING_PARAMETERS = "queryStringParameters";
    public static final String HEALTH_CHECK = "health-check";

    @Override
    public JSONObject handleRequest(final Map<String, Object> input, final Context context) {
        final var logger = context.getLogger();
        logger.log(String.format("Lambda input: %s", JsonUtils.write(input)));
        final var requestParameters = getRequestParameters(input);

        @Nullable final var healthCheck = requestParameters.get(HEALTH_CHECK);
        if ("yes".equals(healthCheck)) {
            final var response = createLambdaResponse(200, "Health check is OK");
            logLambdaResponse(logger, response);
            return response;
        }

        final var response = createLambdaResponse(200, "Lambda invoke result");
        logLambdaResponse(logger, response);
        return response;
    }

    private LambdaResponse createLambdaResponse(final int responseCode, final String payload) {
        return new LambdaResponse(responseCode, LambdaResponsePayload.of(payload));
    }

    private static Map<String, String> getRequestParameters(final Map<String, Object> input) {
        if (input.get(QUERY_STRING_PARAMETERS) == null) {
            return Collections.emptyMap();
        }
        return (Map<String, String>) input.get("queryStringParameters");
    }

    private void logLambdaResponse(final LambdaLogger logger, final LambdaResponse response) {
        logger.log(String.format("Lambda response: %s", response.toJSONString()));
    }

    @Getter
    @RequiredArgsConstructor
    private static class LambdaResponsePayload {
        private final String payload;

        private static LambdaResponsePayload of(final String value) {
            return new LambdaResponsePayload(value);
        }
    }
}
