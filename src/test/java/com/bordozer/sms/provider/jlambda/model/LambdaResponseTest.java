package com.bordozer.sms.provider.jlambda.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LambdaResponseTest {

    @Test
    void lambdaResponseBodyShouldBeString() {
        // given
        final var response = new TestBody();

        // when
        final var actual = new LambdaResponse(200, response);

        // then
        assertThat(actual.get("body").getClass()).isEqualTo(String.class);
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    private static class TestBody {

    }
}
