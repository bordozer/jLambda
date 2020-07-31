package com.bordozer.jlambda.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.bordozer.commons.utils.LoggableJson;
import com.bordozer.jlambda.bemobi.BemobiRequestUtils;
import com.bordozer.jlambda.model.LambdaResponse;
import com.bordozer.jlambda.model.LambdaResponsePayload;
import com.bordozer.jlambda.model.RemoteServiceRequest;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;

import javax.annotation.Nullable;
import java.util.Map;

public class LambdaHandler implements RequestHandler<Map<String, Object>, JSONObject> {

    public static final String SERVER_SCHEME = "http";
    public static final String SERVER_HOST = "bpx.bemobi.com";
    public static final int SERVER_PORT = 80;
    public static final String SERVER_PATH = "/opx/1.0/OPXSendSms";

    public static final String HEALTH_CHECK = "health-check";

    public static final String QUERY_STRING_PARAMETERS = "queryStringParameters";

    @Override
    public JSONObject handleRequest(final Map<String, Object> input, final Context context) {
        final LambdaLogger logger = context.getLogger();

        logger.log(String.format("Lambda input: %s", LoggableJson.of(input).toString()));

        @Nullable final var requestParameters = getRequestParameters(input);
        if (requestParameters == null) {
            final var response = new LambdaResponse(422, "Lambda's parameters should not be null");
            logLambdaResponse(logger, response);
            return response;
        }
        logger.log(String.format("Request parameters: \"%s\"", LoggableJson.of(requestParameters).toString()));

        @Nullable final var healthCheck = requestParameters.get(HEALTH_CHECK);
        if ("yes".equals(healthCheck)) {
            final var response = new LambdaResponse(200, "Health check is OK");
            logLambdaResponse(logger, response);
            return response;
        }

        @Nullable final var apiKey = requestParameters.get(BemobiRequestUtils.API_KEY_PARAM);
        if (StringUtils.isBlank(apiKey)) {
            final var response = new LambdaResponse(422, String.format("ApiKey have to be provided as request parameter '%s'", BemobiRequestUtils.API_KEY_PARAM));
            logLambdaResponse(logger, response);
            return response;
        }

        final Map<String, String> bemobiParameters = BemobiRequestUtils.convertToBemobiParameters(requestParameters);
        logger.log(String.format("Bemobi parameters: \"%s\"", LoggableJson.of(bemobiParameters).toString()));

        final var serviceRequest = RemoteServiceRequest.builder()
                .schema(SERVER_SCHEME)
                .host(SERVER_HOST)
                .port(SERVER_PORT)
                .path(SERVER_PATH)
                .parameters(bemobiParameters)
                .build();
        logger.log(String.format("Bemobi service: \"%s\"", serviceRequest.getRemoteServiceUrl()));

        final var response = new RemoteServiceHandler(logger).get(serviceRequest);
        logger.log(String.format("Bemobi service response: %s", LoggableJson.of(response).toString()));

        final var responseObject = new LambdaResponse(response.getResponseCode(), response.getResponseBody());
        logLambdaResponse(logger, responseObject);
        return responseObject;
    }

    @Nullable
    private static Map<String, String> getRequestParameters(final Map<String, Object> input) {
        if (input.get(QUERY_STRING_PARAMETERS) == null) {
            return null;
        }
        return (Map<String, String>) input.get("queryStringParameters");
    }

    private void logLambdaResponse(final LambdaLogger logger, final LambdaResponse response) {
        logger.log(String.format("Lambda response: %s", response.toJSONString()));
    }
}
