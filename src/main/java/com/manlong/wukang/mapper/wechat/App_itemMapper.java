package com.manlong.wukang.mapper.wechat;

import com.manlong.wukang.entity.wechat.App_item;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface App_itemMapper {

    @Select("SELECT * FROM app_item WHERE item_id = #{item_id} AND status=0")
    App_item getApp_itemById(Integer item_id);

    @Select("SELECT * FROM app_item WHERE status=0")
    List<App_item> getAllApp_items();
}
