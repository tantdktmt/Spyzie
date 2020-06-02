package com.tantd.spyzie.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

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
        mGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    public <T> String toJson(T t) {
        return mGson.toJson(t);
    }

    public JSONArray toJsonArray(String json) {
        try {
            return new JSONArray(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> JSONArray toJsonArray(T t) {
        String jsonStr = toJson(t);
        return toJsonArray(jsonStr);
    }
}
