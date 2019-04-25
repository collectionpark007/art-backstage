/*
 *
 *
 *
 */
package com.art.support;

import java.io.Serializable;

/**
 * 身份信息
 */
public class Principal implements Serializable {

    private static final long serialVersionUID = 5798882004228239559L;

    /**
     * ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * email
     */
    private String email;


    /**
     * @param id       ID
     * @param username 用户名
     */
    public Principal(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    /**
     * @param id       ID
     * @param username 用户名
     */
    public Principal(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    /**
     * 获取ID
     *
     * @return ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置ID
     *
     * @param id ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取用户名
     *
     * @return 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return username;
    }


    /**
     * 获取 email
     *
     * @return email email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * 设置 email
     *
     * @param email email
     */
    public void setEmail(String email) {
        this.email = email;
    }
}