package com.art.entity;

import com.art.mapper.actable.annotation.Column;
import com.art.mapper.actable.annotation.Table;
import com.art.mapper.actable.constants.MySqlTypeConstant;
import com.art.support.annotation.MyColumn;
import com.art.support.annotation.MyTable;

import java.util.ArrayList;
import java.util.List;

/**
 * 后台权限
 *
 * @Author: never
 * @Date: 2019/4/22 上午 10:39
 */
@Table(name = "sys_role")
@MyTable("sys_role")
public class SysRole extends BaseEntity {

    private static final long serialVersionUID = 4299201306752426433L;

    /**
     * 角色名
     */
    @MyColumn("role_name")
    @javax.persistence.Column(columnDefinition = "varchar(20) COMMENT '角色名'")
    @Column(name = "role_name", type = MySqlTypeConstant.VARCHAR, length = 20)
    private String roleName;

    /**
     * 是否有效  1有效  2无效
     */
    @MyColumn("delete_status")
    @javax.persistence.Column(columnDefinition = "tinyint(2) COMMENT '是否有效  1有效  2无效'")
    @Column(name = "delete_status", type = MySqlTypeConstant.INT, length = 2, defaultValue = "1")
    private String deleteStatus;

    /** 权限 */
    private List<String> rolePermissions = new ArrayList();

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(String deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public List<String> getRolePermissions() {
        return rolePermissions;
    }

    public void setRolePermissions(List<String> rolePermissions) {
        this.rolePermissions = rolePermissions;
    }
}
