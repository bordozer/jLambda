package com.bordozer.bemobi.sdk.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.io.IOException;
import java.io.Serializable;

@Getter
@Builder
@ToString
@JsonDeserialize(using = BemobiResponse.BemobiResponseDeserializer.class)
public class BemobiResponse implements Serializable {

    // Note: the properties are capitalized
    private static final String BEMOBI_STATUS_CODE_PROP = "StatusCode";
    private static final String BEMOBI_REASON_PROP = "Reason";

    @NonNull
    private final Integer statusCode;
    @NonNull
    private final String reason;

    public static class BemobiResponseDeserializer extends JsonDeserializer<BemobiResponse> {

        @Override
        public BemobiResponse deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
            final JsonNode node = jp.getCodec().readTree(jp);
            final int statusCode = node.get(BEMOBI_STATUS_CODE_PROP).asInt();
            final String reason = node.get(BEMOBI_REASON_PROP).asText();
            return BemobiResponse.builder()
                    .statusCode(statusCode)
                    .reason(reason)
                    .build();
        }
    }
}
