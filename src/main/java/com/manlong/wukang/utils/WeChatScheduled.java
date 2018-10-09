package com.manlong.wukang.utils;

import com.manlong.wukang.mapper.wechat.Wx_configMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class WeChatScheduled implements SchedulingConfigurer {

    private static Logger log = LoggerFactory.getLogger(WeChatScheduled.class);

    @Autowired
    private Wx_configMapper wx_configMapper;

    //默认的任务周期2小时
    private String cron = "0 0 0/2 * * ? ";

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(new Runnable() {

            @Override
            public void run() {
                new WeChatUtils().excuteWechatTask(wx_configMapper);
            }

        }, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                return new CronTrigger(cron).nextExecutionTime(triggerContext);
            }
        });
    }

    public void setCron(String cron) {
        this.cron = cron;
    }
}
