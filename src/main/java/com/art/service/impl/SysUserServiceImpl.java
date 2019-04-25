package com.art.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.art.config.shiro.AuthenticationToken;
import com.art.entity.SysUser;
import com.art.mapper.SysUserMapper;
import com.art.service.MyHttpSessionService;
import com.art.service.SysPermissionService;
import com.art.service.SysUserService;
import com.art.utils.CommonUtil;
import com.art.utils.constants.Constants;
import com.art.utils.constants.ErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;

/**
 * @author: never
 * @description: 用户/角色/权限
 * @date: 2017/11/2 10:18
 */
@Slf4j
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser, Long> implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    MyHttpSessionService myHttpSessionService;
    @Autowired
    SysPermissionService sysPermissionService;
    @Autowired
    CacheServiceImpl cacheService;


    /**
     * 用户列表
     */
    public JSONObject listUser(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        int count = sysUserMapper.countUser(jsonObject);
        List<JSONObject> list = sysUserMapper.listUser(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    /**
     * 用户列表
     */
    @Override
    public JSONObject findAllUsers(JSONObject jsonObject) {
        int count = sysUserMapper.countUser(jsonObject);
        List<JSONObject> list = sysUserMapper.listUser(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    /**
     * 添加用户
     */
    @Override
    public JSONObject addUser(JSONObject jsonObject) {
        int count = sysUserMapper.queryExistUsername(jsonObject);
        if (count > 0) {
            return CommonUtil.errorJson(ErrorEnum.E_10009);
        }
        SysUser sysUser = JSONObject.parseObject(jsonObject.toString(), SysUser.class);
        sysUserMapper.insert(sysUser);
        return CommonUtil.successJson();
    }

    /**
     * 查询所有的角色
     * 在添加/修改用户的时候要使用此方法
     */
    @Override
    public JSONObject getAllRoles() {
        List<JSONObject> roles = sysUserMapper.getAllRoles();
        return CommonUtil.successPage(roles);
    }

    /**
     * 修改用户
     */
    @Override
    public JSONObject updateUser(JSONObject jsonObject) {
        sysUserMapper.updateUser(jsonObject);
        return CommonUtil.successJson();
    }

    /**
     * 角色列表
     */
    @Override
    public JSONObject listRole() {
        List<JSONObject> roles = sysUserMapper.listRole();
        return CommonUtil.successPage(roles);
    }

    /**
     * 查询所有权限, 给角色分配权限时调用
     */
    @Override
    public JSONObject listAllPermission() {
        List<JSONObject> permissions = sysUserMapper.listAllPermission();
        return CommonUtil.successPage(permissions);
    }

    /**
     * 添加角色
     */
    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("unchecked")
    @Override
    public JSONObject addRole(JSONObject jsonObject) {
        sysUserMapper.insertRole(jsonObject);
        sysUserMapper.insertRolePermission(jsonObject.getString("roleId"), (List<Integer>) jsonObject.get("permissions"));
        return CommonUtil.successJson();
    }

    /**
     * 修改角色
     */
    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("unchecked")
    @Override
    public JSONObject updateRole(JSONObject jsonObject) {
        String roleId = jsonObject.getString("roleId");
        List<Integer> newPerms = (List<Integer>) jsonObject.get("permissions");
        JSONObject roleInfo = sysUserMapper.getRoleAllInfo(jsonObject);
        Set<Integer> oldPerms = (Set<Integer>) roleInfo.get("permissionIds");
        //修改角色名称
        dealRoleName(jsonObject, roleInfo);
        //添加新权限
        saveNewPermission(roleId, newPerms, oldPerms);
        //移除旧的不再拥有的权限
        removeOldPermission(roleId, newPerms, oldPerms);
        return CommonUtil.successJson();
    }

    /**
     * 修改角色名称
     */
    private void dealRoleName(JSONObject paramJson, JSONObject roleInfo) {
        String roleName = paramJson.getString("roleName");
        if (!roleName.equals(roleInfo.getString("roleName"))) {
            sysUserMapper.updateRoleName(paramJson);
        }
    }

    /**
     * 为角色添加新权限
     */
    private void saveNewPermission(String roleId, Collection<Integer> newPerms, Collection<Integer> oldPerms) {
        List<Integer> waitInsert = new ArrayList<>();
        for (Integer newPerm : newPerms) {
            if (!oldPerms.contains(newPerm)) {
                waitInsert.add(newPerm);
            }
        }
        if (waitInsert.size() > 0) {
            sysUserMapper.insertRolePermission(roleId, waitInsert);
        }
    }

    /**
     * 删除角色 旧的 不再拥有的权限
     */
    private void removeOldPermission(String roleId, Collection<Integer> newPerms, Collection<Integer> oldPerms) {
        List<Integer> waitRemove = new ArrayList<>();
        for (Integer oldPerm : oldPerms) {
            if (!newPerms.contains(oldPerm)) {
                waitRemove.add(oldPerm);
            }
        }
        if (waitRemove.size() > 0) {
            sysUserMapper.removeOldPermission(roleId, waitRemove);
        }
    }

    /**
     * 删除角色
     */
    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("unchecked")
    @Override
    public JSONObject deleteRole(JSONObject jsonObject) {
        JSONObject roleInfo = sysUserMapper.getRoleAllInfo(jsonObject);
        List<JSONObject> users = (List<JSONObject>) roleInfo.get("users");
        if (users != null && users.size() > 0) {
            return CommonUtil.errorJson(ErrorEnum.E_10008);
        }
        sysUserMapper.removeRole(jsonObject);
        sysUserMapper.removeRoleAllPermission(jsonObject);
        return CommonUtil.successJson();
    }

    /**
     * 生成loginKey
     *
     * @param sysUser
     * @return
     */
    @Override
    public String generateLoginKey(SysUser sysUser, String password, HttpServletRequest request) {
        if (sysUser == null) {
            return null;
        }
        //设置新的登录信息（区分app 和 pc）
        String newLoginKey = UUID.randomUUID() + CommonUtil.randomNum(6);
        // 区分windows 和 app
        request.setAttribute("loginKey", newLoginKey);
        log.info("登录 loginKey : " + newLoginKey);
        myHttpSessionService.setMemberAttribute(SysUser.PRINCIPAL_ATTRIBUTE_NAME, sysUser);
        AuthenticationToken token = new AuthenticationToken(sysUser.getUsername(), password, false);
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
        //token
        Serializable id = subject.getSession().getId();
        //将token放入redis
        cacheService.set("sys:login:user_token_" + id.toString(), sysUser.getId() + "", Constants.REDIS_SESSION_EXPIRED_SECONDS);
        //防止同一个账号同时登录
        cacheService.set("sys:user:id_" + sysUser.getId(), id.toString(), Constants.REDIS_SESSION_EXPIRED_SECONDS);
        //用户信息
        cacheService.set("sys:login:user_info_" + sysUser.getId(), JSONObject.toJSONString(sysUser), Constants.REDIS_SESSION_EXPIRED_SECONDS);

        return newLoginKey;
    }

    /**
     * 获取管理信息
     *
     * @param jsonObject
     * @return
     */
    @Override
    public SysUser findUserByName(JSONObject jsonObject) {
        return sysUserMapper.findSysUserByName(jsonObject);
    }

    /**
     * 获取当前登录用户信息
     *
     * @return
     */
    @Transactional(readOnly = true)
    public JSONObject getCurrent() {
        SysUser userInfo = (SysUser) myHttpSessionService.getMemberAttribute(SysUser.PRINCIPAL_ATTRIBUTE_NAME);

        Session session = SecurityUtils.getSubject().getSession();
        //SysUser userInfo = (SysUser) session.getAttribute(Constants.SESSION_USER_INFO);
        String username = userInfo.getUsername();
        JSONObject info = new JSONObject();
        JSONObject userPermission = sysPermissionService.getUserPermission(username);
        session.setAttribute(Constants.SESSION_USER_PERMISSION, userPermission);
        info.put("userPermission", userPermission);
        return info;
    }
}
