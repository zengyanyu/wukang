package com.manlong.wukang.controller.wechat;

import com.manlong.wukang.entity.wechat.App_item;
import com.manlong.wukang.entity.wechat.Appointment;
import com.manlong.wukang.mapper.wechat.App_itemMapper;
import com.manlong.wukang.mapper.wechat.AppointmentMapper;
import com.manlong.wukang.utils.CommonUtil;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/pc")
public class AppointmentController {

    @Autowired
    AppointmentMapper appointmentMapper;

    @Autowired
    App_itemMapper app_itemMapper;

    @RequestMapping(value = "/toOfferNumber.do")
    public String toOfferNumber(){

        return "pc/offerNumber";
    }

    @RequestMapping(value = "/orderNumber.do")
    @ResponseBody
    public synchronized Map<String,Object> orderNumber(String name,String id_card){
        Map<String,Object> resultMap = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyyMMdd");
        String currentDate = dateFormat.format(new Date());

        Appointment hasAppointment = appointmentMapper.getAppointmentByApp_dateAndId_card(currentDate, id_card);

        if(hasAppointment!=null && hasAppointment.getLocal_number()!=""){
            resultMap.put("resultCode",1010);
            resultMap.put("resultMsg","重复取号");
            resultMap.put("resultData",hasAppointment);
            return resultMap;
        }
        if(hasAppointment!=null && hasAppointment.getLocal_number()==""){
            //微信预约的人员
            if(hasAppointment.getApp_apm()==20){
                //预约上午
                int am_count = appointmentMapper.getAppointmentCountByApp_dateAndApm(2, currentDate, 20);
                hasAppointment.setLocal_number(dateFormat2.format(new Date()).substring(4, 8)+"20"+String.format("%02d", (am_count+1)));
            }else if(hasAppointment.getApp_apm()==30){
                //预约下午
                int pm_count = appointmentMapper.getAppointmentCountByApp_dateAndApm(2, currentDate, 30);
                hasAppointment.setLocal_number(dateFormat2.format(new Date()).substring(4, 8)+"30"+String.format("%02d", (pm_count+1)));
            }
            hasAppointment.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            appointmentMapper.updateAppointment(hasAppointment);
            resultMap.put("resultCode",1040);
            resultMap.put("resultMsg","微信预约取号");
            resultMap.put("resultData",hasAppointment);
            return resultMap;
        }

        int hour = CommonUtil.getHour(new Date());
        //int minute = CommonUtil.getMinute(new Date());
        Appointment appointment = new Appointment();
        appointment.setApp_item(2);
        appointment.setName(name);
        appointment.setId_card(id_card);
        appointment.setApp_date(new Date());

        App_item app_item = app_itemMapper.getApp_itemById(2);
        Integer app_max_count = app_item.getTotal_max_count();
        Integer app_max_am_count = app_max_count/2;
        Integer app_max_pm_count = app_max_count/2;

        if(hour<11){
            //上午
            appointment.setApp_apm(20);
            appointment.setApp_time(currentDate + "(08:00-10:30)");
            int am_count = appointmentMapper.getAppointmentCountByApp_dateAndApm(2, currentDate, 20);
            if(am_count>=app_max_am_count){
                resultMap.put("resultCode",1020);
                resultMap.put("resultMsg","上午号已取完");
                resultMap.put("resultData",null);
                return resultMap;
            }
            appointment.setLocal_number(dateFormat2.format(new Date()).substring(4, 8)+"20"+String.format("%02d", (am_count+1)));
        }else if(hour>=11){
            //下午hour>=13&&hour<16
            appointment.setApp_apm(30);
            appointment.setApp_time(currentDate + "(13:00-15:30)");
            int pm_count = appointmentMapper.getAppointmentCountByApp_dateAndApm(2, currentDate, 30);
            if(pm_count>=app_max_pm_count){
                resultMap.put("resultCode",1030);
                resultMap.put("resultMsg","下午号已取完");
                resultMap.put("resultData",null);
                return resultMap;
            }
            appointment.setLocal_number(dateFormat2.format(new Date()).substring(4, 8)+"30"+String.format("%02d", (pm_count+1)));
        }else{
            resultMap.put("resultCode",1090);
            resultMap.put("resultMsg","非取号时间");
            resultMap.put("resultData",null);
            return resultMap;
        }
        appointment.setCreate_time(new Timestamp(System.currentTimeMillis()));
        int count = appointmentMapper.insertAppointment(appointment);
        if(count>0){
            resultMap.put("resultCode",1000);
            resultMap.put("resultMsg","返回成功");
            resultMap.put("resultData",appointment);
        }
        return resultMap;
    }
}
