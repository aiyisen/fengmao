package com.qy.ntf.config;

import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

  @Autowired private MyShiroRealm myShiroRealm;
  @Autowired private StringRedisTemplate redisTemplate;

  /**
   * 禁用session
   *
   * @return
   */
  @Bean(name = "securityManager")
  public SecurityManager securityManager() {
    DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
    securityManager.setRealm(myShiroRealm);
    DefaultSessionManager defaultSessionManager = new DefaultSessionManager();
    defaultSessionManager.setSessionValidationSchedulerEnabled(false);
    // 禁用session
    securityManager.setSubjectFactory(new StatelessDefaultSubjectFactory());
    securityManager.setSessionManager(defaultSessionManager);
    // 禁用Session作为存储策略的实现。
    DefaultSubjectDAO defaultSubjectDAO = (DefaultSubjectDAO) securityManager.getSubjectDAO();
    DefaultSessionStorageEvaluator defaultSessionStorageEvaluatord =
        (DefaultSessionStorageEvaluator) defaultSubjectDAO.getSessionStorageEvaluator();
    defaultSessionStorageEvaluatord.setSessionStorageEnabled(false);
    return securityManager;
  }

  /**
   * shirFilter过滤器
   *
   * @param securityManager
   * @return
   */
  @Bean("shirFilter")
  public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
    ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

    // 自定义过滤器
    Map<String, Filter> filterMap = new HashMap<>();
    JWTTokenFilter jwtTokenFilter = new JWTTokenFilter();
    jwtTokenFilter.setRedisTemplate(redisTemplate);
    filterMap.put("authcToken", jwtTokenFilter);
    shiroFilterFactoryBean.setFilters(filterMap);

    shiroFilterFactoryBean.setSecurityManager(securityManager);

    LinkedHashMap<String, String> whiteUrl = new LinkedHashMap<>();
    whiteUrl.put("/local/*", "anon");
    whiteUrl.put("/login", "anon");
    whiteUrl.put("/equLogin", "anon");
    whiteUrl.put("/doc.html", "anon");
    whiteUrl.put("/v2/api-docs", "anon");
    whiteUrl.put("/*.js", "anon");
    whiteUrl.put("/*.ico", "anon");
    whiteUrl.put("/webjars/springfox-swagger-ui/**", "anon");
    whiteUrl.put("/*.html", "anon");
    whiteUrl.put("/*.json", "anon");
    whiteUrl.put("/*.jpg", "anon");
    whiteUrl.put("*.jpeg", "anon");
    whiteUrl.put("/conWithdraw/notify", "anon");
    whiteUrl.put("/sysUserAdmin/userOpenNotify", "anon");
    whiteUrl.put("/storeTreasure/joinBlodCallBack", "anon");
    whiteUrl.put("*.png", "anon");
    whiteUrl.put("/error", "anon");
    whiteUrl.put("/sysAppVersion/list", "anon");
    whiteUrl.put("/webjars/**", "anon");
    whiteUrl.put("/openIdBind", "anon");
    whiteUrl.put("/loginByOpenId", "anon");
    whiteUrl.put("/sendCode/**", "anon");
    whiteUrl.put("/updatePass", "anon");
    whiteUrl.put("/sysMessage/appPageList", "anon");
    whiteUrl.put("/swagger-resources/**", "anon");

    whiteUrl.put("/sendCode/", "anon");
    whiteUrl.put("/regWithPass", "anon");
    whiteUrl.put("/loginWithPass", "anon");
    whiteUrl.put("/storeProPool/page", "anon");
    whiteUrl.put("/loginWithCode", "anon");
    whiteUrl.put("/refreshToken", "anon");
    whiteUrl.put("/appGetPlate", "anon");
    whiteUrl.put("/sysBanner/list/*", "anon");
    whiteUrl.put("/storeTreasure/appPageList", "anon");
    whiteUrl.put("/storeProduct/appPageList", "anon");
    whiteUrl.put("/storeProduct/getNeedTreasure/*", "anon");
    whiteUrl.put("/storeProPool/appPageList", "anon");
    whiteUrl.put("/notify/**", "anon");
    whiteUrl.put("/storeTreasure/checkedUserPhone", "anon");
    whiteUrl.put("/storeTreasure/bindBoxByAppPage", "anon");
    whiteUrl.put("/getMobileByToken", "anon");
    whiteUrl.put("/robots.txt", "anon");
    whiteUrl.put("/storeCategroy/list", "anon");
    whiteUrl.put("/storeProPool/getPoolPriceRecord/**", "anon");
    whiteUrl.put("/userinfoByPhone/**", "anon");
    whiteUrl.put("/storeProPool/getPoolPriceRecordByPage", "anon");
    whiteUrl.put("/storeTreasure/checkedUserPhone/*", "anon");
    whiteUrl.put("/verifyCode/**", "anon");
    whiteUrl.put("/storeTreasure/launchCalendar", "anon");
    whiteUrl.put("/sysPlateConfig/helpList", "anon");
    whiteUrl.put("/sysPlateConfig/appGetPlate", "anon");
    whiteUrl.put("/sysDictonary/list/**", "anon");
    whiteUrl.put("/sysDictonary/valueList/**", "anon");
    whiteUrl.put("/sysHelp/page", "anon");
    whiteUrl.put("/sysSeries/appPage", "anon");
    //    whiteUrl.put("/app/updateUserBalance/**", "anon");

    whiteUrl.put("/**", "authcToken");
    shiroFilterFactoryBean.setFilterChainDefinitionMap(whiteUrl);

    return shiroFilterFactoryBean;
  }

  /**
   * 开启shiro aop注解支持. 使用代理方式;所以需要开启代码支持;
   *
   * @param securityManager
   * @return
   */
  @Bean
  public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
      SecurityManager securityManager) {
    AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor =
        new AuthorizationAttributeSourceAdvisor();
    authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
    return authorizationAttributeSourceAdvisor;
  }

  @Bean
  public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
    DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
    advisorAutoProxyCreator.setProxyTargetClass(true);
    return advisorAutoProxyCreator;
  }
}
