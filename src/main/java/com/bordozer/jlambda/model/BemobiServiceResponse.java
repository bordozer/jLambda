package com.bordozer.jlambda.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@Builder
@ToString
public class BemobiServiceResponse {
    @NonNull
    private final Integer responseCode;
    @NonNull
    private final String responseBody;
}
