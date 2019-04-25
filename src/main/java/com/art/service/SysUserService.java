package com.art.service;

import com.alibaba.fastjson.JSONObject;
import com.art.entity.SysUser;
import com.art.mapper.SysUserMapper;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: never
 * @description: 用户/角色/权限
 * @date: 2017/11/2 10:18
 */
public interface SysUserService extends BaseService<SysUserMapper, SysUser, Long> {
    /**
     * 用户列表
     */
    JSONObject listUser(JSONObject jsonObject);

    /**
     * 用户列表
     */
    JSONObject findAllUsers(JSONObject jsonObject);

    /**
     * 查询所有的角色
     * 在添加/修改用户的时候要使用此方法
     */
    JSONObject getAllRoles();

    /**
     * 添加用户
     */
    JSONObject addUser(JSONObject jsonObject);

    /**
     * 修改用户
     */
    JSONObject updateUser(JSONObject jsonObject);

    /**
     * 角色列表
     */
    JSONObject listRole();

    /**
     * 查询所有权限, 给角色分配权限时调用
     */
    JSONObject listAllPermission();

    /**
     * 添加角色
     */
    JSONObject addRole(JSONObject jsonObject);

    /**
     * 修改角色
     */
    JSONObject updateRole(JSONObject jsonObject);

    /**
     * 删除角色
     */
    JSONObject deleteRole(JSONObject jsonObject);

    /**
     * 生成loginKey
     *
     * @return
     */
    String generateLoginKey(SysUser sysUser, String password, HttpServletRequest request);

    /**
     * 获取管理信息
     *
     * @param jsonObject
     * @return
     */
    SysUser findUserByName(JSONObject jsonObject);

    /**
     * 获取当前登录用户信息
     *
     * @return
     */
    JSONObject getCurrent();
}
