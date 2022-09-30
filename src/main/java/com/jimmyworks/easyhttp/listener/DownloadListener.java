package com.jimmyworks.easyhttp.listener;

import com.jimmyworks.easyhttp.exception.HttpException;
import lombok.NonNull;
import okhttp3.Headers;

import java.io.File;

/**
 * 下載監聽器
 *
 * @author Jimmy Kang
 */
public interface DownloadListener {

    void onSuccess(@NonNull Headers headers, @NonNull File file);

    void onProgress(long downloadBytes, long totalBytes);

    void onError(@NonNull HttpException e);
}
