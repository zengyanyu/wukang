package com.manlong.wukang.mapper.wechat;

import com.manlong.wukang.entity.wechat.App_holiday;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface App_holidayMapper {

    @Select("SELECT * FROM app_holiday " +
            "WHERE holiday_status = 10 AND holiday_date BETWEEN #{start_day} AND #{end_day}")
    List<App_holiday> getHolidaysByDate_arr(@Param("start_day")String start_day, @Param("end_day")String end_day);

    @Select("SELECT * FROM app_holiday " +
            "WHERE holiday_status = 20 AND holiday_date BETWEEN #{start_day} AND #{end_day}")
    List<App_holiday> getWorkdaysByDate_arr(@Param("start_day")String start_day, @Param("end_day")String end_day);
}
