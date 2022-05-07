package com.atguigu.yygh.service.impl;

import com.atguigu.yygh.service.SmsService;
import com.atguigu.yygh.utils.ConstantPropertiesUtils;
import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Set;

@Service
public class SmsServiceImpl implements SmsService {
    @Override
    public boolean send(String phone, String code) {
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)) {
            return false;
        }
        // 整合容联云发送短信
        // TODO 发送短信
        //生产环境请求地址：app.cloopen.com
        String serverIp = "app.cloopen.com";
        //请求端口
        String serverPort = "8883";
        //主账号,登陆云通讯网站后,可在控制台首页看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN
        String accountSId = ConstantPropertiesUtils.ACCOUNTS_ID;
        String accountToken = ConstantPropertiesUtils.ACCOUNT_TOKEN;
        //请使用管理控制台中已创建应用的APPID
        String appId = ConstantPropertiesUtils.APP_ID;
        CCPRestSmsSDK sdk = new CCPRestSmsSDK();
        sdk.init(serverIp, serverPort);
        sdk.setAccount(accountSId, accountToken);
        sdk.setAppId(appId);
        sdk.setBodyType(BodyType.Type_JSON);
        String to = phone;
        String templateId = "1";
        String[] datas = {code, "2"};
        HashMap<String, Object> result = sdk.sendTemplateSMS(to, templateId, datas);
        if ("000000".equals(result.get("statusCode"))) {
            //正常返回输出data包体信息（map）
            HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for (String key : keySet) {
                Object object = data.get(key);
                System.out.println(key + " = " + object);
            }
            return true;
        } else {
            //异常返回输出错误码和错误信息
            System.out.println("错误码=" + result.get("statusCode") + " 错误信息= " + result.get("statusMsg"));
            // TODO 发送短信结束
            return false;
        }
    }
}
