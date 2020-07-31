package com.bordozer.jlambda.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BemobiResponseCodeConverter {

    public static Integer convertToLambdaResponseCode(final Integer bemobiResponseCode) {
        switch (bemobiResponseCode) {
            case 0:
                return 200;
            case 112:
                return 400;
            case 1001:
                return 412;
            case 2000:
                return 417;
            default:
                throw new IllegalArgumentException(String.format("Unsupported Bemobi response code: %s", bemobiResponseCode));
        }
    }
}
