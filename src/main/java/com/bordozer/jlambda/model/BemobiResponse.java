package com.bordozer.jlambda.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Builder
@ToString
public class BemobiResponse implements Serializable {
    @NonNull
    private final Integer StatusCode; // TODO: name convention
    @NonNull
    private final String Reason;      // TODO: name convention
}
