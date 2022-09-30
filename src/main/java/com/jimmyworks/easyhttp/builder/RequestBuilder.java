package com.jimmyworks.easyhttp.builder;

import com.jimmyworks.easyhttp.entity.RequestInfo;
import com.jimmyworks.easyhttp.service.DoRequestService;
import com.jimmyworks.easyhttp.type.HttpMethod;
import lombok.Getter;
import lombok.NonNull;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Request建構器
 *
 * @author Jimmy Kang
 */
public class RequestBuilder {

    protected final String url;
    protected final HttpMethod httpMethod;
    protected final Request.Builder requestBuilder = new Request.Builder();
    protected final Map<String, String> urlParamsMap = new HashMap<>();

    @Getter private final OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

    public RequestBuilder(@NonNull String url) {
        this.url = url;
        this.httpMethod = HttpMethod.GET;
        initOkHttpClientBuilder();
    }

    public RequestBuilder(@NonNull String url, @NonNull HttpMethod httpMethod) {
        this.url = url;
        this.httpMethod = httpMethod;
        initOkHttpClientBuilder();
    }

    private void initOkHttpClientBuilder() {
        okHttpClientBuilder
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);
    }

    public RequestBuilder tag(@NonNull String tag) {
        this.requestBuilder.tag(tag);
        return this;
    }

    public RequestBuilder addHeader(@NonNull String key, @NonNull String value) {
        this.requestBuilder.header(key, value);
        return this;
    }

    public RequestBuilder headers(@NonNull Map<String, String> headersMap) {
        headersMap.forEach(this.requestBuilder::header);
        return this;
    }

    public RequestBuilder addUrlParam(@NonNull String key, @NonNull String value) {
        this.urlParamsMap.put(key, value);
        return this;
    }

    public RequestBuilder urlParams(@NonNull Map<String, String> urlParamsMap) {
        this.urlParamsMap.putAll(urlParamsMap);
        return this;
    }

    public RequestBuilder connectTimeout(long timeout, @NonNull TimeUnit timeUnit) {
        this.okHttpClientBuilder.connectTimeout(timeout, timeUnit);
        return this;
    }

    public RequestBuilder readTimeout(long timeout, @NonNull TimeUnit timeUnit) {
        this.okHttpClientBuilder.readTimeout(timeout, timeUnit);
        return this;
    }

    public RequestBuilder writeTimeout(long timeout, @NonNull TimeUnit timeUnit) {
        this.okHttpClientBuilder.writeTimeout(timeout, timeUnit);
        return this;
    }

    protected String buildUrl() {
        StringBuilder urlBuilder = new StringBuilder(this.url);
        int i = 0;
        for (Map.Entry<String, String> entry : this.urlParamsMap.entrySet()) {
            if (i == 0) {
                urlBuilder.append("?");
            }
            urlBuilder.append(entry.getKey()).append("=").append(entry.getValue());

            if (i != urlParamsMap.size() - 1) {
                urlBuilder.append("&");
            }
            i++;
        }
        return urlBuilder.toString();
    }

    public DoRequestService build() {
        Request request = requestBuilder.url(buildUrl()).method(httpMethod.getCode(), null).build();
        return new DoRequestService(okHttpClientBuilder.build(), new RequestInfo(request));
    }
}
