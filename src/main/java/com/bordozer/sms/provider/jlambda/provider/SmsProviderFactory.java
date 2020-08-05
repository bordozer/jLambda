package com.bordozer.sms.provider.jlambda.provider;

import com.bordozer.sms.provider.jlambda.api.SmsProvider;
import com.bordozer.sms.provider.jlambda.provider.bemobi.BemobiProvider;
import com.bordozer.sms.provider.sdk.Logger;
import com.bordozer.sms.provider.sdk.provider.bemobi.BemobiClient;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SmsProviderFactory {

    public static SmsProvider instance(final Logger logger) {
        // TODO: extend for other providers
        return BemobiProvider.builder()
                .bemobiClient(new BemobiClient(logger))
                .logger(logger)
                .build();
    }
}
