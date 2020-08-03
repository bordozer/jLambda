package com.bordozer.bemobi.sdk.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Builder
@ToString
@JsonDeserialize(using = BemobiResponseDeserializer.class)
public class BemobiResponse implements Serializable {
    @NonNull
    private final Integer statusCode;
    @NonNull
    private final String reason;
}
