package com.bordozer.jlambda.handler;

import com.bordozer.bemobi.sdk.BemobiClient;
import com.bordozer.bemobi.sdk.Logger;
import com.bordozer.bemobi.sdk.model.BemobiRequest;
import com.bordozer.jlambda.model.LambdaResponse;
import com.bordozer.jlambda.utils.BemobiParametersBuilder;
import com.bordozer.jlambda.utils.JsonUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import static com.bordozer.bemobi.sdk.BemobiClient.SERVER_HOST;
import static com.bordozer.bemobi.sdk.BemobiClient.SERVER_PATH;
import static com.bordozer.bemobi.sdk.BemobiClient.SERVER_PORT;
import static com.bordozer.bemobi.sdk.BemobiClient.SERVER_SCHEME;
import static com.bordozer.jlambda.converter.BemobiResponseCodeConverter.convertToLambdaResponseCode;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class BemobiHandler {

    public static final String HEALTH_CHECK = "health-check";

    @NonNull
    private final BemobiClient bemobiClient;
    @NonNull
    private final Logger logger;

    public LambdaResponse handle(final Map<String, String> requestParameters) {
        final BemobiRequest bemobiRequest = BemobiRequest.builder()
                .schema(SERVER_SCHEME)
                .host(SERVER_HOST)
                .port(SERVER_PORT)
                .path(SERVER_PATH)
                .parameters(BemobiParametersBuilder.build(requestParameters))
                .build();

        logger.log(String.format("Bemobi request: %s", JsonUtils.write(bemobiRequest)));
        final var response = bemobiClient.get(bemobiRequest);
        logger.log(String.format("Bemobi response: %s", JsonUtils.write(response)));

        return new LambdaResponse(convertToLambdaResponseCode(response.getStatusCode()), response);
    }
}
