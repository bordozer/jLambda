package com.bordozer.jlambda.utils;

import com.bordozer.bemobi.sdk.model.BemobiParameters;
import com.bordozer.bemobi.sdk.utils.CommonUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import static com.bordozer.bemobi.sdk.BemobiClient.ACCOUNT_ID_PARAM;
import static com.bordozer.bemobi.sdk.BemobiClient.API_KEY_PARAM;
import static com.bordozer.bemobi.sdk.BemobiClient.AUTH_STRING_PARAM;
import static com.bordozer.bemobi.sdk.BemobiClient.CURRENT_TIME_PARAM;
import static com.bordozer.bemobi.sdk.BemobiClient.MESSAGE_PARAM;
import static com.bordozer.bemobi.sdk.BemobiClient.MSISDN_PARAM;
import static com.bordozer.bemobi.sdk.BemobiClient.OPX_USER_ID_PARAM;
import static com.bordozer.bemobi.sdk.BemobiClient.SITE_ID_PARAM;
import static com.bordozer.bemobi.sdk.utils.BemobiUtils.calculateAuthString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BemobiParametersBuilder {

    public static BemobiParameters build(final Map<String, String> requestParameters) {
        final var bemobiParameters = extractBemobiParameters(requestParameters);

        final String authString = calculateAuthString(requestParameters.get(API_KEY_PARAM), bemobiParameters);
        bemobiParameters.put(AUTH_STRING_PARAM, authString);

        return BemobiParameters.builder()
                .authString(bemobiParameters.get(AUTH_STRING_PARAM))
                .accountId(bemobiParameters.get(ACCOUNT_ID_PARAM))
                .siteId(bemobiParameters.get(SITE_ID_PARAM))
                .msisdn(bemobiParameters.get(MSISDN_PARAM))
                .opxUserId(bemobiParameters.get(OPX_USER_ID_PARAM))
                .message(bemobiParameters.get(MESSAGE_PARAM))
                .currentTime(bemobiParameters.get(CURRENT_TIME_PARAM))
                .build();
    }

    private static Map<String, String> extractBemobiParameters(final Map<String, String> requestParameters) {
        final var map = new HashMap<String, String>();
        map.put(ACCOUNT_ID_PARAM, requestParameters.get(ACCOUNT_ID_PARAM));
        map.put(MSISDN_PARAM, requestParameters.get(MSISDN_PARAM));
        map.put(OPX_USER_ID_PARAM, requestParameters.get(OPX_USER_ID_PARAM));
        map.put(SITE_ID_PARAM, requestParameters.get(SITE_ID_PARAM));
        map.put(MESSAGE_PARAM, requestParameters.get(MESSAGE_PARAM));
        map.put(CURRENT_TIME_PARAM, String.valueOf(CommonUtils.getCurrentEpochTime()));

        return map;
    }
}
