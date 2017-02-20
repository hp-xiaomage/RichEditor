package com.xmg.richeditor_android.utils;

import com.google.gson.Gson;

public class GsonUtil {
    static Gson gson = new Gson();

    public static <T> T getBean(String jsonStr, Class<T> className) {
        try {
            T t = gson.fromJson(jsonStr, className);
            return t;
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> String toJsonString(T object) {
        return gson.toJson(object);
    }

}
