package com.bordozer.jlambda;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import static com.bordozer.commons.utils.FileUtils.readSystemResource;
import static org.assertj.core.api.Assertions.assertThat;

class RemoteServiceHandlerIntegrationTest {

    private static final String SCHEME = "http";
    private static final String SERVER_URL = "bpx.bemobi.com/opx/1.0/OPXSendSms";
    private static final int SERVER_PORT = 80;

    private static final String REMOTE_SERVICE_EXPECTED_RESPONSE = readSystemResource("remote-service-response.json");

    @Test
    @SneakyThrows
    void shouldGetBemobiServiceResponse() {
        // given
        final var currentTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        final Map<String, String> map = new HashMap<>();
        map.put("AccountID", "202080231122");
        map.put("CurrentTime", String.valueOf(currentTime));
        map.put("Message", "Hi from lambda");
        map.put("Msisdn", "+380673201664");
        map.put("OPXUserID", "unspecified");
        map.put("SiteID", "402080231693");
        map.put("AuthString", "187d5267961c595ffa5c7c03bd72ed00758faa10\n");

        // when
        final var response = RemoteServiceHandler.get(SCHEME, SERVER_URL, SERVER_PORT, map);

        // then
        assertThat(response.getResponseCode()).isEqualTo(200);
        JSONAssert.assertEquals(REMOTE_SERVICE_EXPECTED_RESPONSE, response.getResponseBody(), false);
    }
}
