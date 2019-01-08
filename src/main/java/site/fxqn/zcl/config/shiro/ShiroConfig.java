package site.fxqn.zcl.config.shiro;
/*
    作者: kat-qian
    日期: 2018/5/31 11:16
*/

import lombok.SneakyThrows;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.fxqn.zcl.utils.SpringUtils;

import java.util.*;

/**
 * shiro的配置类，将会读取配置文件对shiro进行配置
 */
@Configuration
public class ShiroConfig {

    @Value("${shiro.filter-map}")
    private String filterMapClassName;
    @Value("${shiro.login-url}")
    private String loginUrl;
    @Value("${shiro.unauthorized-url}")
    private String unauthorizedUrl;


    // shiro的安全管理器
    // 注入 shiro.realm配置的realms
    @Bean
    public SecurityManager securityManager(Realm realm) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);
        return securityManager;
    }


    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 登录界面
        shiroFilterFactoryBean.setLoginUrl(loginUrl);
        // 无权限页面
        shiroFilterFactoryBean.setUnauthorizedUrl(unauthorizedUrl);
        // 配置配置文件中的map拦截url的map集合
        Class<?> filterMapClass = Class.forName(filterMapClassName);
        Object filterMapObj = filterMapClass.newInstance();
        Map filterMap = (Map) filterMapObj;
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);

        return shiroFilterFactoryBean;
    }


    // shiro注解配置
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }


    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        return new AuthorizationAttributeSourceAdvisor();
    }

}
