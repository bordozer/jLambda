package com.bordozer.jlambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.bordozer.commons.utils.LoggableJson;
import com.google.common.io.Resources;
import lombok.SneakyThrows;
import org.json.simple.JSONObject;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class LambdaHandler implements RequestHandler<Map<String, Object>, JSONObject> {

    @Override
    public JSONObject handleRequest(final Map<String, Object> input, final Context context) {
        final LambdaLogger logger = context.getLogger();

        logger.log(String.format("Input: %s", LoggableJson.of(input).toString()));

        final JSONObject responseObject = new JSONObject();
        final String body = readResource();
        responseObject.put("body", body);

        logger.log(String.format("Response: %s", LoggableJson.of(body).toString()));

        return responseObject;
    }

    @SneakyThrows
    private String readResource() {
        final URL url = Resources.getResource("fake-response.json");
        return Resources.toString(url, StandardCharsets.UTF_8);
    }
}
