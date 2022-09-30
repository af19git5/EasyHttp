package com.jimmyworks.easyhttp.listener;

import com.jimmyworks.easyhttp.exception.HttpException;
import lombok.NonNull;
import okhttp3.Headers;

/**
 * 字串回傳監聽器
 *
 * @author Jimmy Kang
 */
public interface StringResponseListener {

    void onSuccess(@NonNull Headers headers, String body);

    void onError(@NonNull HttpException e);
}
