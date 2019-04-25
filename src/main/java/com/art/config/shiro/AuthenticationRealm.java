/*
 *
 *
 *
 */
package com.art.config.shiro;

import com.alibaba.fastjson.JSONObject;
import com.art.entity.SysRole;
import com.art.entity.SysUser;
import com.art.mapper.SysUserMapper;
import com.art.support.Principal;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限认证
 */
public class AuthenticationRealm extends AuthorizingRealm {

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 获取认证信息
     *
     * @param token 令牌
     * @return 认证信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(org.apache.shiro.authc.AuthenticationToken token) {
        AuthenticationToken authenticationToken = (AuthenticationToken) token;
        String username = authenticationToken.getUsername();
        String password = new String(authenticationToken.getPassword());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        if (username != null && password != null) {
            SysUser sysUser = sysUserMapper.findSysUserByName(jsonObject);
            if (sysUser == null) {
                throw new UnknownAccountException();
            }
            if (!DigestUtils.md5Hex(password).equals(sysUser.getPassword())) {
                throw new IncorrectCredentialsException("密码错误");
            }
            return new SimpleAuthenticationInfo(new Principal(sysUser.getId(), username), password, getName());
        }
        throw new UnknownAccountException();
    }

    /**
     * 获取授权信息
     *
     * @param principals principals
     * @return 授权信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Principal principal = (Principal) principals.fromRealm(getName()).iterator().next();
        if (principal != null) {
            List<String> authorities = this.listRolesById(principal.getId());
            if (authorities != null) {
                SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
                authorizationInfo.addStringPermissions(authorities);
                return authorizationInfo;
            }
        }
        return null;
    }

    /**
     * 获取相关权限列表
     *
     * @param id
     * @return
     */
    private List<String> listRolesById(Long id) {
        List<String> authorities = new ArrayList<String>();
        SysUser sysUser = sysUserMapper.findSysUserById(id);
        if (sysUser != null) {
            for (SysRole role : sysUser.getRoles()) {
                authorities.addAll(role.getRolePermissions());
            }
        }
        return authorities;
    }

}