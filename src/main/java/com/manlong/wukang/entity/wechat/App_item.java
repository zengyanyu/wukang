package com.manlong.wukang.entity.wechat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class App_item implements Serializable {

    private Integer item_id;//主键ID

    private String item_name;//预约项目名称

    private Integer app_max_count;//微信最大预约数

    private Integer local_max_count;//现场最大预约数

    private Integer total_max_count;//项目最大人数

    private String item_icon;

    private Integer status = 0;//
}
