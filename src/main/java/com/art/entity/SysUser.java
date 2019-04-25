package com.art.entity;

import com.art.mapper.actable.annotation.Column;
import com.art.mapper.actable.annotation.Table;
import com.art.mapper.actable.constants.MySqlTypeConstant;
import com.art.support.annotation.MyColumn;
import com.art.support.annotation.MyTable;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author: never
 * @Date: 2019/4/22 下午10:33
 */
@Table(name = "sys_user")
@MyTable("sys_user")
public class SysUser extends BaseEntity {

    private static final long serialVersionUID = 5199200306752426433L;

    /**
     * "身份信息"参数名称
     */
    public static final String PRINCIPAL_ATTRIBUTE_NAME = SysUser.class.getName() + ".PRINCIPAL";

    @MyColumn("username")
    @Column(name = "username", type = MySqlTypeConstant.VARCHAR, length = 128)
    private String username;

    @MyColumn("password")
    @Column(name = "password", type = MySqlTypeConstant.VARCHAR, length = 128)
    private String password;

    @MyColumn("nickname")
    @Column(name = "nickname", type = MySqlTypeConstant.VARCHAR, length = 128)
    private String nickname;

    @MyColumn("role_id")
    @Column(name = "role_id", type = MySqlTypeConstant.BIGINT, length = 21)
    private String roleId;

    @MyColumn("delete_status")
    @Column(name = "delete_status", type = MySqlTypeConstant.INT, length = 2, defaultValue = "1")
    private String deleteStatus;

    /** 角色 */
    private Set<SysRole> roles = new HashSet();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(String deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public Set<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<SysRole> roles) {
        this.roles = roles;
    }
}
