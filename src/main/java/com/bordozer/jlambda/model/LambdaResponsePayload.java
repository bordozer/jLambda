package com.bordozer.jlambda.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LambdaResponsePayload {

    private final String payload;

    public static LambdaResponsePayload of(final String payload) {
        return new LambdaResponsePayload(payload);
    }
}
