package com.shaoya.yaapiinterface.controller;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 网易云音乐接口
 *
 * @author shaoyafan
 */
@RestController
public class NetEaseController {

    @PostMapping("/api/comments.163")
    public String hotComments(HttpServletRequest request) {
        String url = "https://api.uomg.com/api/comments.163";
        String body = URLUtil.decode(request.getHeader("body"), CharsetUtil.CHARSET_UTF_8);
        HttpResponse response = HttpRequest.post(url)
                .body(body)
                .execute();
        return response.body();
    }

    @PostMapping("/api/rand.music")
    public String randMusic(HttpServletRequest request) {
        String url = "https://api.uomg.com/api/rand.music";
        String body = URLUtil.decode(request.getHeader("body"), CharsetUtil.CHARSET_UTF_8);
        HttpResponse httpResponse = HttpRequest.get(url + "?" + body)
                .execute();
        return httpResponse.body();
    }
}
