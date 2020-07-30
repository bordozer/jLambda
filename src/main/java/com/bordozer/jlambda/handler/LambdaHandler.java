package com.bordozer.jlambda.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.bordozer.commons.utils.LoggableJson;
import com.bordozer.jlambda.model.RemoteServiceRequest;
import org.json.simple.JSONObject;

import java.util.Map;

public class LambdaHandler implements RequestHandler<Map<String, Object>, JSONObject> {

    public static final String SERVER_SCHEME = "http";
    public static final String SERVER_HOST = "bpx.bemobi.com";
    public static final int SERVER_PORT = 80;
    public static final String SERVER_PATH = "/opx/1.0/OPXSendSms";

    private static final String LAMBDA_BODY_TAG = "body";

    @Override
    public JSONObject handleRequest(final Map<String, Object> input, final Context context) {
        final LambdaLogger logger = context.getLogger();

        logger.log(String.format("Lambda input: %s", LoggableJson.of(input).toString()));

        final Map<String, String> parameters = (Map<String, String>) input.get("queryStringParameters");
        logger.log(String.format("Parameters: \"%s\"", LoggableJson.of(parameters).toString()));

        final var serviceRequest = RemoteServiceRequest.builder()
                .schema(SERVER_SCHEME)
                .host(SERVER_HOST)
                .port(SERVER_PORT)
                .path(SERVER_PATH)
                .parameters(parameters)
                .build();
        logger.log(String.format("Remote service: \"%s\"", serviceRequest.getRemoteServiceUrl()));

        final var response = RemoteServiceHandler.get(serviceRequest);
        logger.log(String.format("Remote service response: %s", LoggableJson.of(response).toString()));

        final JSONObject responseObject = new JSONObject();
        responseObject.put(LAMBDA_BODY_TAG, response.getResponseBody());
        logger.log(String.format("Lambda response: %s", responseObject.toJSONString()));

        return responseObject;
    }
}
