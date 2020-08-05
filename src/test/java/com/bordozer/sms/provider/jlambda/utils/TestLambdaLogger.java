package com.bordozer.sms.provider.jlambda.utils;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class TestLambdaLogger implements LambdaLogger {

    public static final TestLambdaLogger LAMBDA_LOGGER = new TestLambdaLogger();

    @Override
    public void log(final String message) {
        log.info(message);
    }

    @Override
    public void log(final byte[] message) {
        log.info(new String(message));
    }
}
