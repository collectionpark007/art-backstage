/*
 *
 *
 *
 */
package com.art.support.filter;

import com.alibaba.fastjson.JSONObject;
import com.art.config.shiro.AuthenticationToken;
import com.art.service.RSAService;
import com.art.utils.constants.ErrorEnum;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Filter - 权限认证
 */
public class AuthenticationFilter extends FormAuthenticationFilter {

    /**
     * 默认"加密密码"参数名称
     */
    private static final String DEFAULT_EN_PASSWORD_PARAM = "enPassword";

    /**
     * 默认"验证ID"参数名称
     */
    private static final String DEFAULT_CAPTCHA_ID_PARAM = "captchaId";

    /**
     * 默认"验证码"参数名称
     */
    private static final String DEFAULT_CAPTCHA_PARAM = "captcha";

    /**
     * "加密密码"参数名称
     */
    private String enPasswordParam = DEFAULT_EN_PASSWORD_PARAM;

    /**
     * "验证ID"参数名称
     */
    private String captchaIdParam = DEFAULT_CAPTCHA_ID_PARAM;

    /**
     * "验证码"参数名称
     */
    private String captchaParam = DEFAULT_CAPTCHA_PARAM;

    private RSAService rsaService;

    public AuthenticationFilter(RSAService rsaService) {
        this.rsaService = rsaService;
    }

    @Override
    protected org.apache.shiro.authc.AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        String username = getUsername(servletRequest);
        String password = getPassword(servletRequest);
        boolean rememberMe = isRememberMe(servletRequest);
        return new AuthenticationToken(username, password, rememberMe);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", ErrorEnum.E_20011.getErrorCode());
        jsonObject.put("msg", ErrorEnum.E_20011.getErrorMsg());
        PrintWriter out = null;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        try {
            res.setCharacterEncoding("UTF-8");
            res.setContentType("application/json");
            out = servletResponse.getWriter();
            out.println(jsonObject);
        } catch (Exception e) {
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
        return false;
    }

    @Override
    protected boolean onLoginSuccess(org.apache.shiro.authc.AuthenticationToken token, Subject subject, ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        Session session = subject.getSession();
        Map<Object, Object> attributes = new HashMap<Object, Object>();
        Collection<Object> keys = session.getAttributeKeys();
        for (Object key : keys) {
            attributes.put(key, session.getAttribute(key));
        }
        session.stop();
        session = subject.getSession();
        for (Entry<Object, Object> entry : attributes.entrySet()) {
            session.setAttribute(entry.getKey(), entry.getValue());
        }
        return super.onLoginSuccess(token, subject, servletRequest, servletResponse);
    }

    @Override
    protected String getPassword(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String password = rsaService.decryptParameter(enPasswordParam, request);
        rsaService.removePrivateKey(request);
        return password;
    }


    /**
     * 获取验证码
     *
     * @param servletRequest ServletRequest
     * @return 验证码
     */
    protected String getCaptcha(ServletRequest servletRequest) {
        return WebUtils.getCleanParam(servletRequest, captchaParam);
    }

    /**
     * 获取"加密密码"参数名称
     *
     * @return "加密密码"参数名称
     */
    public String getEnPasswordParam() {
        return enPasswordParam;
    }

    /**
     * 设置"加密密码"参数名称
     *
     * @param enPasswordParam "加密密码"参数名称
     */
    public void setEnPasswordParam(String enPasswordParam) {
        this.enPasswordParam = enPasswordParam;
    }

    /**
     * 获取"验证ID"参数名称
     *
     * @return "验证ID"参数名称
     */
    public String getCaptchaIdParam() {
        return captchaIdParam;
    }

    /**
     * 设置"验证ID"参数名称
     *
     * @param captchaIdParam "验证ID"参数名称
     */
    public void setCaptchaIdParam(String captchaIdParam) {
        this.captchaIdParam = captchaIdParam;
    }

    /**
     * 获取"验证码"参数名称
     *
     * @return "验证码"参数名称
     */
    public String getCaptchaParam() {
        return captchaParam;
    }

    /**
     * 设置"验证码"参数名称
     *
     * @param captchaParam "验证码"参数名称
     */
    public void setCaptchaParam(String captchaParam) {
        this.captchaParam = captchaParam;
    }

}