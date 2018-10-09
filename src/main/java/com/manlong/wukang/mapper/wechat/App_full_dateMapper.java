package com.manlong.wukang.mapper.wechat;

import com.manlong.wukang.entity.wechat.App_full_date;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface App_full_dateMapper {

    @Select("SELECT * FROM app_full_date WHERE app_item = #{item_id} AND full_date = #{date}")
    App_full_date getApp_full_dateByApp_itemAndDate(@Param("item_id")Integer item_id, @Param("date")String date);

    @Options(useGeneratedKeys = true,keyProperty = "full_date_id")
    @Insert("INSERT INTO app_full_date(app_item,full_date,full_status,create_time,update_time) " +
            "VALUES(#{app_item},#{full_date},#{full_status},#{create_time},#{update_time})")
    int insertApp_full_date(App_full_date app_full_date);

    @Update("UPDATE app_full_date " +
            "SET app_item=#{app_item},full_date=#{full_date},full_status=#{full_status},create_time=#{create_time},update_time=#{update_time} " +
            "WHERE full_date_id=#{full_date_id}")
    int updateApp_full_date(App_full_date app_full_date);

    @Select("SELECT * FROM app_full_date WHERE app_item = #{app_item} AND full_status = #{full_status} AND full_date BETWEEN #{begin_date} AND #{end_date}")
    List<App_full_date> getApp_full_datesByApp_itemAndFull_status(@Param("app_item")Integer app_item,
                                                                  @Param("full_status")Integer full_status,
                                                                  @Param("begin_date") String begin_date,
                                                                  @Param("end_date") String end_date);
}
