package com.bordozer.jlambda.handler;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.bordozer.jlambda.model.BemobiRequest;
import com.bordozer.jlambda.model.BemobiParameters;
import com.bordozer.jlambda.model.BemobiResponse;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BemobiClient {

    private static final int CONNECTION_TIMEOUT_MS = 20000;

    private final LambdaLogger logger;

    @SneakyThrows
    public BemobiResponse get(final BemobiRequest request) {
        final URIBuilder builder = new URIBuilder();
        builder.setScheme(request.getSchema())
                .setHost(request.getHost())
                .setPort(request.getPort())
                .setPath(request.getPath())
                .setParameters(getUrlParameters(request.getParameters()));
        final URI uri = builder.build();
        logger.log(String.format("Bemobi request string: \"%s\"", uri.toString()));

        final RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(CONNECTION_TIMEOUT_MS)
                .setConnectTimeout(CONNECTION_TIMEOUT_MS)
                .setSocketTimeout(CONNECTION_TIMEOUT_MS)
                .build();

        final HttpGet httpGet = new HttpGet(uri);
        httpGet.setConfig(requestConfig);
        httpGet.addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        httpGet.addHeader(HttpHeaders.TIMEOUT, "20");

        try (final CloseableHttpClient httpClient = HttpClients.createDefault()) {
            try (final CloseableHttpResponse response = httpClient.execute(httpGet)) {
                final HttpEntity entity = response.getEntity();
                final var responseBody = EntityUtils.toString(entity);
                // TODO: deserialize ignoring case
                return new Gson().fromJson(responseBody, BemobiResponse.class);
            }
        }
    }

    private static List<NameValuePair> getUrlParameters(final BemobiParameters bemobiParameters) {
        return bemobiParameters.asMap().entrySet().stream()
                .map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
