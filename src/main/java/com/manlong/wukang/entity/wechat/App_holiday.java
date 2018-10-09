package com.manlong.wukang.entity.wechat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class App_holiday {

    private Integer holiday_id;

    private Date holiday_date;

    private String holiday_name;

    private Integer holiday_status = 0;

    private String remark;

    private Date create_time;
}
