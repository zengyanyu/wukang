package com.manlong.wukang.config;

import com.manlong.wukang.mapper.wechat.Wx_configMapper;
import com.manlong.wukang.utils.WeChatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AfterServiceStarted implements ApplicationRunner {

    private static Logger log = LoggerFactory.getLogger(AfterServiceStarted.class);

    @Autowired
    Wx_configMapper wx_configMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //System.out.println("执行");
        new WeChatUtils().excuteWechatTask(wx_configMapper);
    }
}
