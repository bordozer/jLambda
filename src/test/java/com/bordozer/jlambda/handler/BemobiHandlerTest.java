package com.bordozer.jlambda.handler;

import com.bordozer.jlambda.model.BemobiResponse;
import com.bordozer.jlambda.utils.TestLambdaLogger;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.HashMap;

import static com.bordozer.commons.utils.FileUtils.readSystemResource;
import static com.bordozer.jlambda.bemobi.BemobiRequestUtils.API_KEY_PARAM;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BemobiHandlerTest {

    private static final BemobiResponse BEMOBI_RESPONSE = BemobiResponse.builder()
            .StatusCode(0)
            .Reason("Message sent successfully")
            .build();

    private static final String LAMBDA_EXPECTED_RESPONSE = String.format(
            readSystemResource("lambda-response-template.json"), 200, "{\"statusCode\":0,\"reason\":\"Message sent successfully\"}".replace("\"", "\\\"")
    );
    private static final String FAKE_API_KEY_HEX = "1056E0F39CD97BE9AE45A";

    @Test
    @SneakyThrows
    void shouldGetRemoteServiceResponse() {
        // given
        final var bemobiClient = mock(BemobiClient.class);
        when(bemobiClient.get(any())).thenReturn(BEMOBI_RESPONSE);

        final var requestParameters = new HashMap<String, String>();
        requestParameters.put(API_KEY_PARAM, FAKE_API_KEY_HEX);
        requestParameters.put("param", "value");

        // when
        final var handler = BemobiHandler.builder()
                .bemobiClient(bemobiClient)
                .logger(TestLambdaLogger.LAMBDA_LOGGER)
                .build();
        final var response = handler.handle(requestParameters);

        // then
        JSONAssert.assertEquals(LAMBDA_EXPECTED_RESPONSE, response.toJSONString(), false);
    }
}
