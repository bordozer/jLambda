package com.bordozer.jlambda.model;

import com.bordozer.commons.utils.JsonUtils;
import com.bordozer.commons.utils.LoggableJson;
import org.json.simple.JSONObject;

public class LambdaResponse extends JSONObject {

    public LambdaResponse(final int responseCode, final Object response) {
        final var payload = new JSONObject();
        payload.put("payload", JsonUtils.write(response));

        final var headers = new JSONObject();
        headers.put("Content-Type", "application/json");

        this.put("statusCode", responseCode);
        this.put("headers", headers);
        this.put("isBase64Encoded", false);
        this.put("body", JsonUtils.write(payload));
    }
}
