package com.jimmyworks.easyhttp.entity;

import lombok.Getter;
import lombok.NonNull;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author Jimmy Kang
 */
@Getter
public class RequestInfo {

    private final String url;
    private final String contentType;
    private final Headers headers;
    private final Request request;

    public RequestInfo(@NonNull Request request) {
        this.url = request.url().toString();
        this.headers = request.headers();
        this.request = request;
        RequestBody requestBody = request.body();
        if (null == requestBody) {
            this.contentType = null;
        } else {
            MediaType mediaType = requestBody.contentType();
            if (null == mediaType) {
                this.contentType = null;
            } else {
                this.contentType = mediaType.toString();
            }
        }
    }
}
