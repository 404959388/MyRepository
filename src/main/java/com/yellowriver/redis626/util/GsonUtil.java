package com.yellowriver.redis626.util;

import com.google.gson.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huanghe
 * @create 2021/12/6 9:57 上午
 */
public class GsonUtil {

    private static Gson prettyGson = null;
    private static Gson gson = null;
    private static Gson gsonNoNull = null;
    private static Gson prettyGsonNoNull = null;

    static {
        gsonNoNull =
                new GsonBuilder()
                        .enableComplexMapKeySerialization()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .disableHtmlEscaping()
                        .create();
        gson =
                new GsonBuilder()
                        .serializeNulls()
                        // 开启后默认是不序列化 和反序列化的
                        //       .excludeFieldsWithoutExposeAnnotation()
                        .enableComplexMapKeySerialization()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .disableHtmlEscaping()
                        .create();
        prettyGson =
                new GsonBuilder()
                        .serializeNulls()
                        .setPrettyPrinting()
                        //    .excludeFieldsWithoutExposeAnnotation()
                        .enableComplexMapKeySerialization()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .disableHtmlEscaping()
                        .create();
        prettyGsonNoNull =
                new GsonBuilder()
                        .setPrettyPrinting()
                        //    .excludeFieldsWithoutExposeAnnotation()
                        .enableComplexMapKeySerialization()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .disableHtmlEscaping()
                        .create();
    }

    /**
     * 将对象转成json格式
     *
     * @param object
     * @return String
     */
    public static String asJsonString(Object object) {
        return gson.toJson(object);
    }

    public static String asJsonString(Object object, Boolean pretty) {
        if (pretty) {
            return prettyGson.toJson(object);
        } else {
            return asJsonString(object);
        }
    }

    public static String asJsonStringNotNull(Object object) {
        return gsonNoNull.toJson(object);
    }

    public static String asJsonStringNotNull(Object object, boolean pretty) {
        if (pretty) {
            return prettyGsonNoNull.toJson(object);
        } else {
            return asJsonStringNotNull(object);
        }
    }

    /**
     * 将json转成特定的cls的对象
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> T asJavaObject(String gsonString, Class<T> cls) {
        return gson.fromJson(gsonString, cls);
    }

    /**
     * json字符串转成 - list
     *
     * @param arrayString
     * @param cls
     * @return
     */
    public static <T> List<T> asJavaArray(String arrayString, Class<T> cls) {
        ArrayList<T> mList = new ArrayList<T>();
        JsonArray array = parseArray(arrayString);
        for (final JsonElement elem : array) {
            mList.add(gson.fromJson(elem, cls));
        }
        return mList;
    }

    public static JsonObject parseObject(String objectString) {
        return JsonParser.parseString(objectString).getAsJsonObject();
    }

    public static JsonArray parseArray(String arrayString) {
        return JsonParser.parseString(arrayString).getAsJsonArray();
    }

    public static JsonElement parseElement(String objectString) {
        return JsonParser.parseString(objectString);
    }

    public static JsonElement parseElement(Object value) {
        return parseElement(asJsonString(value));
    }
}
