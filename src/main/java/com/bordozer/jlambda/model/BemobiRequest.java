package com.bordozer.jlambda.model;

import com.bordozer.jlambda.utils.CommonUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.bordozer.jlambda.bemobi.BemobiRequestUtils.ACCOUNT_ID_PARAM;
import static com.bordozer.jlambda.bemobi.BemobiRequestUtils.AUTH_STRING_PARAM;
import static com.bordozer.jlambda.bemobi.BemobiRequestUtils.CURRENT_TIME_PARAM;
import static com.bordozer.jlambda.bemobi.BemobiRequestUtils.MESSAGE_PARAM;
import static com.bordozer.jlambda.bemobi.BemobiRequestUtils.MSISDN_PARAM;
import static com.bordozer.jlambda.bemobi.BemobiRequestUtils.OPX_USER_ID_PARAM;
import static com.bordozer.jlambda.bemobi.BemobiRequestUtils.SITE_ID_PARAM;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BemobiRequest {
    private String schema;
    private String host;
    private Integer port;
    private String path;
    private final BemobiParameters bemobiParameters = new BemobiParameters();

    public static BemobiRequest builder() {
        return new BemobiRequest();
    }

    public BemobiRequest schema(final String schema) {
        this.schema = schema;
        return this;
    }

    public BemobiRequest host(final String host) {
        this.host = host;
        return this;
    }

    public BemobiRequest port(final int port) {
        this.port = port;
        return this;
    }

    public BemobiRequest path(final String path) {
        this.path = path;
        return this;
    }

    public BemobiParameters withParameters() {
        return bemobiParameters;
    }

    @Getter
    public class BemobiParameters {
        private String authString;
        private String accountId;
        private String siteId;
        private String msisdn;
        private String opxUserId;
        private String message;
        private String currentTime;

        public BemobiParameters authString(final String authString) {
            this.authString = authString;
            return this;
        }

        public BemobiParameters accountId(final String accountId) {
            this.accountId = accountId;
            return this;
        }

        public BemobiParameters siteId(final String siteId) {
            this.siteId = siteId;
            return this;
        }

        public BemobiParameters msisdn(final String msisdn) {
            this.msisdn = msisdn;
            return this;
        }

        public BemobiParameters opxUserId(final String opxUserId) {
            this.opxUserId = opxUserId;
            return this;
        }

        public BemobiParameters message(final String message) {
            this.message = message;
            return this;
        }

        public BemobiParameters currentTime(final String currentTime) {
            this.currentTime = currentTime;
            return this;
        }

        public BemobiRequest build() {
            return BemobiRequest.this;
        }

        public Map<String, String> asMap() {
            final var map = new LinkedHashMap<String, String>();
            map.put(AUTH_STRING_PARAM, authString);
            map.put(ACCOUNT_ID_PARAM, accountId);
            map.put(SITE_ID_PARAM, siteId);
            map.put(MSISDN_PARAM, msisdn);
            map.put(OPX_USER_ID_PARAM, opxUserId);
            map.put(MESSAGE_PARAM, message);
            map.put(CURRENT_TIME_PARAM, String.valueOf(CommonUtils.getCurrentEpochTime()));
            return map;
        }
    }
}
