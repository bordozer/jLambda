package com.bordozer.jlambda.utils;

import com.bordozer.bemobi.sdk.BemobiClient;
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

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BemobiRequestUtils {

    public static BemobiParameters convertToBemobiParameters(final Map<String, String> parametersMap) {

        final var map = new HashMap<String, String>();
        map.put(BemobiClient.ACCOUNT_ID_PARAM, parametersMap.get(BemobiClient.ACCOUNT_ID_PARAM));
        map.put(BemobiClient.MSISDN_PARAM, parametersMap.get(BemobiClient.MSISDN_PARAM));
        map.put(BemobiClient.OPX_USER_ID_PARAM, parametersMap.get(BemobiClient.OPX_USER_ID_PARAM));
        map.put(BemobiClient.SITE_ID_PARAM, parametersMap.get(BemobiClient.SITE_ID_PARAM));
        map.put(BemobiClient.MESSAGE_PARAM, parametersMap.get(BemobiClient.MESSAGE_PARAM));
        map.put(BemobiClient.CURRENT_TIME_PARAM, String.valueOf(CommonUtils.getCurrentEpochTime()));

        final String authString = calculateAuthString(parametersMap.get(BemobiClient.API_KEY_PARAM), map);
        map.put(BemobiClient.AUTH_STRING_PARAM, authString);

        return BemobiParameters.builder()
                .authString(map.get(BemobiClient.AUTH_STRING_PARAM))
                .accountId(map.get(BemobiClient.ACCOUNT_ID_PARAM))
                .siteId(map.get(BemobiClient.SITE_ID_PARAM))
                .msisdn(map.get(BemobiClient.MSISDN_PARAM))
                .opxUserId(map.get(BemobiClient.OPX_USER_ID_PARAM))
                .message(map.get(BemobiClient.MESSAGE_PARAM))
                .currentTime(map.get(BemobiClient.CURRENT_TIME_PARAM))
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
            return BemobiClient.OPTIONAL_UNSPECIFIED_PARAM_VALUE;
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
