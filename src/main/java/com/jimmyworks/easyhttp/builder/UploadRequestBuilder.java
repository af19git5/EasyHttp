package com.jimmyworks.easyhttp.builder;

import com.jimmyworks.easyhttp.entity.RequestInfo;
import com.jimmyworks.easyhttp.service.DoRequestService;
import com.jimmyworks.easyhttp.type.HttpMethod;
import com.jimmyworks.easyhttp.utils.FileUtils;
import lombok.NonNull;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 檔案上傳建構器
 *
 * @author Jimmy Kang
 */
public class UploadRequestBuilder extends RequestBuilder {

    private final MultipartBody.Builder multipartBodyBuilder =
            new MultipartBody.Builder().setType(MediaType.get("multipart/form-data"));

    public UploadRequestBuilder(@NonNull String url) {
        super(url, HttpMethod.POST);
    }

    public UploadRequestBuilder(@NonNull String url, @NonNull HttpMethod httpMethod) {
        super(url, httpMethod);
    }

    @Override
    public UploadRequestBuilder tag(@NonNull String tag) {
        super.tag(tag);
        return this;
    }

    @Override
    public UploadRequestBuilder addHeader(@NonNull String key, @NonNull String value) {
        super.addHeader(key, value);
        return this;
    }

    @Override
    public UploadRequestBuilder headers(@NonNull Map<String, String> headersMap) {
        super.headers(headersMap);
        return this;
    }

    @Override
    public UploadRequestBuilder addUrlParam(@NonNull String key, @NonNull String value) {
        super.addUrlParam(key, value);
        return this;
    }

    @Override
    public UploadRequestBuilder urlParams(@NonNull Map<String, String> urlParamsMap) {
        super.urlParams(urlParamsMap);
        return this;
    }

    @Override
    public UploadRequestBuilder connectTimeout(long timeout, @NonNull TimeUnit timeUnit) {
        super.connectTimeout(timeout, timeUnit);
        return this;
    }

    @Override
    public UploadRequestBuilder readTimeout(long timeout, @NonNull TimeUnit timeUnit) {
        super.readTimeout(timeout, timeUnit);
        return this;
    }

    @Override
    public UploadRequestBuilder writeTimeout(long timeout, @NonNull TimeUnit timeUnit) {
        super.writeTimeout(timeout, timeUnit);
        return this;
    }

    public UploadRequestBuilder contentType(String contentType) {
        MediaType mediaType = MediaType.parse(contentType);
        if (null != mediaType) {
            multipartBodyBuilder.setType(mediaType);
        }
        return this;
    }

    public UploadRequestBuilder addMultipartParam(String key, String value) {
        multipartBodyBuilder.addFormDataPart(key, value);
        return this;
    }

    public UploadRequestBuilder addMultipartFile(String key, String fileName, byte[] bytes) {
        multipartBodyBuilder.addFormDataPart(
                key, fileName, RequestBody.create(bytes, FileUtils.fileNameToMediaType(fileName)));
        return this;
    }

    public UploadRequestBuilder addMultipartFile(String key, File file) {
        multipartBodyBuilder.addFormDataPart(
                key, file.getName(), RequestBody.create(file, FileUtils.fileToMediaType(file)));
        return this;
    }

    private RequestInfo buildRequestInfo() {
        requestBuilder.url(buildUrl());
        return new RequestInfo(
                requestBuilder.method(httpMethod.getCode(), multipartBodyBuilder.build()).build());
    }

    public DoRequestService build() {
        return new DoRequestService(getOkHttpClientBuilder().build(), buildRequestInfo());
    }
}
