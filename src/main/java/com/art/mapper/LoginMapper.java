package com.art.mapper;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

/**
 * @author: never
 * @description: 登录相关dao
 * @date: 2019/04/19 11:02
 */
public interface LoginMapper {

    /**
     * 根据用户名和密码查询对应的用户
     */
    JSONObject getUser(@Param("username") String username, @Param("password") String password);
}
