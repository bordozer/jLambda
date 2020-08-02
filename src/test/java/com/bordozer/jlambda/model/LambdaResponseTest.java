package com.bordozer.jlambda.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LambdaResponseTest {

    @Test
    void lambdaResponseBodyShouldBeString() {
        // given
        final var response = new BemobiResponse(200, "A reason");

        // when
        final var actual = new LambdaResponse(200, response);

        // then
        assertThat(actual.get("body").getClass()).isEqualTo(String.class);
    }
}
