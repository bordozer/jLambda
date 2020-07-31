package com.bordozer.jlambda.handler;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.bordozer.commons.utils.LoggableJson;
import com.bordozer.jlambda.model.LambdaResponse;
import com.bordozer.jlambda.model.RemoteServiceRequest;
import lombok.Builder;
import lombok.NonNull;

@Builder
public class BemobiHandler {

    public static final String HEALTH_CHECK = "health-check";

    @NonNull
    private final RemoteServiceRequest serviceRequest;
    @NonNull
    private final LambdaLogger logger;

    public LambdaResponse handle() {
        final var response = new BemobiClient(logger).get(serviceRequest);
        logger.log(String.format("Bemobi service response: %s", LoggableJson.of(response).toString()));

        return new LambdaResponse(response.getResponseCode(), response.getResponseBody());
    }
}
