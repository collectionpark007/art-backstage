package com.art.entity;

import com.art.mapper.actable.annotation.Column;
import com.art.mapper.actable.annotation.Table;
import com.art.mapper.actable.constants.MySqlTypeConstant;
import com.art.support.annotation.MyColumn;
import com.art.support.annotation.MyTable;

/**
 * 后台权限
 *
 * @Author: never
 * @Date: 2019/4/22 上午 10:30
 */
@Table(name = "sys_role_permission")
@MyTable("sys_role_permission")
public class SysRolePermission extends BaseEntity {

    private static final long serialVersionUID = 5199301306752426433L;

    /**
     * 归属菜单,前端判断并展示菜单使用
     */
    @MyColumn("role_id")
    @Column(name = "role_id", type = MySqlTypeConstant.BIGINT, length = 21)
    @javax.persistence.Column(columnDefinition = "COMMENT '归属菜单'")
    private String roleId;

    /**
     * 菜单的中文释义
     */
    @MyColumn("permission_id")
    @Column(name = "permission_id", type = MySqlTypeConstant.BIGINT, length = 21)
    @javax.persistence.Column(name = "permission_id", columnDefinition = "COMMENT '菜单的中文释义'")
    private String permissionId;


    /**
     * 是否有效 1 有效 2 无效
     */
    @MyColumn("delete_status")
    @Column(name = "delete_status", type = MySqlTypeConstant.INT, length = 2, defaultValue = "1")
    @javax.persistence.Column(columnDefinition = "COMMENT '是否有效 1 有效 2 无效'")
    private String deleteStatus;


}
