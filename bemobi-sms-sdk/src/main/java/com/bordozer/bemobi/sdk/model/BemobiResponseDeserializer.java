package com.bordozer.bemobi.sdk.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class BemobiResponseDeserializer extends JsonDeserializer<BemobiResponse> {

    // Note: the properties are capitalized
    private static final String BEMOBI_STATUS_CODE_PROP = "StatusCode";
    private static final String BEMOBI_REASON_PROP = "Reason";

    @Override
    public BemobiResponse deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
        final JsonNode node = jp.getCodec().readTree(jp);
        final int statusCode = node.get(BEMOBI_STATUS_CODE_PROP).intValue();
        final String reason = node.get(BEMOBI_REASON_PROP).asText();
        return BemobiResponse.builder()
                .statusCode(statusCode)
                .reason(reason)
                .build();
    }
}
