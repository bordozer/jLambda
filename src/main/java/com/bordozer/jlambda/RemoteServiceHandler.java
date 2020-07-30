package com.bordozer.jlambda;

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
import java.util.Map;
import java.util.stream.Collectors;

public class RemoteServiceHandler {

    public static final String PATH = "/api/health-check";
    private static final int CONNECTION_TIMEOUT_MS = 20000;

    @SneakyThrows
    static String get(final String scheme, final String serverUrl, final int serverPort, final Map<String, String> httpParametersMap) {

        final List<NameValuePair> urlParameters = getParameters(httpParametersMap);

        final URIBuilder builder = new URIBuilder();
        builder.setScheme(scheme)
                .setHost(serverUrl)
                .setPort(serverPort)
                .setPath(PATH)
                .setParameters(urlParameters);
        final URI uri = builder.build();

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
                return EntityUtils.toString(entity);
            }
        }
    }

    private static List<NameValuePair> getParameters(final Map<String, String> httpParametersMap) {
        return httpParametersMap.keySet().stream()
                .map(parameter -> new BasicNameValuePair(parameter, httpParametersMap.get(parameter)))
                .collect(Collectors.toList());
    }
}
