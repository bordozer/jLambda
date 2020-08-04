package com.bordozer.jlambda.handler;

import com.bordozer.bemobi.sdk.BemobiClient;
import com.bordozer.bemobi.sdk.Logger;
import com.bordozer.jlambda.api.SmsProvider;
import com.bordozer.jlambda.bemobi.BemobiHandler;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SmsProviderFactory {

    public static SmsProvider instance(final Logger logger) {
        return BemobiHandler.builder()
                .bemobiClient(new BemobiClient(logger))
                .logger(logger)
                .build();
    }
}
