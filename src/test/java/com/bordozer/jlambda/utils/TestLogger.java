package com.bordozer.jlambda.utils;

import com.bordozer.bemobi.sdk.Logger;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestLogger implements Logger {

    public static final TestLogger TEST_LOGGER = new TestLogger();

    @Override
    public void log(final String message) {
        log.debug(message);
    }
}
