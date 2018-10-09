package com.manlong.wukang.mapper.wechat;


import com.manlong.wukang.entity.wechat.Wx_user;
import org.apache.ibatis.annotations.*;

public interface Wx_userMapper {

    @Select("SELECT * FROM wx_user WHERE wx_user_id=#{wx_user_id}")
    Wx_user getWxUserById(Integer wx_user_id);

    @Delete("DELETE FROM wx_user WHERE wx_user_id=#{wx_user_id}")
    int deleteWx_userById(Integer wx_user_id);

}
