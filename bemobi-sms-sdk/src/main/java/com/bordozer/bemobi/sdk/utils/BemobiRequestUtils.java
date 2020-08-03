package com.bordozer.bemobi.sdk.utils;

import com.bordozer.bemobi.sdk.model.BemobiParameters;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.bordozer.bemobi.sdk.BemobiClient.ACCOUNT_ID_PARAM;
import static com.bordozer.bemobi.sdk.BemobiClient.API_KEY_PARAM;
import static com.bordozer.bemobi.sdk.BemobiClient.AUTH_STRING_PARAM;
import static com.bordozer.bemobi.sdk.BemobiClient.CURRENT_TIME_PARAM;
import static com.bordozer.bemobi.sdk.BemobiClient.MESSAGE_PARAM;
import static com.bordozer.bemobi.sdk.BemobiClient.MSISDN_PARAM;
import static com.bordozer.bemobi.sdk.BemobiClient.OPTIONAL_UNSPECIFIED_PARAM_VALUE;
import static com.bordozer.bemobi.sdk.BemobiClient.OPX_USER_ID_PARAM;
import static com.bordozer.bemobi.sdk.BemobiClient.SITE_ID_PARAM;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BemobiRequestUtils {

    public static BemobiParameters convertToBemobiParameters(final Map<String, String> requestParameters) {

        final var map = new HashMap<String, String>();
        map.put(ACCOUNT_ID_PARAM, requestParameters.get(ACCOUNT_ID_PARAM));
        map.put(MSISDN_PARAM, requestParameters.get(MSISDN_PARAM));
        map.put(OPX_USER_ID_PARAM, requestParameters.get(OPX_USER_ID_PARAM));
        map.put(SITE_ID_PARAM, requestParameters.get(SITE_ID_PARAM));
        map.put(MESSAGE_PARAM, requestParameters.get(MESSAGE_PARAM));
        map.put(CURRENT_TIME_PARAM, String.valueOf(CommonUtils.getCurrentEpochTime()));

        final String authString = calculateAuthString(requestParameters.get(API_KEY_PARAM), map);
        map.put(AUTH_STRING_PARAM, authString);

        return BemobiParameters.builder()
                .authString(map.get(AUTH_STRING_PARAM))
                .accountId(map.get(ACCOUNT_ID_PARAM))
                .siteId(map.get(SITE_ID_PARAM))
                .msisdn(map.get(MSISDN_PARAM))
                .opxUserId(map.get(OPX_USER_ID_PARAM))
                .message(map.get(MESSAGE_PARAM))
                .currentTime(map.get(CURRENT_TIME_PARAM))
                .build();
    }

    public static String calculateAuthString(final String apiKey, final Map<String, String> map) {
        final String concatenatedParameters = map
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> String.format("%s%s", entry.getKey().toLowerCase(), convertValue(entry.getValue())))
                .collect(Collectors.joining());

        final byte[] bemobiKeyBytes = convertHexStr2Bytes(apiKey);
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_1, bemobiKeyBytes).hmacHex(concatenatedParameters);
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
