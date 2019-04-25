package com.art.service;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: never
 * @description: 登录Service
 * @date: 2019/04/18 11:02
 */
public interface LoginService {
    /**
     * 登录表单提交
     */
    JSONObject authLogin(JSONObject jsonObject, HttpServletRequest request);

    /**
     * 根据用户名和密码查询对应的用户
     *
     * @param username 用户名
     * @param password 密码
     */
    JSONObject getUser(String username, String password);

    /**
     * 退出登录
     */
    JSONObject logout(HttpServletRequest request);
}
