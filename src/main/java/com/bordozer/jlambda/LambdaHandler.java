package com.bordozer.jlambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.bordozer.commons.utils.LoggableJson;
import org.json.simple.JSONObject;

import java.util.Map;

public class LambdaHandler implements RequestHandler<Map<String, Object>, JSONObject> {

    @Override
    public JSONObject handleRequest(final Map<String, Object> input, final Context context) {
        final LambdaLogger logger = context.getLogger();

        logger.log(String.format("Lambda input: %s", LoggableJson.of(input).toString()));

        final var serverUrl = System.getenv("SERVER_URL");
        final var serverPort = Integer.parseInt(System.getenv("SERVER_PORT"));
        logger.log(String.format("Remote service API: \"%s:%s\"", serverUrl, serverPort));

        final var response = RemoteServiceHandler.get(serverUrl, serverPort);
        logger.log(String.format("Remote service response: %s", LoggableJson.of(response).toString()));

        final JSONObject responseObject = new JSONObject();
        responseObject.put("response", response);
        logger.log(String.format("Lambda response: %s", responseObject.toJSONString()));

        return responseObject;
    }
}
