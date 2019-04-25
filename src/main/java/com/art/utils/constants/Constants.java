package com.art.utils.constants;

/**
 * @author: never
 * @description: 通用常量类, 单个业务的常量请单开一个类, 方便常量的分类管理
 * @date: 2019/04/22 10:15
 */
public interface Constants {

	String SUCCESS_CODE = "0000";
	String SUCCESS_MSG = "请求成功";

	/**
	 * session中存放用户信息的key值
	 */
	String SESSION_USER_INFO = "userInfo";
	String SESSION_USER_TOKEN = "user-token";
	String SESSION_USER_PERMISSION = "userPermission";

	/**
	 * redis session 有效秒数 半年
	 */
	int REDIS_SESSION_EXPIRED_SECONDS = 180 * 24 * 60 * 60;
}
