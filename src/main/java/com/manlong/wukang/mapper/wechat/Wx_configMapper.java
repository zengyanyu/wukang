package com.manlong.wukang.mapper.wechat;

import com.manlong.wukang.entity.wechat.Wx_config;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

public interface Wx_configMapper {

    @Select("SELECT * FROM wx_config WHERE config_id=#{congig_id}")
    Wx_config getWx_confogById(Integer congig_id);

    @Options(useGeneratedKeys = true,keyProperty = "config_id")
    @Insert("INSERT INTO wx_config VALUES(#{config_id},#{access_token},#{jsapi_ticket},#{create_time})")
    int insertWx_config(Wx_config wx_config);

    @Select("SELECT * FROM wx_config ORDER BY config_id DESC limit 1")
    Wx_config getLastWx_config();
}
