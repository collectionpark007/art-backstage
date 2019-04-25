package com.art.config.shiro;

import com.art.service.RSAService;
import com.art.support.filter.AuthenticationFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: never
 * @description: shiro配置类
 * @date: 2019/04/24 10:10
 */
@Configuration
public class ShiroConfiguration {
    Log log = LogFactory.getLog(getClass());

    @Autowired
    private RSAService rsaService;

    //将自己的验证方式加入容器
    @Bean
    public AuthenticationRealm initRealm() {
        log.info("####初始化 AuthenticationRealm #####");
        AuthenticationRealm realm = new AuthenticationRealm();
        realm.setAuthenticationCacheName("authorization");
        return realm;

    }

    //权限管理，配置主要是Realm的管理认证
    @Bean(name = "securityManager")
    public SecurityManager securityManager() {

        log.info("####初始化 SecurityManager #####");

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManager(initEhCacheMangerFactory().getObject());
        securityManager.setCacheManager(ehCacheManager);
        securityManager.setRealm(initRealm());

        return securityManager;
    }

    @Bean(name = "ehCacheManager")
    public EhCacheManagerFactoryBean initEhCacheMangerFactory() {
        EhCacheManagerFactoryBean ehCacheFactoryBean = new EhCacheManagerFactoryBean();
        ehCacheFactoryBean.setShared(true);
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        ehCacheFactoryBean.setConfigLocation(resourceLoader.getResource("classpath:/ehcache.xml"));
        return ehCacheFactoryBean;
    }

    @Bean(name = "cacheManager")
    public EhCacheCacheManager getCacheManager(EhCacheManagerFactoryBean ehCacheManager) {

        EhCacheCacheManager cacheManager = new EhCacheCacheManager();
        cacheManager.setCacheManager(ehCacheManager.getObject());

        return cacheManager;
    }


    //Filter工厂，设置对应的过滤条件和跳转条件
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("securityManager") SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //权限设置, 这里有拦截顺序判断，不能用hashMap
        Map<String, String> map = new LinkedHashMap<>();
        map.put("/", "anon");
        map.put("/static/**", "anon");
        map.put("/login/*", "anon");
        map.put("/api/common/*", "anon");
        map.put("/error", "anon");
        //<!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边 -->
        map.put("/admin/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);

        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("authc", new AuthenticationFilter(rsaService));
        //登出
        LogoutFilter logoutFilter = new LogoutFilter();
        filterMap.put("logout", logoutFilter);

        shiroFilterFactoryBean.setFilters(filterMap);
        return shiroFilterFactoryBean;
    }


    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName("shiroFilter");
        filterRegistration.setFilter(proxy);

        return filterRegistration;
    }

    //加入注解的使用，不加入这个注解不生效
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
