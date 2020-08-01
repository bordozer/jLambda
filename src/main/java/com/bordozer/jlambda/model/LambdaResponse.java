package com.bordozer.jlambda.model;

import com.bordozer.commons.utils.JsonUtils;
import org.json.simple.JSONObject;

public class LambdaResponse extends JSONObject {

    private static final String AWS_LAMBDA_RESPONSE_PAYLOAD_TAG = "body";

    public LambdaResponse(final int responseCode, final Object response) {
        final var headers = new JSONObject();
        headers.put("Content-Type", "application/json");

        this.put("statusCode", responseCode);
        this.put("headers", headers);
        this.put("isBase64Encoded", false);
        this.put(AWS_LAMBDA_RESPONSE_PAYLOAD_TAG, JsonUtils.write(response)); // should have String type, not Object
    }
}
