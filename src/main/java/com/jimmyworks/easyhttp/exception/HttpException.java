package com.jimmyworks.easyhttp.exception;

import lombok.Getter;
import lombok.NonNull;

/**
 * 類別無對應錯誤
 *
 * @author Jimmy Kang
 */
public class HttpException extends Exception {

    @Getter private final String url;
    @Getter private final Integer httpCode;
    @Getter private final String responseBody;

    public HttpException(@NonNull String url, @NonNull String errorMessage) {
        super("url: " + url + ", " + errorMessage);
        this.url = url;
        this.httpCode = 0;
        this.responseBody = "";
    }

    public HttpException(@NonNull String url, @NonNull Exception e) {
        super("url: " + url + ", " + e.getMessage());
        this.url = url;
        this.httpCode = 0;
        this.responseBody = "";
    }

    public HttpException(@NonNull String url, @NonNull Integer httpCode) {
        super("url: " + url + ", httpCode:" + httpCode);
        this.url = url;
        this.httpCode = httpCode;
        this.responseBody = "";
    }

    public HttpException(
            @NonNull String url, @NonNull Integer httpCode, @NonNull String responseBody) {
        super("url: " + url + ", httpCode:" + httpCode + ", responseBody: " + responseBody);
        this.url = url;
        this.httpCode = httpCode;
        this.responseBody = responseBody;
    }
}
