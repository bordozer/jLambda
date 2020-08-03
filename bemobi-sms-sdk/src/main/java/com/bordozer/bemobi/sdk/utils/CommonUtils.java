package com.bordozer.bemobi.sdk.utils;

import com.google.common.io.Resources;
import com.google.gson.Gson;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.net.URL;
import java.nio.charset.StandardCharsets;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommonUtils {

    @SneakyThrows
    public static String readResource(final String name) {
        final URL url = Resources.getResource(name);
        return Resources.toString(url, StandardCharsets.UTF_8);
    }

    public static String toJson(final Object obj) {
        return new Gson().toJson(obj);
    }
}
