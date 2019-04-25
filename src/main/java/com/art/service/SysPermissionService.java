package com.art.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @author: never
 * @date: 2019/04/19 13:10
 */
public interface SysPermissionService {
	/**
	 * 查询某用户的 角色  菜单列表   权限列表
	 */
	JSONObject getUserPermission(String username);
}
