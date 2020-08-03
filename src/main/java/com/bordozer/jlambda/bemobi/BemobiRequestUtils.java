package com.bordozer.jlambda.bemobi;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BemobiRequestUtils {

    public static final String AUTH_STRING_PARAM = "AuthString";
    public static final String API_KEY_PARAM = "ApiKey";
    public static final String ACCOUNT_ID_PARAM = "AccountID";
    public static final String MSISDN_PARAM = "Msisdn";
    public static final String OPX_USER_ID_PARAM = "OPXUserID";
    public static final String SITE_ID_PARAM = "SiteID";
    public static final String MESSAGE_PARAM = "Message";
    public static final String CURRENT_TIME_PARAM = "CurrentTime";

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
}
