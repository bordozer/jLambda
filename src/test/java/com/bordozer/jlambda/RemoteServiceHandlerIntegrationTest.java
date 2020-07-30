package com.bordozer.jlambda;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.HashMap;
import java.util.Map;

import static com.bordozer.commons.utils.FileUtils.readSystemResource;

class RemoteServiceHandlerIntegrationTest {

    private static final String SERVER_URL = "http://bpx.bemobi.com/opx/1.0/OPXSendSms";
    private static final int SERVER_PORT = 80;

    private static final String REMOTE_SERVICE_EXPECTED_RESPONSE = readSystemResource("remote-service-response.json");

    @Test
    @SneakyThrows
    void shouldGetLambdaResponse() {
        // given
        final Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        // when
        final var response = RemoteServiceHandler.get(SERVER_URL, SERVER_PORT, map);

        // then
        JSONAssert.assertEquals(REMOTE_SERVICE_EXPECTED_RESPONSE, response, false);
    }
}
