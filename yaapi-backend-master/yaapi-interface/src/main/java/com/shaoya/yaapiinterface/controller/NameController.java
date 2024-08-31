package com.shaoya.yaapiinterface.controller;

import com.shaoya.yaapiclientsdk.model.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 名称 API
 *
 * @author shaoyafan
 */
@RestController
public class NameController {

    @PostMapping("api/name/user")
    public String getUserNameByPost(@RequestBody User user, HttpServletRequest request) {
        return "Post 你的名字是" + user.getUsername();
    }
}
