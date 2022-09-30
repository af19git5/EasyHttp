package com.jimmyworks.easyhttp.listener;

import com.jimmyworks.easyhttp.exception.HttpException;
import lombok.NonNull;
import okhttp3.Headers;

/**
 * json回傳監聽器
 *
 * @author Jimmy Kang
 */
public interface JsonResponseListener<T> {

    void onSuccess(@NonNull Headers headers, T body);

    void onError(@NonNull HttpException e);
}
