package com.jimmyworks.easyhttp.type;

import com.jimmyworks.easyhttp.exception.TypeNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * http傳輸格式
 *
 * @author Jimmy Kang
 */
@AllArgsConstructor
@Getter
public enum HttpMethod {
    GET("GET", false),

    HEAD("HEAD", false),

    POST("POST", true),

    PUT("PUT", true),

    DELETE("DELETE", true),

    PATCH("PATCH", true);

    private final String code;

    private final Boolean haveBody;

    public static HttpMethod toHttpMethod(String code) throws TypeNotFoundException {
        for (HttpMethod tmp : HttpMethod.values()) {
            if (tmp.getCode().equals(code)) {
                return tmp;
            }
        }
        throw new TypeNotFoundException("HttpMethod not found");
    }
}
