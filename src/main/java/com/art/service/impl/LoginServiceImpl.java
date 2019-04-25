package com.art.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.art.entity.SysUser;
import com.art.mapper.LoginMapper;
import com.art.service.*;
import com.art.utils.CommonUtil;
import com.art.utils.constants.Constants;
import com.art.utils.constants.ErrorEnum;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: never
 * @description: 登录service实现类
 * @date: 2019/04/18 11:53
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginMapper loginMapper;
    @Autowired
    private SysPermissionService sysPermissionService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private MyHttpSessionService myHttpSessionService;
    @Autowired
    private RSAService rsaService;

    /**
     * 登录表单提交
     */
    @Override
    public JSONObject authLogin(JSONObject jsonObject, HttpServletRequest request) {
        //String password = rsaService.decryptRSAParameter(jsonObject.getString("enPassword"), request);
        String password = jsonObject.getString("enPassword");
        SysUser sysUser = sysUserService.findUserByName(jsonObject);

        if (sysUser == null) {
            return CommonUtil.errorJson(ErrorEnum.E_10010);
        }
        if (!DigestUtils.md5Hex(password).equals(sysUser.getPassword())) {
            return CommonUtil.errorJson(ErrorEnum.E_10011);
        }
        rsaService.removePrivateKey(request);
        String loginKey = sysUserService.generateLoginKey(sysUser,password, request);
        jsonObject.put("loginKey", loginKey);
        return jsonObject;
    }

    /**
     * 根据用户名和密码查询对应的用户
     */
    @Override
    public JSONObject getUser(String username, String password) {
        return loginMapper.getUser(username, password);
    }


    /**
     * 退出登录
     */
    @Override
    public JSONObject logout(HttpServletRequest request) {
        try {
            Subject currentUser = SecurityUtils.getSubject();
            currentUser.logout();
            String loginKey = request.getHeader(Constants.SESSION_USER_INFO);
            myHttpSessionService.removeSession(loginKey);
        } catch (Exception e) {
        }
        return CommonUtil.successJson();
    }
}
