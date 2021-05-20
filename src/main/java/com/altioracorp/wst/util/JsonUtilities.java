package com.altioracorp.wst.util;

import com.google.gson.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class JsonUtilities {

    private static final JsonUtilities INSTANCE = new JsonUtilities();

    public static JsonUtilities getInstance() {
        return INSTANCE;
    }

    public static JsonObject stringToJsonObject(final String string) {
        return new Gson().fromJson(string, JsonObject.class);
    }

    public static String removeLineBreak(final String string) {
        return string.replaceAll("\n", "");
    }

    public static Gson createBuilder() {
        final GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(BigDecimal.class, (JsonSerializer<BigDecimal>) (src, typeOfSrc, context) -> {
            final DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.CEILING);
            return new JsonPrimitive(src.setScale(2, BigDecimal.ROUND_HALF_UP));
        });
        return builder.create();
    }

    public static Integer getIntegerFromJson(final JSONObject jsonObject, final String key) {
        return jsonObject.has(key) ? Integer.parseInt(jsonObject.get(key).toString()) : null;
    }

    public static BigDecimal getBigDecimalFromJson(final JSONObject jsonObject, final String key) {
        return jsonObject.has(key) ? new BigDecimal(jsonObject.get(key).toString())
                .setScale(2, BigDecimal.ROUND_HALF_UP) : null;
    }

    public static JSONObject getObjectFromJson(final JSONObject jsonObject, final String key) {
        return jsonObject.has(key) ? (JSONObject) jsonObject.get(key) : null;
    }

    public static JSONArray getArrayFromJson(final JSONObject jsonObject, final String key) {
        return jsonObject.has(key) ? (JSONArray) jsonObject.get(key) : null;
    }

    public static String getStringFromJson(final JSONObject jsonObject, final String key) {
        return jsonObject.has(key) ? jsonObject.get(key).toString() : null;
    }

    public static boolean isJsonArray(final JSONObject jsonObject, final String key) {
        return jsonObject.get(key) instanceof JSONArray;
    }

    public static boolean isJsonObject(final JSONObject jsonObject, final String key) {
        return jsonObject.get(key) instanceof JSONObject;
    }
}
