package com.bordozer.jlambda.handler;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.bordozer.commons.utils.LoggableJson;
import com.bordozer.jlambda.model.BemobiRequest;
import com.bordozer.jlambda.model.LambdaResponse;
import lombok.Builder;
import lombok.NonNull;

@Builder
public class BemobiHandler {

    public static final String HEALTH_CHECK = "health-check";

    @NonNull
    private final BemobiRequest serviceRequest;
    @NonNull
    private final LambdaLogger logger;

    public LambdaResponse handle() {
        final var response = new BemobiClient(logger).get(serviceRequest);
        logger.log(String.format("Bemobi service response: %s", LoggableJson.of(response).toString()));

        return new LambdaResponse(getLambdaResponseCode(response.getStatusCode()), response.getReason());
    }

    private Integer getLambdaResponseCode(final Integer responseCode) {
        switch (responseCode) {
            case 0:
                return 200;
            case 112:
                return 400;
            case 1001:
                return 412;
            case 2000:
                return 417;
            default:
                throw new IllegalArgumentException(String.format("Unsupported Bemobi response code: %s", responseCode));
        }
    }
}
