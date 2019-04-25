package com.art.controller;

import com.alibaba.fastjson.JSONObject;
import com.art.service.RSAService;
import com.art.utils.CommonUtil;
import org.apache.commons.codec.binary.Base64;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;


/**
 * Controller - 公共
 */
@RestController
@RequestMapping("/api/common")
public class CommonController {

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;


    /**
     * 获取公钥
     */
    @GetMapping("/public_key")
    public JSONObject publicKey(HttpServletRequest request) {

        Map<String, Object> resultSet = new HashMap<>();
        RSAPublicKey publicKey = rsaService.generateKey(request);
        resultSet.put("modulus", Base64.encodeBase64String(publicKey.getModulus().toByteArray()));
        resultSet.put("exponent", Base64.encodeBase64String(publicKey.getPublicExponent().toByteArray()));
        return CommonUtil.successJson(resultSet);
    }

}
