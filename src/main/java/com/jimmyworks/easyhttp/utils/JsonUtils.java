package com.jimmyworks.easyhttp.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lombok.NonNull;

/**
 * Json處理共用
 *
 * @author Jimmy Kang
 */
public class JsonUtils {

    public static <T> T jsonToObject(@NonNull Class<T> clazz, @NonNull String json) {
        return jsonToObject(new Gson(), clazz, json);
    }

    public static <T> T jsonToObject(
            @NonNull Gson gson, @NonNull Class<T> clazz, @NonNull String json) {
        return gson.fromJson(json, clazz);
    }

    public static <T> T jsonToObject(@NonNull TypeToken<T> type, @NonNull String json) {
        return jsonToObject(new Gson(), type, json);
    }

    public static <T> T jsonToObject(
            @NonNull Gson gson, @NonNull TypeToken<T> type, @NonNull String json) {
        return gson.fromJson(json, type.getType());
    }

    public static String toJson(@NonNull Object obj) {
        return toJson(new Gson(), obj);
    }

    public static String toJson(@NonNull Gson gson, @NonNull Object obj) {
        return gson.toJson(obj);
    }
}
