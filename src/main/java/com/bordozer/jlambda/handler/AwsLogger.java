package com.bordozer.jlambda.handler;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.bordozer.bemobi.sdk.Logger;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AwsLogger implements Logger {

    private final LambdaLogger logger;

    public static AwsLogger of(final LambdaLogger logger) {
        return new AwsLogger(logger);
    }

    @Override
    public void log(final String message) {
        logger.log(message);
    }
}
