package com.shaoya.yaapiinterface;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.shaoya.yaapiclientsdk.model.User;

import java.util.HashMap;
import java.util.Map;

import static com.shaoya.yaapiclientsdk.utils.SignUtils.genSign;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
//        String post = main.invokeInterface("/api/name/", "POST", "{\"username\":\"shaoya\"}");
        User user = new User();
        user.setUsername("shaoya");
        String post = main.getUserNameByPost(user);
        System.out.println(post);
    }

    public String getUserNameByPost(User user) {
        String GATEWAY_URL = "http://localhost:8090";
        String json = JSONUtil.toJsonStr(user);
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_URL + "/api/name/user")
                .addHeaders(getHeaderMap(json))
                .body(json)
                .execute();
        System.out.println(httpResponse.getStatus());
        String result = httpResponse.body();
        System.out.println(result);
        return result;
    }

        private Map<String, String> getHeaderMap(String body) {
        Map<String, String> hashMap = new HashMap<>();
//        hashMap.put("accessKey", accessKey);
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        hashMap.put("body", body);
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis()/1000));
//        hashMap.put("sign", genSign(body, secretKey));
        return hashMap;
    }

    public String invokeInterface(String url, String method, String params) {
        String GATEWAY_URL = "http://localhost:8090";
        HttpResponse httpResponse = HttpRequest.post("http://localhost:8090/api/name/user")
                .header("Accept-Charset", CharsetUtil.UTF_8)
                .addHeaders(getHeaderMap(method, params))
                .body(params)
                .execute();
        return JSONUtil.formatJsonStr(httpResponse.body());
    }

    private Map<String, String> getHeaderMap(String method, String body) {
        Map<String, String> map = new HashMap<>();
        map.put("nonce", RandomUtil.randomNumbers(4));
        map.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        map.put("method", method);
        body = URLUtil.encode(body, CharsetUtil.CHARSET_UTF_8);
        map.put("body", body);
        return map;
    }

}
