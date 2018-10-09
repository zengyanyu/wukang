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
public class App_full_date {

    private Integer full_date_id;

    private Integer app_item;

    private Date full_date;

    private Integer full_status = 0;

    private Date create_time;

    private Date update_time;
}
