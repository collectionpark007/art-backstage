package com.art.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.art.service.SysUserService;
import com.art.utils.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: never
 * @Date: 2019/4/16 下午11:26
 */
@RestController
@RequestMapping("/admin")
public class SysUserController {

    @Autowired
    private SysUserService userService;

    @RequiresPermissions("user:add")
    @PostMapping("/addUser")
    public JSONObject addUser(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "username, password, nickname,   roleId");
        return userService.addUser(requestJson);
    }

    @RequiresPermissions("user:list")
    @PostMapping("/listUser")
    public JSONObject listUser(@RequestBody JSONObject jsonObject) {
        return userService.listUser(jsonObject);
    }


}
