package com.bordozer.bemobi.sdk;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class TestLambdaLogger implements Logger {

    public static final TestLambdaLogger LAMBDA_LOGGER = new TestLambdaLogger();

    @Override
    public void log(final String message) {
        log.info(message);
    }
}
