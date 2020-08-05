package com.bordozer.sms.provider.sdk.provider.bemobi.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.bordozer.sms.provider.sdk.provider.bemobi.BemobiClient.ACCOUNT_ID_PARAM;
import static com.bordozer.sms.provider.sdk.provider.bemobi.BemobiClient.AUTH_STRING_PARAM;
import static com.bordozer.sms.provider.sdk.provider.bemobi.BemobiClient.CURRENT_TIME_PARAM;
import static com.bordozer.sms.provider.sdk.provider.bemobi.BemobiClient.MESSAGE_PARAM;
import static com.bordozer.sms.provider.sdk.provider.bemobi.BemobiClient.MSISDN_PARAM;
import static com.bordozer.sms.provider.sdk.provider.bemobi.BemobiClient.OPX_USER_ID_PARAM;
import static com.bordozer.sms.provider.sdk.provider.bemobi.BemobiClient.SITE_ID_PARAM;

@Getter
@Builder
public class BemobiParameters {
    @NonNull
    private final String authString;
    @NonNull
    private final String accountId;
    @NonNull
    private final String siteId;
    @NonNull
    private final String msisdn;
    @NonNull
    private final String opxUserId;
    @NonNull
    private final String message;
    @NonNull
    private final String currentTime;

    public Map<String, String> asMap() {
        final var map = new LinkedHashMap<String, String>();
        map.put(AUTH_STRING_PARAM, authString);
        map.put(ACCOUNT_ID_PARAM, accountId);
        map.put(SITE_ID_PARAM, siteId);
        map.put(MSISDN_PARAM, msisdn);
        map.put(OPX_USER_ID_PARAM, opxUserId);
        map.put(MESSAGE_PARAM, message);
        map.put(CURRENT_TIME_PARAM, currentTime);
        return map;
    }
}
