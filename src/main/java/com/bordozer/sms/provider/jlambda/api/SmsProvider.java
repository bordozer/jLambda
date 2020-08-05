package com.bordozer.sms.provider.jlambda.api;

import com.bordozer.sms.provider.jlambda.model.LambdaResponse;

import java.util.Map;

public interface SmsProvider {

    LambdaResponse handle(Map<String, String> requestParameters);
}
