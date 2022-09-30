package com.jimmyworks.easyhttp.builder;

import com.jimmyworks.easyhttp.entity.RequestInfo;
import com.jimmyworks.easyhttp.service.DoRequestService;
import com.jimmyworks.easyhttp.type.HttpMethod;
import com.jimmyworks.easyhttp.utils.JsonUtils;
import lombok.NonNull;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Request建構器(包含body層)
 *
 * @author Jimmy Kang
 */
public class BodyRequestBuilder extends RequestBuilder {

    private String contentType = "application/json";
    private String requestBodyString = "";
    private RequestBody requestBody = null;

    public BodyRequestBuilder(@NonNull String url) {
        super(url, HttpMethod.POST);
    }

    public BodyRequestBuilder(@NonNull String url, @NonNull HttpMethod httpMethod) {
        super(url, httpMethod);
    }

    @Override
    public BodyRequestBuilder tag(@NonNull String tag) {
        super.tag(tag);
        return this;
    }

    @Override
    public BodyRequestBuilder addHeader(@NonNull String key, @NonNull String value) {
        super.addHeader(key, value);
        return this;
    }

    @Override
    public BodyRequestBuilder headers(@NonNull Map<String, String> headersMap) {
        super.headers(headersMap);
        return this;
    }

    @Override
    public BodyRequestBuilder addUrlParam(@NonNull String key, @NonNull String value) {
        super.addUrlParam(key, value);
        return this;
    }

    @Override
    public BodyRequestBuilder urlParams(@NonNull Map<String, String> urlParamsMap) {
        super.urlParams(urlParamsMap);
        return this;
    }

    @Override
    public BodyRequestBuilder connectTimeout(long timeout, @NonNull TimeUnit timeUnit) {
        super.connectTimeout(timeout, timeUnit);
        return this;
    }

    @Override
    public BodyRequestBuilder readTimeout(long timeout, @NonNull TimeUnit timeUnit) {
        super.readTimeout(timeout, timeUnit);
        return this;
    }

    @Override
    public BodyRequestBuilder writeTimeout(long timeout, @NonNull TimeUnit timeUnit) {
        super.writeTimeout(timeout, timeUnit);
        return this;
    }

    public BodyRequestBuilder stringBody(String string) {
        contentType = "text/plain";
        this.requestBodyString = string;
        return this;
    }

    public BodyRequestBuilder stringBody(String contentType, String string) {
        this.contentType = contentType;
        this.requestBodyString = string;
        return this;
    }

    public BodyRequestBuilder jsonBody(Object obj) {
        this.contentType = "application/json";
        this.requestBodyString = JsonUtils.toJson(obj);
        return this;
    }

    public BodyRequestBuilder jsonBody(String contentType, Object obj) {
        this.contentType = contentType;
        this.requestBodyString = JsonUtils.toJson(obj);
        return this;
    }

    public BodyRequestBuilder formBody(Map<String, String> formMap) {
        FormBody.Builder builder = new FormBody.Builder(Charset.defaultCharset());
        for (Map.Entry<String, String> entry : formMap.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        this.requestBody = builder.build();
        return this;
    }

    public BodyRequestBuilder formBody(FormBody formBody) {
        this.requestBody = formBody;
        return this;
    }

    public BodyRequestBuilder requestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    private RequestInfo buildRequestInfo() {
        requestBuilder.url(buildUrl());

        if (null != requestBody || !httpMethod.getHaveBody()) {
            return new RequestInfo(
                    requestBuilder.method(httpMethod.getCode(), requestBody).build());
        } else {
            return new RequestInfo(
                    requestBuilder
                            .method(
                                    httpMethod.getCode(),
                                    RequestBody.create(
                                            requestBodyString, MediaType.parse(contentType)))
                            .build());
        }
    }

    @Override
    public DoRequestService build() {
        return new DoRequestService(getOkHttpClientBuilder().build(), buildRequestInfo());
    }
}
