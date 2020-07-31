package com.bordozer.jlambda.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@Builder
@ToString
public class BemobiResponse {
    @NonNull
    private final Integer StatusCode;
    @NonNull
    private final String Reason;
}
