package com.jimmyworks.easyhttp;

import com.jimmyworks.easyhttp.builder.BodyRequestBuilder;
import com.jimmyworks.easyhttp.builder.RequestBuilder;
import com.jimmyworks.easyhttp.service.DoRequestService;
import com.jimmyworks.easyhttp.type.HttpMethod;
import lombok.NonNull;
import okhttp3.Call;

import java.util.List;
import java.util.Map;

/**
 * EasyHttp
 *
 * @author Jimmy Kang
 */
public class EasyHttp {

    public static RequestBuilder get(@NonNull String url) {
        return new RequestBuilder(url);
    }

    public static RequestBuilder head(@NonNull String url) {
        return new RequestBuilder(url, HttpMethod.HEAD);
    }

    public static BodyRequestBuilder post(@NonNull String url) {
        return new BodyRequestBuilder(url);
    }

    public static BodyRequestBuilder put(@NonNull String url) {
        return new BodyRequestBuilder(url, HttpMethod.PUT);
    }

    public static BodyRequestBuilder delete(@NonNull String url) {
        return new BodyRequestBuilder(url, HttpMethod.DELETE);
    }

    public static BodyRequestBuilder patch(@NonNull String url) {
        return new BodyRequestBuilder(url, HttpMethod.PATCH);
    }

    public static BodyRequestBuilder method(@NonNull HttpMethod method, @NonNull String url) {
        return new BodyRequestBuilder(url, method);
    }

    public static void cancel(String tag) {
        List<Call> callList = DoRequestService.callMap.get(tag);

        if (null != callList) {
            for (Call call : callList) {
                call.cancel();
            }
            DoRequestService.callMap.remove(tag);
        }
    }

    public static void cancelAll() {
        for (Map.Entry<String, List<Call>> entry : DoRequestService.callMap.entrySet()) {
            for (Call call: entry.getValue()) {
                call.cancel();
            }
        }
        DoRequestService.callMap.clear();
    }
}
