package com.bordozer.jlambda.api;

import com.bordozer.jlambda.model.LambdaResponse;

import java.util.Map;

public interface SmsProvider {

    LambdaResponse handle(Map<String, String> requestParameters);
}
