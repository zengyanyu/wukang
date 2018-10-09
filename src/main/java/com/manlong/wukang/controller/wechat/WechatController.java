package com.manlong.wukang.controller.wechat;

import com.manlong.wukang.bean.BaseData;
import com.manlong.wukang.bean.other.Data;
import com.manlong.wukang.bean.other.ReminderData;
import com.manlong.wukang.bean.other.Template;
import com.manlong.wukang.bean.other.WeixinOauth2Token;
import com.manlong.wukang.bean.vo.AppCommitVO;
import com.manlong.wukang.bean.vo.AppFullVO;
import com.manlong.wukang.bean.vo.AppointmentConfigVO;
import com.manlong.wukang.entity.wechat.*;
import com.manlong.wukang.mapper.wechat.*;
import com.manlong.wukang.utils.CommonUtil;
import com.manlong.wukang.utils.WeChatUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/wechat")
public class WechatController {

    @Autowired
    App_itemMapper app_itemMapper;

    @Autowired
    AppointmentMapper appointmentMapper;

    @Autowired
    App_holidayMapper app_holidayMapper;

    @Autowired
    App_full_dateMapper app_full_dateMapper;

    @Autowired
    Wx_configMapper wx_configMapper;

    //@DuplicateSubmitToken
    @RequestMapping(value = "/toApp.do")
    public String toApp(String code,Model model){
        if(code==null){
            return "weChat/service_busy";
        }
        WeixinOauth2Token wO2T = WeChatUtils.getOauth2AccessToken(code);
        if(wO2T==null){
            return "weChat/service_busy";
        }
        model.addAttribute("wx_openid", wO2T.getOpenId());
        /*String wx_openid = "oxrIYwM3axIAgFIGoF7BMo7ac47E";
        model.addAttribute("wx_openid", wx_openid);*/
        List<App_item> app_items = app_itemMapper.getAllApp_items();
        model.addAttribute("app_items",app_items);
        return "weChat/appList";
    }

    @RequestMapping(value = "/toAppRemind.do")
    public String toAppRemind(String wx_openid,Integer item_id,Model model){
        model.addAttribute("wx_openid",wx_openid);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = df.format(new Date());
        String url = "weChat/service_busy";
        if(item_id==1){
            url = "weChat/healthAppRemind";
        }
        if(item_id==2){
            List<Appointment> apps = appointmentMapper.getAppointmentsByApp_itemAndCurrentDate(item_id,currentDate);
            if(apps==null || apps.size()<=0){
                url = "weChat/driverAppRemind";
            }else{
                model.addAttribute("myApps",apps);
                url = "weChat/myApp";
            }
        }
        return url;
    }

    @RequestMapping(value = "/toDriverApp.do")
    public String toDriverApp(String wx_openid,Model model){
        model.addAttribute("wx_openid",wx_openid);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date beginTime = CommonUtil.getDayBegin();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(beginTime);
        String startday = df.format(calendar.getTime());
        calendar.setTime(CommonUtil.getBeginDayOfTomorrow2(beginTime,14));
        String endday = df.format(calendar.getTime());

        AppointmentConfigVO appointmentConfigVO = new AppointmentConfigVO();
        appointmentConfigVO.setMinDate(startday);
        appointmentConfigVO.setMaxDate(endday);

        List<Appointment> appointments = appointmentMapper.getAppointmentsByApp_itemAndDate_arr(2,startday,endday);

        List<App_holiday> holidays = app_holidayMapper.getHolidaysByDate_arr(startday,endday);

        if(holidays.size()>0 && holidays!=null){
            String[] holidays_arr = dateListToArr(holidays);
            model.addAttribute("holidays_arr", JSONArray.fromObject(holidays_arr));
        }

        List<App_holiday> workdays = app_holidayMapper.getWorkdaysByDate_arr(startday,endday);
        if(workdays.size()>0 && workdays!=null){
            String[] workdays_arr = dateListToArr(workdays);
            model.addAttribute("workdays_arr", JSONArray.fromObject(workdays_arr));
        }

        List<App_full_date> app_full_dates = app_full_dateMapper.getApp_full_datesByApp_itemAndFull_status(2, 10,startday,endday);
        if(app_full_dates.size()>0 && app_full_dates!=null){
            String[] fulldays_arr = dateListToArr2(app_full_dates);
            model.addAttribute("fulldays_arr", JSONArray.fromObject(fulldays_arr));
        }

        model.addAttribute("app_config_vo",appointmentConfigVO);
        return "weChat/driverApp";
    }

    //List<App_full_date>集合转化为String[]数组
    public String[] dateListToArr2(List<App_full_date> days){
        String[] days_arr = new String[days.size()];
        for(int i=0;i<days.size();i++){
            Calendar calendar = new GregorianCalendar();
            Date date = days.get(i).getFull_date();
            calendar.setTime(date);
            int y1 = calendar.get(Calendar.YEAR);
            int month1 = calendar.get(Calendar.MONTH);
            int day1 = calendar.get(Calendar.DAY_OF_MONTH);
            String hlday = y1+"-"+month1+"-"+day1;
            days_arr[i] = hlday;
        }
        return days_arr;
    }

    //List<App_holiday>集合转化为String[]数组
    public String[] dateListToArr(List<App_holiday> days){
        String[] days_arr = new String[days.size()];
        for(int i=0;i<days.size();i++){
            Calendar calendar = new GregorianCalendar();
            Date date = days.get(i).getHoliday_date();
            calendar.setTime(date);
            int y1 = calendar.get(Calendar.YEAR);
            int month1 = calendar.get(Calendar.MONTH);
            int day1 = calendar.get(Calendar.DAY_OF_MONTH);
            String hlday = y1+"-"+month1+"-"+day1;
            days_arr[i] = hlday;
        }
        return days_arr;
    }

    @RequestMapping(value = "/getAmPmCheckBox.do")
    @ResponseBody
    public AppFullVO getAmPmCheckBox(String date_str){
        AppFullVO appFullVO = new AppFullVO();
        App_full_date app_full_date = app_full_dateMapper.getApp_full_dateByApp_itemAndDate(2, date_str);
        if(app_full_date!=null){
            appFullVO.setFull_date(CommonUtil.dateToString(app_full_date.getFull_date()));
            appFullVO.setFull_status(app_full_date.getFull_status());
        }else{
            appFullVO.setFull_date(date_str);
            appFullVO.setFull_status(0);
        }
        return appFullVO;
    }

    @RequestMapping(value = "/app_driver.do")
    @ResponseBody
    public Map<String,Object> app_driver(AppCommitVO appCommitVO) throws ParseException {
        Map<String,Object> resultMap = new HashMap<String,Object>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        App_full_date app_full_date = app_full_dateMapper.getApp_full_dateByApp_itemAndDate(2, appCommitVO.getApp_date());

        if(app_full_date == null){
            App_full_date afd = new App_full_date();
            afd.setApp_item(2);
            afd.setFull_date(sdf.parse(appCommitVO.getApp_date()));
            afd.setFull_status(0);
            afd.setCreate_time(new Timestamp(System.currentTimeMillis()));
            app_full_dateMapper.insertApp_full_date(afd);
        }else{
            App_item app_item = app_itemMapper.getApp_itemById(2);
            Integer app_max_count = app_item.getApp_max_count();
            Integer app_max_am_count = app_max_count/2;
            Integer app_max_pm_count = app_max_count/2;

            int app_am_count = appointmentMapper.getWechatAppointmentCountByApp_dateAndApm(2,appCommitVO.getApp_date(),20);
            int app_pm_count = appointmentMapper.getWechatAppointmentCountByApp_dateAndApm(2,appCommitVO.getApp_date(),30);

            if(app_full_date.getFull_status()==0){
                if(appCommitVO.getApp_apm()==20){
                    if(app_am_count+1>=app_max_am_count){
                        app_full_date.setFull_status(20);
                        app_full_date.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                        app_full_dateMapper.updateApp_full_date(app_full_date);
                    }
                }
                if(appCommitVO.getApp_apm()==30){
                    if(app_pm_count+1>=app_max_pm_count){
                        app_full_date.setFull_status(30);
                        app_full_date.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                        app_full_dateMapper.updateApp_full_date(app_full_date);
                    }
                }
            }else if(app_full_date.getFull_status()==10){
                //预约满了
                resultMap.put("resultCode",1010);
                resultMap.put("resultMsg","当日已预约满");
                return resultMap;
            }else if(app_full_date.getFull_status()==20){
                if(appCommitVO.getApp_apm()==20){
                    //上午已经约满
                    resultMap.put("resultCode",1020);
                    resultMap.put("resultMsg","上午已预约满");
                    return resultMap;
                }
                if(appCommitVO.getApp_apm()==30){
                    if(app_pm_count+1>=app_max_pm_count){
                        app_full_date.setFull_status(10);
                        app_full_date.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                        app_full_dateMapper.updateApp_full_date(app_full_date);
                    }
                }
            }else if(app_full_date.getFull_status()==30){
                if(appCommitVO.getApp_apm()==20){
                    if(app_am_count+1>=app_max_am_count){
                        app_full_date.setFull_status(10);
                        app_full_date.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                        app_full_dateMapper.updateApp_full_date(app_full_date);
                    }
                }
                if(appCommitVO.getApp_apm()==30){
                    // 下午约满
                    resultMap.put("resultCode",1030);
                    resultMap.put("resultMsg","下午已预约满");
                    return resultMap;
                }
            }
        }

        Appointment appointment = new Appointment();
        appointment.setApp_apm(appCommitVO.getApp_apm());
        appointment.setApp_date(sdf.parse(appCommitVO.getApp_date()));
        appointment.setApp_item(appCommitVO.getApp_item_id());
        appointment.setId_card(appCommitVO.getId_card());
        appointment.setName(appCommitVO.getName());
        appointment.setCreate_time(new Timestamp(System.currentTimeMillis()));
        appointment.setWx_openid(appCommitVO.getWx_openid());
        if(appCommitVO.getApp_apm()==20){
            appointment.setApp_time(appCommitVO.getApp_date()+"(08:00-10:30)");
        }
        if(appCommitVO.getApp_apm()==30){
            appointment.setApp_time(appCommitVO.getApp_date()+"(13:00-15:30)");
        }
        int count = appointmentMapper.insertAppointment(appointment);
        if(count>0){
            resultMap.put("resultCode",1000);
            resultMap.put("resultMsg","预约成功");
            sendAppointment(appCommitVO.getWx_openid(),appointment);
        }
        return resultMap;
    }

    @RequestMapping(value = "/app_success.do")
    public String app_success(String wx_openid, Model model, HttpServletRequest request){

        //微信jsSDK必备参数
        Map<String, Object> map = new WeChatUtils().getWxConfig(request,wx_configMapper);
        model.addAttribute("wxconfig", map);
        model.addAttribute("wx_openid",wx_openid);
        return "weChat/appSuccess";
    }

    //预约成功发送推送消息
    public void sendAppointment(String openID,Appointment app){
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Integer app_apm = app.getApp_apm();
        String keyword3 = "";
        if(app_apm==20){
            keyword3 = sdf.format(app.getApp_date()) + "上午(08:00-10:30)";
        }
        if(app_apm==30){
            keyword3 = sdf.format(app.getApp_date()) + "下午(13:00-15:30)";
        }
        Wx_config wx_config = wx_configMapper.getLastWx_config();
        String access_token = wx_config.getAccess_token();
        Template template = new Template();
        template.setTouser(openID);
        template.setTopcolor("#FF0000");
        template.setUrl(null);
        //template.setUrl(BaseData.IPStr+"ms/toMyApp.do?openID="+openID);
        template.setTemplate_id(BaseData.APPSUCCESS_TEMPLET_ID);
        ReminderData reminderData = new ReminderData();
        reminderData.setFirst(new Data("您的预约信息如下：", "#FF0000"));
        reminderData.setKeyword1(new Data(app.getName(), "#173177"));
        reminderData.setKeyword2(new Data("驾驶员体检", "#173177"));
        reminderData.setKeyword3(new Data(keyword3, "#173177"));
        reminderData.setKeyword4(new Data("体检大楼1楼", "#173177"));
        reminderData.setRemark(new Data("注意事项:"
                + "\n1、请按照预约日期以及时间内来办理，凭身份证现场取号，先来先做，过期作废！"
                + "\n2、预约后若您有事不能来医院，请提前取消预约","#173177"));
        template.setData(reminderData);
        JSONObject jSONObject = WeChatUtils.sendTemplate(template,access_token);
        System.out.println("成功预约||微信openID："+openID+"||预约人："+app.getName()+"发送状态："+jSONObject.getString("errmsg"));
    }
}
