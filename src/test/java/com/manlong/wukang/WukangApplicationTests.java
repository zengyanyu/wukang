package com.manlong.wukang;

import com.manlong.wukang.config.WeChatConfig;
import com.manlong.wukang.mapper.wechat.Wx_userMapper;
import com.manlong.wukang.utils.CommonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WukangApplicationTests {

    @Test
    public void contextLoads() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date beginTime = CommonUtil.getDayBegin();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(beginTime);
        calendar.add(Calendar.DATE,-1);
        String startday = df.format(calendar.getTime());
        System.out.println(startday);

        calendar.setTime(CommonUtil.getBeginDayOfTomorrow2(beginTime,14));
        String endday = df.format(calendar.getTime());
        System.out.println(endday);
    }

}
