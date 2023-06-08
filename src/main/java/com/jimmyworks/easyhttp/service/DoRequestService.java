package com.jimmyworks.easyhttp.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jimmyworks.easyhttp.entity.RequestInfo;
import com.jimmyworks.easyhttp.exception.HttpException;
import com.jimmyworks.easyhttp.listener.DownloadListener;
import com.jimmyworks.easyhttp.listener.JsonResponseListener;
import com.jimmyworks.easyhttp.listener.StringResponseListener;
import com.jimmyworks.easyhttp.utils.FileUtils;
import com.jimmyworks.easyhttp.utils.JsonUtils;

import lombok.NonNull;

import okhttp3.*;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 發送請求服務
 *
 * @author Jimmy Kang
 */
public class DoRequestService {

    public static final Map<String, List<Call>> callMap = new HashMap<>();

    private final OkHttpClient okHttpClient;
    private final RequestInfo requestInfo;

    public DoRequestService(@NonNull OkHttpClient okHttpClient, @NonNull RequestInfo requestInfo) {
        this.okHttpClient = okHttpClient;
        this.requestInfo = requestInfo;
    }

    private Response call() throws IOException {
        Call call = okHttpClient.newCall(requestInfo.getRequest());
        Object tag = requestInfo.getRequest().tag();
        if (null != tag) {
            addCall(tag.toString(), call);
        }
        return call.execute();
    }

    private void call(@NonNull Callback callback) {
        Call call = okHttpClient.newCall(requestInfo.getRequest());
        Object tag = requestInfo.getRequest().tag();
        if (null != tag) {
            addCall(tag.toString(), call);
        }
        call.enqueue(callback);
    }

    private void addCall(@NonNull String tag, @NonNull Call call) {
        List<Call> callList = callMap.get(tag);
        if (null == callList) {
            callList = new ArrayList<>();
        }
        callList.add(call);
        callMap.put(tag, callList);
    }

    public String getAsString() throws HttpException {
        return getAsString(StandardCharsets.UTF_8);
    }

    public String getAsString(@NonNull Charset charset) throws HttpException {
        try (Response response = call()) {
            ResponseBody responseBody = response.body();
            if (response.code() >= 200 && response.code() < 300) {
                if (null != responseBody) {
                    return new String(responseBody.bytes(), charset);
                } else {
                    return "";
                }
            } else {
                if (null != responseBody) {
                    throw new HttpException(
                            requestInfo.getUrl(),
                            response.code(),
                            new String(responseBody.bytes(), charset));
                } else {
                    throw new HttpException(requestInfo.getUrl(), response.code());
                }
            }
        } catch (IOException e) {
            throw new HttpException(requestInfo.getUrl(), e);
        }
    }

    public void getAsString(@NonNull StringResponseListener responseListener) {
        getAsString(StandardCharsets.UTF_8, responseListener);
    }

    public void getAsString(
            @NonNull Charset charset, @NonNull StringResponseListener responseListener) {
        call(
                new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        responseListener.onError(new HttpException(requestInfo.getUrl(), e));
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) {
                        try (response) {
                            ResponseBody responseBody = response.body();
                            if (response.code() >= 200 && response.code() < 300) {
                                if (null != responseBody) {
                                    responseListener.onSuccess(
                                            response.headers(),
                                            new String(responseBody.bytes(), charset));
                                } else {
                                    responseListener.onSuccess(response.headers(), "");
                                }
                            } else {
                                if (null != responseBody) {
                                    responseListener.onError(
                                            new HttpException(
                                                    requestInfo.getUrl(),
                                                    response.code(),
                                                    new String(responseBody.bytes(), charset)));
                                } else {
                                    responseListener.onError(
                                            new HttpException(
                                                    requestInfo.getUrl(), response.code()));
                                }
                            }
                        } catch (IOException e) {
                            responseListener.onError(new HttpException(requestInfo.getUrl(), e));
                        }
                    }
                });
    }

    public <T> T getJsonAsObject(@NonNull Class<T> clazz) throws HttpException {
        return getJsonAsObject(new Gson(), clazz);
    }

    public <T> T getJsonAsObject(@NonNull Gson gson, @NonNull Class<T> clazz) throws HttpException {
        return getJsonAsObject(gson, StandardCharsets.UTF_8, clazz);
    }

    public <T> T getJsonAsObject(@NonNull Charset charset, @NonNull Class<T> clazz)
            throws HttpException {
        return JsonUtils.jsonToObject(clazz, getAsString(charset));
    }

    public <T> T getJsonAsObject(
            @NonNull Gson gson, @NonNull Charset charset, @NonNull Class<T> clazz)
            throws HttpException {
        return JsonUtils.jsonToObject(gson, clazz, getAsString(charset));
    }

    public <T> void getJsonAsObject(
            @NonNull Class<T> clazz, @NonNull JsonResponseListener<T> responseListener) {
        getJsonAsObject(StandardCharsets.UTF_8, clazz, responseListener);
    }

    public <T> void getJsonAsObject(
            @NonNull Gson gson,
            @NonNull Class<T> clazz,
            @NonNull JsonResponseListener<T> responseListener) {
        getJsonAsObject(gson, StandardCharsets.UTF_8, clazz, responseListener);
    }

    public <T> void getJsonAsObject(
            @NonNull Charset charset,
            @NonNull Class<T> clazz,
            @NonNull JsonResponseListener<T> responseListener) {
        getJsonAsObject(new Gson(), charset, clazz, responseListener);
    }

    public <T> void getJsonAsObject(
            @NonNull Gson gson,
            @NonNull Charset charset,
            @NonNull Class<T> clazz,
            @NonNull JsonResponseListener<T> responseListener) {
        getAsString(
                charset,
                new StringResponseListener() {
                    @Override
                    public void onSuccess(@NonNull Headers headers, String body) {
                        T bodyObject;
                        try {
                            bodyObject = JsonUtils.jsonToObject(gson, clazz, body);
                        } catch (Exception e) {
                            responseListener.onError(new HttpException(requestInfo.getUrl(), e));
                            return;
                        }
                        responseListener.onSuccess(headers, bodyObject);
                    }

                    @Override
                    public void onError(@NonNull HttpException e) {
                        responseListener.onError(e);
                    }
                });
    }

    public <T> T getJsonAsObject(@NonNull TypeToken<T> type) throws HttpException {
        return getJsonAsObject(StandardCharsets.UTF_8, type);
    }

    public <T> T getJsonAsObject(@NonNull Gson gson, @NonNull TypeToken<T> type)
            throws HttpException {
        return getJsonAsObject(gson, StandardCharsets.UTF_8, type);
    }

    public <T> T getJsonAsObject(@NonNull Charset charset, @NonNull TypeToken<T> type)
            throws HttpException {
        return getJsonAsObject(new Gson(), charset, type);
    }

    public <T> T getJsonAsObject(
            @NonNull Gson gson, @NonNull Charset charset, @NonNull TypeToken<T> type)
            throws HttpException {
        try {
            return JsonUtils.jsonToObject(gson, type, getAsString(charset));
        } catch (HttpException e) {
            throw e;
        } catch (Exception e) {
            throw new HttpException(requestInfo.getUrl(), e);
        }
    }

    public <T> void getJsonAsObject(
            @NonNull TypeToken<T> type, @NonNull JsonResponseListener<T> responseListener) {
        getJsonAsObject(StandardCharsets.UTF_8, type, responseListener);
    }

    public <T> void getJsonAsObject(
            @NonNull Gson gson,
            @NonNull TypeToken<T> type,
            @NonNull JsonResponseListener<T> responseListener) {
        getJsonAsObject(gson, StandardCharsets.UTF_8, type, responseListener);
    }

    public <T> void getJsonAsObject(
            @NonNull Charset charset,
            @NonNull TypeToken<T> type,
            @NonNull JsonResponseListener<T> responseListener) {
        getJsonAsObject(new Gson(), charset, type, responseListener);
    }

    public <T> void getJsonAsObject(
            @NonNull Gson gson,
            @NonNull Charset charset,
            @NonNull TypeToken<T> type,
            @NonNull JsonResponseListener<T> responseListener) {
        getAsString(
                charset,
                new StringResponseListener() {
                    @Override
                    public void onSuccess(@NonNull Headers headers, String body) {
                        T bodyObject;
                        try {
                            bodyObject = JsonUtils.jsonToObject(gson, type, body);
                        } catch (Exception e) {
                            responseListener.onError(new HttpException(requestInfo.getUrl(), e));
                            return;
                        }
                        responseListener.onSuccess(headers, bodyObject);
                    }

                    @Override
                    public void onError(@NonNull HttpException e) {
                        responseListener.onError(e);
                    }
                });
    }

    public void download(@NonNull File file) throws HttpException {
        FileUtils.mkdir(file.getParentFile());
        try (Response response = call();
                FileOutputStream outputStream = new FileOutputStream(file)) {
            if (response.code() >= 200 && response.code() < 300) {
                ResponseBody responseBody = response.body();
                if (null == responseBody) {
                    throw new HttpException(requestInfo.getUrl(), "Response body is empty.");
                }

                InputStream inputStream = responseBody.byteStream();
                byte[] buf = new byte[1024];
                int len = 0;

                while (len != -1) {
                    outputStream.write(buf, 0, len);
                    len = inputStream.read(buf);
                }
            } else {
                throw new HttpException(requestInfo.getUrl(), response.code());
            }
        } catch (IOException e) {
            throw new HttpException(requestInfo.getUrl(), e);
        }
    }

    public void download(@NonNull File file, @NonNull DownloadListener downloadListener) {
        FileUtils.mkdir(file.getParentFile());
        try (Response response = call();
                FileOutputStream outputStream = new FileOutputStream(file)) {
            if (response.code() >= 200 && response.code() < 300) {
                ResponseBody responseBody = response.body();
                if (null == responseBody) {
                    downloadListener.onError(
                            new HttpException(requestInfo.getUrl(), "Response body is empty."));
                    return;
                }

                long contentLength = responseBody.contentLength();
                InputStream inputStream = responseBody.byteStream();
                byte[] buf = new byte[1024];
                long sum = 0;
                int len = 0;

                while (len != -1) {
                    outputStream.write(buf, 0, len);
                    sum += len;
                    if (contentLength == -1) {
                        len = inputStream.read(buf);
                        continue;
                    }
                    downloadListener.onProgress(sum, contentLength);
                    len = inputStream.read(buf);
                }
                downloadListener.onSuccess(response.headers(), file);
            } else {
                downloadListener.onError(new HttpException(requestInfo.getUrl(), response.code()));
            }
        } catch (IOException e) {
            downloadListener.onError(new HttpException(requestInfo.getUrl(), e));
        }
    }
}
