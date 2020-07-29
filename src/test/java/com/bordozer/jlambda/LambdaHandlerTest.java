package com.bordozer.jlambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaRuntime;
import com.bordozer.commons.utils.FileUtils;
import lombok.SneakyThrows;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LambdaHandlerTest {

    private static final String EXPECTED_RESPONSE = FileUtils.readSystemResource("lambda-expected-response.json");

    @Test
    @SneakyThrows
    void shouldGetLambdaResponse() {
        // given
        final LambdaHandler lambdaHandler = new LambdaHandler();
        final Context ctx = createContext();

        final Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        // when
        final JSONObject jsonObject = lambdaHandler.handleRequest(map, ctx);

        // then
        JSONAssert.assertEquals(EXPECTED_RESPONSE, jsonObject.toJSONString(), false);
    }

    private Context createContext() {
        final Context context = mock(Context.class);
        when(context.getLogger()).thenReturn(LambdaRuntime.getLogger());
        return context;
    }
}
