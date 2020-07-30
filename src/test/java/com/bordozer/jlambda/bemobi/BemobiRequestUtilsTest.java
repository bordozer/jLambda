package com.bordozer.jlambda.bemobi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class BemobiRequestUtilsTest {

    @Test
    void shouldThrowExceptionIfNoApiKeyParameter() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            BemobiRequestUtils.convertToBemobiParameters(Collections.emptyMap());
        });
    }
}
