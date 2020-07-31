package com.bordozer.jlambda.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.Collections;
import java.util.Map;

@Getter
@Builder
public class BemobiRequest {
    @NonNull
    private final String schema;
    @NonNull
    private final String host;
    @NonNull
    private final Integer port;
    @NonNull
    private final String path;
    @NonNull
    @Builder.Default
    private final Map<String, String> parameters = Collections.emptyMap();
}
