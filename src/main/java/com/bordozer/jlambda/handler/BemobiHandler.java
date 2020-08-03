package com.bordozer.jlambda.handler;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.bordozer.commons.utils.LoggableJson;
import com.bordozer.jlambda.model.BemobiRequest;
import com.bordozer.jlambda.model.LambdaResponse;
import com.bordozer.jlambda.utils.CommonUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import static com.bordozer.jlambda.bemobi.BemobiRequestUtils.ACCOUNT_ID_PARAM;
import static com.bordozer.jlambda.bemobi.BemobiRequestUtils.API_KEY_PARAM;
import static com.bordozer.jlambda.bemobi.BemobiRequestUtils.MESSAGE_PARAM;
import static com.bordozer.jlambda.bemobi.BemobiRequestUtils.MSISDN_PARAM;
import static com.bordozer.jlambda.bemobi.BemobiRequestUtils.OPX_USER_ID_PARAM;
import static com.bordozer.jlambda.bemobi.BemobiRequestUtils.SITE_ID_PARAM;
import static com.bordozer.jlambda.bemobi.BemobiRequestUtils.calculateAuthString;
import static com.bordozer.jlambda.converter.BemobiResponseCodeConverter.convertToLambdaResponseCode;
import static com.bordozer.jlambda.handler.LambdaHandler.SERVER_HOST;
import static com.bordozer.jlambda.handler.LambdaHandler.SERVER_PATH;
import static com.bordozer.jlambda.handler.LambdaHandler.SERVER_PORT;
import static com.bordozer.jlambda.handler.LambdaHandler.SERVER_SCHEME;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class BemobiHandler {

    public static final String HEALTH_CHECK = "health-check";

    @NonNull
    private final BemobiClient bemobiClient;
    @NonNull
    private final LambdaLogger logger;

    public LambdaResponse handle(final Map<String, String> requestParameters) {
        final var bemobiRequest = BemobiRequest.builder()
                .schema(SERVER_SCHEME)
                .host(SERVER_HOST)
                .port(SERVER_PORT)
                .path(SERVER_PATH)
                .withParameters()
                .authString(calculateAuthString(requestParameters.get(API_KEY_PARAM), requestParameters))
                .accountId(requestParameters.get(ACCOUNT_ID_PARAM))
                .siteId(requestParameters.get(SITE_ID_PARAM))
                .msisdn(requestParameters.get(MSISDN_PARAM))
                .opxUserId(requestParameters.get(OPX_USER_ID_PARAM))
                .message(requestParameters.get(MESSAGE_PARAM))
                .currentTime(String.valueOf(CommonUtils.getCurrentEpochTime()))
                .build();

        logger.log(String.format("Bemobi request: %s", LoggableJson.of(bemobiRequest).toString()));
        final var response = bemobiClient.get(bemobiRequest);
        logger.log(String.format("Bemobi response: %s", LoggableJson.of(response).toString()));

        return new LambdaResponse(convertToLambdaResponseCode(response.getStatusCode()), response);
    }
}
