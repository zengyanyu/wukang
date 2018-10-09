package com.manlong.wukang.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@PropertySource(value = "classpath:wechat.yml")
public class WeChatConfig {

    @Value("${WECHAT_APPID}")
    private String WECHAT_APPID;

    @Value("${WECHAT_APPSECRET}")
    private String WECHAT_APPSECRET;

    @Value("${PAGESIZE}")
    private Integer PAGESIZE;

    @Value("${IPStr}")
    private String IPStr;

    @Value("${CANCELAPP_TEMPLET_ID}")
    private String CANCELAPP_TEMPLET_ID;

    @Value("${APPSUCCESS_TEMPLET_ID}")
    private String APPSUCCESS_TEMPLET_ID;

}
