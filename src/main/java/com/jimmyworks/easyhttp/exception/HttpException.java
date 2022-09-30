package com.jimmyworks.easyhttp.exception;

import lombok.NonNull;

/**
 * 類別無對應錯誤
 *
 * @author Jimmy Kang
 */
public class HttpException extends Exception {

    private final String url;
    private final Integer httpCode;
    private final String responseBody;

    public HttpException(@NonNull String url, @NonNull String errorMessage) {
        super("url: " + url + ", " + errorMessage);
        this.url = url;
        this.httpCode = 0;
        this.responseBody = null;
    }

    public HttpException(@NonNull String url, @NonNull Exception e) {
        super("url: " + url + ", " + e.getMessage());
        this.url = url;
        this.httpCode = 0;
        this.responseBody = null;
    }

    public HttpException(
            @NonNull String url, @NonNull Integer httpCode, @NonNull String responseBody) {
        super("url: " + url + ", http code:" + httpCode + ", responseBody: " + responseBody);
        this.url = url;
        this.httpCode = httpCode;
        this.responseBody = responseBody;
    }
}
