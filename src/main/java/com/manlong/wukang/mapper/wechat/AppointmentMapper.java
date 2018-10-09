package com.manlong.wukang.mapper.wechat;

import com.manlong.wukang.entity.wechat.Appointment;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface AppointmentMapper {

    @Select("SELECT * FROM appointment " +
            "WHERE app_item = #{item_id} AND status = 0 " +
            "AND app_date BETWEEN #{begin_date} AND #{end_date}")
    List<Appointment> getAppointmentsByApp_itemAndDate_arr(
            @Param("item_id") Integer item_id,@Param("begin_date") String begin_date, @Param("end_date") String end_date);

    @Options(useGeneratedKeys = true,keyProperty = "app_id")
    @Insert("INSERT INTO appointment(app_item,wx_user_id,name,phone_number,id_card,wx_openid,app_date,app_time,app_apm,app_number,local_number,create_time,update_time,status)" +
            "VALUES(#{app_item},#{wx_user_id},#{name},#{phone_number},#{id_card},#{wx_openid},#{app_date},#{app_time},#{app_apm},#{app_number},#{local_number},#{create_time},#{update_time},#{status})")
    int insertAppointment(Appointment appointment);

    @Select("SELECT count(*) FROM appointment WHERE app_item = #{app_item} AND app_date = #{app_date} AND app_apm = #{app_apm} AND `status` = 0 AND local_number!=''")
    int getAppointmentCountByApp_dateAndApm(@Param("app_item")Integer app_item ,@Param("app_date")String app_date,@Param("app_apm")Integer app_apm);

    @Select("SELECT count(*) FROM appointment WHERE app_item = #{app_item} AND app_date = #{app_date} AND app_apm = #{app_apm} AND `status` = 0 AND wx_openid IS NOT NULL")
    int getWechatAppointmentCountByApp_dateAndApm(@Param("app_item")Integer app_item ,@Param("app_date")String app_date,@Param("app_apm")Integer app_apm);

    @Select("SELECT * FROM appointment WHERE status = 0 AND app_date = #{app_date} AND id_card=#{id_card} ORDER BY create_time DESC LIMIT 1")
    Appointment getAppointmentByApp_dateAndId_card(@Param("app_date")String app_date,@Param("id_card")String id_card);

    @Update("UPDATE appointment SET app_item=#{app_item},wx_user_id=#{wx_user_id},name=#{name},phone_number=#{phone_number}," +
            "id_card=#{id_card},wx_openid=#{wx_openid},app_date=#{app_date},app_time=#{app_time},app_apm=#{app_apm},app_number=#{app_number}," +
            "local_number=#{local_number},create_time=#{create_time},update_time=#{update_time},status=#{status} WHERE app_id = #{app_id}")
    int updateAppointment(Appointment appointment);

    @Select("SELECT * FROM appointment " +
            "WHERE app_item = #{item_id} AND status = 0 " +
            "AND app_date >= #{current_date}")
    List<Appointment> getAppointmentsByApp_itemAndCurrentDate(@Param("item_id") Integer item_id,@Param("current_date") String current_date);
}
