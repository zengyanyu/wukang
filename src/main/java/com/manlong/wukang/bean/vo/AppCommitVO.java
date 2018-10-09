package com.manlong.wukang.bean.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppCommitVO{

    private Integer app_item_id;

    private String wx_openid;

    private String name;

    private String id_card;

    private String app_date;

    private String app_time;

    private Integer app_apm;
}
