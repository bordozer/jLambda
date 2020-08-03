package com.bordozer.bemobi.sdk.utils;

import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class BemobiUtilsTest extends TestCase {

    private static final String FAKE_API_KEY_HEX = "1056E0F39CD97BE9AE45A";

    @Test
    void shouldCreateAuthString() {
        // given
        final var map = new HashMap<String, String>();
        map.put("p1", "v1");
        map.put("p2", "v2");

        // when
        final var authString = BemobiUtils.calculateAuthString(FAKE_API_KEY_HEX, map);

        //then
        assertThat(authString).isEqualTo("6492a9150f8c3fa7a5cf93c71c0de08ed9827962");
    }
}
