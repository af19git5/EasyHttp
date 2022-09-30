package com.jimmyworks.easyhttp.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Json處理共用
 *
 * @author Jimmy Kang
 */
public class JsonUtils {

    public static <T> T jsonToObject(Class<T> clazz, String json) {
        return new Gson().fromJson(json, clazz);
    }

    public static <T> T jsonToObject(TypeToken<T> type, String json) {
        return new Gson().fromJson(json, type.getType());
    }

    public static String toJson(Object obj) {
        return new Gson().toJson(obj);
    }
}
