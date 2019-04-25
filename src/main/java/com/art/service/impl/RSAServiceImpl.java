/*
 *
 *
 *
 */
package com.art.service.impl;

import com.art.service.CacheService;
import com.art.service.RSAService;
import com.art.utils.RSAEncrypt;
import com.art.utils.RSAKeyUtil;
import com.art.utils.RSAUtils;
import com.art.utils.constants.Constants;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Service - RSA安全
 */
@Service("rsaServiceImpl")
public class RSAServiceImpl implements RSAService {

    @Autowired
    CacheService cacheService;

    /**
     * "私钥"参数名称
     */
    private static final String PRIVATE_KEY_ATTRIBUTE_NAME = "privateKey";

    @Transactional(readOnly = true)
    public RSAPublicKey generateKey(HttpServletRequest request) {
        Assert.notNull(request);
        KeyPair keyPair = RSAUtils.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        String publicKeyStr = Base64.encodeBase64String(publicKey.getModulus().toByteArray());
        cacheService.set(publicKeyStr, privateKey, Constants.REDIS_SESSION_EXPIRED_SECONDS);
        return publicKey;
    }

    @Transactional(readOnly = true)
    public void removePrivateKey(HttpServletRequest request) {
        Assert.notNull(request);
        HttpSession session = request.getSession();
        session.removeAttribute(PRIVATE_KEY_ATTRIBUTE_NAME);
    }

    @Transactional(readOnly = true)
    public String decryptParameter(String name, HttpServletRequest request) {
        Assert.notNull(request);
        if (name != null) {
            HttpSession session = request.getSession();
            String publicKey = request.getParameter("publicKey");
            RSAPrivateKey privateKey = (RSAPrivateKey) cacheService.getObj(publicKey);
            String parameter = request.getParameter(name);
            if (privateKey != null && StringUtils.isNotEmpty(parameter)) {
                return RSAUtils.decrypt(privateKey, parameter);
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    public String generateRSAKey(HttpServletRequest request) {

        RSAEncrypt rsa = new RSAEncrypt();
        rsa.genKeyPair();

        //转为PEM格式
        String keyPublic = RSAKeyUtil.getPEMkey(rsa.getPublicKey());
        //保存私钥
        request.getSession().setAttribute(PRIVATE_KEY_ATTRIBUTE_NAME, rsa.getPrivateKey());
        return keyPublic;
    }

    @Override
    public String decryptRSAParameter(String parameter, HttpServletRequest request) {
        Assert.notNull(request);
        if (parameter != null) {
            String publicKey = request.getParameter("publicKey");
//			Object obj = cacheService.get(publicKey);;
            RSAPrivateKey privateKey = (RSAPrivateKey) cacheService.getObj(publicKey);
            if (privateKey != null && StringUtils.isNotEmpty(parameter)) {
                //替换URLCoding
                parameter = parameter.replaceAll("%25", "%");
                parameter = parameter.replaceAll("%2B", "+");
                parameter = parameter.replaceAll("%2F", "/");
                parameter = parameter.replaceAll("%3F", "?");
                parameter = parameter.replaceAll("%23", "#");
                parameter = parameter.replaceAll("%26", "&");
                parameter = parameter.replaceAll("%3D", "=");
                parameter = parameter.replaceAll("%2B", "+");
                parameter = parameter.replaceAll("%2F", "/");
                parameter = parameter.replaceAll("%3F", "?");
                parameter = parameter.replaceAll("%25", "%");
                parameter = parameter.replaceAll("%23", "#");
                parameter = parameter.replaceAll("%26", "&");
                parameter = parameter.replaceAll("%3D", "=");

                System.out.println("parameter密文=" + parameter);
                byte[] chiperData = Base64.decodeBase64(parameter);
                byte[] plainData;
                try {
                    plainData = RSAEncrypt.decrypt(privateKey, chiperData);
                    parameter = new String(plainData);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                return parameter;
            }
        }
        return null;
    }
}