package com.art.controller;

import com.alibaba.fastjson.JSONObject;
import com.art.service.LoginService;
import com.art.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: never
 * @description: 登录相关Controller
 * @date: 2019/04/22 12:33
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 登录
     */
    @PostMapping("/auth")
    public JSONObject authLogin(@RequestBody JSONObject requestJson, HttpServletRequest request) {
        CommonUtil.hasAllRequired(requestJson, "username,enPassword");
        return loginService.authLogin(requestJson, request);
    }

    /**
     * 登出
     */
    @PostMapping("/logout")
    public JSONObject logout(HttpServletRequest request) {
        return loginService.logout(request);
    }
}
