package com.bordozer.jlambda;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BemobiRequestUtils {

    private static final String OPTIONAL_UNSPECIFIED_PARAM_VALUE = "unspecified";

    public static String calculateAuthString(final String apiKey, final Map<String, String> map) {
        final String concatenatedParameters = map
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> String.format("%s%s", entry.getKey().toLowerCase(), convertValue(entry.getValue())))
                .collect(Collectors.joining());

        final byte[] bemobiKeyBytes = convertHexStr2Bytes(apiKey);
        final String authString = new HmacUtils(HmacAlgorithms.HMAC_SHA_1, bemobiKeyBytes).hmacHex(concatenatedParameters);
        return authString;
    }

    private static String convertValue(final String value) {
        if (StringUtils.isBlank(value)) {
            return OPTIONAL_UNSPECIFIED_PARAM_VALUE;
        }
        return value.toLowerCase();
    }

    private static byte[] convertHexStr2Bytes(final String hex) {
        // Adding one byte to get the right conversion
        // Values starting with "0" can be converted
        byte[] bArray = new BigInteger("10" + hex, 16).toByteArray();

        // Copy all the REAL bytes, not the "first"
        byte[] ret = new byte[bArray.length - 1];
        if (ret.length >= 0) {
            System.arraycopy(bArray, 1, ret, 0, ret.length);
        }

        return ret;
    }

    public static long getCurrentEpochTime() {
        return LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
