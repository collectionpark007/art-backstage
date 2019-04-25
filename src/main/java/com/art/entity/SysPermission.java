package com.art.entity;

import com.art.mapper.actable.annotation.Column;
import com.art.mapper.actable.annotation.Table;
import com.art.mapper.actable.command.BaseModel;
import com.art.mapper.actable.constants.MySqlTypeConstant;
import com.art.support.annotation.MyColumn;
import com.art.support.annotation.MyTable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 后台权限
 *
 * @Author: never
 * @Date: 2019/4/22 上午 10:30
 */
@Table(name = "sys_permission")
@MyTable("sys_permission")
public class SysPermission extends BaseModel {

    private static final long serialVersionUID = 5199201306752426433L;

    @Id
    @MyColumn("id")
    @Column(name = "id", type = MySqlTypeConstant.BIGINT, length = 21, isAutoIncrement = true, isKey = true)
    @GeneratedValue
    private Long id;

    /**
     * 归属菜单,前端判断并展示菜单使用
     */
    @MyColumn("menu_code")
    @Column(name = "menu_code", type = MySqlTypeConstant.VARCHAR)
    private String menuCode;

    /**
     * 菜单的中文释义
     */
    @MyColumn("menu_name")
    @Column(name = "menu_name", type = MySqlTypeConstant.VARCHAR)
    private String menuName;

    /**
     * 权限的代码/通配符,对应代码中@RequiresPermissions 的value
     */
    @MyColumn("permission_code")
    @Column(name = "permission_code", type = MySqlTypeConstant.VARCHAR)
    private String permissionCode;

    /**
     * 本权限的中文释义
     */
    @MyColumn("permission_name")
    @Column(name = "permission_name", type = MySqlTypeConstant.VARCHAR)
    private String permissionName;

    /**
     * 是否本菜单必选权限, 1.必选 2非必选 通常是"列表"权限是必选
     */
    @MyColumn("required_permission")
    @Column(name = "required_permission", type = MySqlTypeConstant.INT, length = 2, defaultValue = "2")
    private String requiredPermission;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getRequiredPermission() {
        return requiredPermission;
    }

    public void setRequiredPermission(String requiredPermission) {
        this.requiredPermission = requiredPermission;
    }
}
