package com.bordozer.sms.provider.jlambda.provider.bemobi;

import com.bordozer.sms.provider.jlambda.api.SmsProvider;
import com.bordozer.sms.provider.jlambda.model.LambdaResponse;
import com.bordozer.sms.provider.jlambda.utils.JsonUtils;
import com.bordozer.sms.provider.sdk.Logger;
import com.bordozer.sms.provider.sdk.provider.bemobi.BemobiClient;
import com.bordozer.sms.provider.sdk.provider.bemobi.model.BemobiRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import static com.bordozer.sms.provider.jlambda.provider.bemobi.BemobiResponseCodeConverter.convertToLambdaResponseCode;
import static com.bordozer.sms.provider.sdk.provider.bemobi.BemobiClient.SERVER_HOST;
import static com.bordozer.sms.provider.sdk.provider.bemobi.BemobiClient.SERVER_PATH;
import static com.bordozer.sms.provider.sdk.provider.bemobi.BemobiClient.SERVER_PORT;
import static com.bordozer.sms.provider.sdk.provider.bemobi.BemobiClient.SERVER_SCHEME;

@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BemobiProvider implements SmsProvider {

    public static final String HEALTH_CHECK = "health-check";

    @NonNull
    private final BemobiClient bemobiClient;
    @NonNull
    private final Logger logger;

    @Override
    public LambdaResponse handle(final Map<String, String> requestParameters) {
        final var smsProviderRequest = BemobiRequest.builder()
                .schema(SERVER_SCHEME)
                .host(SERVER_HOST)
                .port(SERVER_PORT)
                .path(SERVER_PATH)
                .parameters(BemobiParametersBuilder.build(requestParameters))
                .build();

        logger.log(String.format("Bemobi request: %s", JsonUtils.write(smsProviderRequest)));
        final var response = bemobiClient.get(smsProviderRequest);
        logger.log(String.format("Bemobi response: %s", JsonUtils.write(response)));

        return new LambdaResponse(convertToLambdaResponseCode(response.getStatusCode()), response);
    }
}
