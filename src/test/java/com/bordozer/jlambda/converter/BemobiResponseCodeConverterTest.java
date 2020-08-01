package com.bordozer.jlambda.converter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class BemobiResponseCodeConverterTest {

    static Stream<Arguments> dataSupplier() {
        return Stream.of(
                Arguments.of(0, 200),
                Arguments.of(112, 400),
                Arguments.of(1001, 412),
                Arguments.of(2000, 417)
        );
    }

    @DisplayName("Should convert bemobi response code to lambda one")
    @ParameterizedTest
    @MethodSource("dataSupplier")
    void shouldConvert0To200(final Integer bemobiCode, final Integer lambdaCode) {
        assertThat(BemobiResponseCodeConverter.convertToLambdaResponseCode(bemobiCode)).isEqualTo(lambdaCode);
    }
}
