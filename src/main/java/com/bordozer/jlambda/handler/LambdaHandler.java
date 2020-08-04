package com.bordozer.jlambda.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.bordozer.bemobi.sdk.BemobiClient;
import com.bordozer.bemobi.sdk.Logger;
import com.bordozer.jlambda.bemobi.BemobiHandler;
import com.bordozer.jlambda.model.LambdaResponse;
import com.bordozer.jlambda.utils.JsonUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;

import javax.annotation.Nullable;
import java.util.Map;

import static com.bordozer.bemobi.sdk.BemobiClient.API_KEY_PARAM;

public class LambdaHandler implements RequestHandler<Map<String, Object>, JSONObject> {

    public static final String QUERY_STRING_PARAMETERS = "queryStringParameters";
    public static final String HEALTH_CHECK = "health-check";

    @Override
    public JSONObject handleRequest(final Map<String, Object> input, final Context context) {
        final Logger logger = AwsLogger.of(context.getLogger());

        logger.log(String.format("Lambda input: %s", JsonUtils.write(input)));

        @Nullable final var requestParameters = getRequestParameters(input);
        if (requestParameters == null) {
            final var response = createLambdaResponse(422, "Lambda's parameters should not be null");
            logLambdaResponse(logger, response);
            return response;
        }
        logger.log(String.format("Request parameters: \"%s\"", JsonUtils.write(requestParameters)));

        @Nullable final var healthCheck = requestParameters.get(HEALTH_CHECK);
        if ("yes".equals(healthCheck)) {
            final var response = createLambdaResponse(200, "Health check is OK");
            logLambdaResponse(logger, response);
            return response;
        }

        @Nullable final var apiKey = requestParameters.get(API_KEY_PARAM);
        if (StringUtils.isBlank(apiKey)) {
            final var response = createLambdaResponse(422, String.format("ApiKey have to be provided as request parameter '%s'", API_KEY_PARAM));
            logLambdaResponse(logger, response);
            return response;
        }

        final var response = SmsProviderFactory.instance(logger).handle(requestParameters);
        logger.log(String.format("Lambda response: %s", response.toJSONString()));
        return response;
    }

    private LambdaResponse createLambdaResponse(final int i, final String s) {
        return new LambdaResponse(i, LambdaResponsePayload.of(s));
    }

    @Nullable
    private static Map<String, String> getRequestParameters(final Map<String, Object> input) {
        if (input.get(QUERY_STRING_PARAMETERS) == null) {
            return null;
        }
        return (Map<String, String>) input.get("queryStringParameters");
    }

    private void logLambdaResponse(final Logger logger, final LambdaResponse response) {
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
