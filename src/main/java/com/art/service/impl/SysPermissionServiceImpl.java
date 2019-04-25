package com.art.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.art.mapper.SysPermissionMapper;
import com.art.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author: never
 * @date: 2019/04/19 13:15
 */
@Service
public class SysPermissionServiceImpl implements SysPermissionService {

	@Autowired
	private SysPermissionMapper sysPermissionMapper;

	/**
	 * 查询某用户的 角色  菜单列表   权限列表
	 */
	@Override
	public JSONObject getUserPermission(String username) {
		JSONObject userPermission = getUserPermissionFromDB(username);
		return userPermission;
	}

	/**
	 * 从数据库查询用户权限信息
	 */
	private JSONObject getUserPermissionFromDB(String username) {
		JSONObject userPermission = sysPermissionMapper.getUserPermission(username);
		//管理员角色ID为1
		int adminRoleId = 1;
		//如果是管理员
		String roleIdKey = "roleId";
		if (adminRoleId == userPermission.getIntValue(roleIdKey)) {
			//查询所有菜单  所有权限
			Set<String> menuList = sysPermissionMapper.getAllMenu();
			Set<String> permissionList = sysPermissionMapper.getAllPermission();
			userPermission.put("menuList", menuList);
			userPermission.put("permissionList", permissionList);
		}
		return userPermission;
	}
}
