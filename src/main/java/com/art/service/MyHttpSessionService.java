package com.art.service;

public interface MyHttpSessionService {

    /**
     * 删除session
     *
     * @param loginKey
     */
    void removeSession(String loginKey);


    /**
     * 存储用户属性
     *
     * @param name
     * @param value
     */
    void setMemberAttribute(String name, Object value);


    /**
     * 获取用户值
     *
     * @param name
     * @return
     */
    Object getMemberAttribute(String name);

}
