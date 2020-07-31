package com.bordozer.jlambda.model;

import org.json.simple.JSONObject;

public class AlbResponse extends JSONObject {

    public AlbResponse(final int responseCode, final String responseBody) {
        final var headers = new JSONObject();
        headers.put("Content-Type", "application/json");

        this.put("statusCode", responseCode);
        this.put("headers", headers);
        this.put("body", responseBody);
        this.put("isBase64Encoded", false);
    }
}
