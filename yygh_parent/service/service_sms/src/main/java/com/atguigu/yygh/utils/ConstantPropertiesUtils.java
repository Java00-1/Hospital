package com.atguigu.yygh.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConstantPropertiesUtils implements InitializingBean {

    @Value("${app.cloopen.accountSId}")
    private String accountSId;

    @Value("${app.cloopen.accountToken}")
    private String accountToken;

    @Value("${app.cloopen.appId}")
    private String appId;

    @Value("${app.cloopen.restDevUrl}")
    private String restDevUrl;

    /**
     * 定义三个常量属性对外暴露调用
     */
    public static String ACCOUNTS_ID;
    public static String ACCOUNT_TOKEN;
    public static String APP_ID;
    public static String REST_DEV_URL;

    @Override
    public void afterPropertiesSet() throws Exception {
        ACCOUNTS_ID = accountSId;
        ACCOUNT_TOKEN = accountToken;
        APP_ID = appId;
        REST_DEV_URL = restDevUrl;
    }
}
