package com.art.service.impl;

import com.art.service.CacheService;
import com.art.service.MyHttpSessionService;
import com.art.utils.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: never
 */
@Slf4j
@Service("myHttpSessionServiceImpl")
public class MyHttpSessionServiceImpl implements MyHttpSessionService {


    @Resource
    private CacheService cacheService;

    @Override
    public void removeSession(String loginKey) {

        if (StringUtils.isNotEmpty(loginKey)) {

            cacheService.delete(loginKey);
        }

    }

    @Override
    public void setMemberAttribute(String name, Object value) {
        Map<String, Object> attributesMap = getMemberAttributesMap();
        attributesMap.put(name, value);
        String loginKey = (String) attributesMap.get(Constants.SESSION_USER_INFO);
        cacheService.set(loginKey, attributesMap, Constants.REDIS_SESSION_EXPIRED_SECONDS);
    }

    private Map<String, Object> getMemberAttributesMap() {
        Map<String, Object> attributesMap = null;
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            String loginKey = (String) request.getAttribute(Constants.SESSION_USER_TOKEN);//先从request中取
            if (StringUtils.isEmpty(loginKey)) {
                loginKey = request.getHeader(Constants.SESSION_USER_TOKEN);
            }
            Object object = cacheService.getObj(loginKey);
            if (object != null) {
                attributesMap = (Map<String, Object>) cacheService.getObj(loginKey);
            }
            if (attributesMap == null) {
                attributesMap = new HashMap();
            }
            attributesMap.put(Constants.SESSION_USER_TOKEN, loginKey);
            return attributesMap;
        }
        return new HashMap();
    }

    @Override
    public Object getMemberAttribute(String name) {
        return getMemberAttributesMap().get(name);
    }

}


