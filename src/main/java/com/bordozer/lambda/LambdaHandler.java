package com.bordozer.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.bordozer.commons.utils.FileUtils;
import com.bordozer.commons.utils.LoggableJson;
import org.json.simple.JSONObject;

import java.util.Map;

public class LambdaHandler implements RequestHandler<Map<String, String>, JSONObject> {

    private static final String BODY = FileUtils.readSystemResource("fake-response.json");

    @Override
    public JSONObject handleRequest(final Map<String, String> input, final Context context) {
        final LambdaLogger logger = context.getLogger();

        logger.log(LoggableJson.of(input).toString());

        final JSONObject responseObject = new JSONObject();
        responseObject.put("body", BODY);

        return responseObject;
    }
}
