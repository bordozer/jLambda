package com.bordozer.sms.provider.sdk.provider.bemobi;

import com.bordozer.sms.provider.sdk.Logger;
import com.bordozer.sms.provider.sdk.provider.bemobi.model.BemobiParameters;
import com.bordozer.sms.provider.sdk.provider.bemobi.model.BemobiRequest;
import com.bordozer.sms.provider.sdk.provider.bemobi.model.BemobiResponse;
import com.bordozer.sms.provider.sdk.utils.JsonUtils;
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

    public static final String SERVER_SCHEME = "http";
    public static final String SERVER_HOST = "bpx.bemobi.com";
    public static final int SERVER_PORT = 80;
    public static final String SERVER_PATH = "/opx/1.0/OPXSendSms";

    public static final String AUTH_STRING_PARAM = "AuthString";
    public static final String API_KEY_PARAM = "ApiKey";
    public static final String ACCOUNT_ID_PARAM = "AccountID";
    public static final String MSISDN_PARAM = "Msisdn";
    public static final String OPX_USER_ID_PARAM = "OPXUserID";
    public static final String SITE_ID_PARAM = "SiteID";
    public static final String MESSAGE_PARAM = "Message";
    public static final String CURRENT_TIME_PARAM = "CurrentTime";

    private static final int CONNECTION_TIMEOUT_MS = 20000;
    public static final String OPTIONAL_UNSPECIFIED_PARAM_VALUE = "unspecified";

    private final Logger logger;

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
                return JsonUtils.read(responseBody, BemobiResponse.class);
            }
        }
    }

    private static List<NameValuePair> getUrlParameters(final BemobiParameters bemobiParameters) {
        return bemobiParameters.asMap().entrySet().stream()
                .map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
