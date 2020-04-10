package com.tantd.spyzie.util;

import com.google.gson.Gson;

/**
 * Created by tantd on 4/10/2020.
 */
public final class JsonUtil {

    private static JsonUtil instance;

    private Gson mGson;

    public static synchronized JsonUtil getInstance() {
        if (instance == null) {
            instance = new JsonUtil();
        }
        return instance;
    }

    private JsonUtil() {
        mGson = new Gson();
    }

    public <T> String toJson(T t) {
        return mGson.toJson(t);
    }
}
