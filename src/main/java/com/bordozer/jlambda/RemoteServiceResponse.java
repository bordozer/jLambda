package com.bordozer.jlambda;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@Builder
@ToString
public class RemoteServiceResponse {
    @NonNull
    private final int responseCode;
    @NonNull
    private final String responseBody;
}