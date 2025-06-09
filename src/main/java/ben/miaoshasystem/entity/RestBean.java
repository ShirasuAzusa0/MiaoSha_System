package ben.miaoshasystem.entity;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;

public record RestBean<T>(String status, String msg, T data) {
    public static <T> RestBean<T> success(T data) {
        return new RestBean<>("success", "请求成功", data);
    }

    public static <T> RestBean<T> success() {
        return success(null);
    }

    public static <T> RestBean<T> failure(String msg) {
        return new RestBean<>("failure", msg, null);
    }

    public String asJsonString() {
        return JSONObject.toJSONString(this, JSONWriter.Feature.WriteNulls);
    }
}
