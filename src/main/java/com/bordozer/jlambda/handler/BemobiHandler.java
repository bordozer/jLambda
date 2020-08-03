package com.bordozer.jlambda.handler;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.bordozer.commons.utils.LoggableJson;
import com.bordozer.jlambda.model.BemobiRequest;
import com.bordozer.jlambda.model.LambdaResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import static com.bordozer.jlambda.bemobi.BemobiRequestUtils.convertToBemobiParameters;
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
        final BemobiRequest bemobiRequest = BemobiRequest.builder()
                .schema(SERVER_SCHEME)
                .host(SERVER_HOST)
                .port(SERVER_PORT)
                .path(SERVER_PATH)
                .parameters(convertToBemobiParameters(requestParameters))
                .build();

        logger.log(String.format("Bemobi request: %s", LoggableJson.of(bemobiRequest).toString()));
        final var response = bemobiClient.get(bemobiRequest);
        logger.log(String.format("Bemobi response: %s", LoggableJson.of(response).toString()));

        return new LambdaResponse(convertToLambdaResponseCode(response.getStatusCode()), response);
    }
}
