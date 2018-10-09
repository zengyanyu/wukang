package com.manlong.wukang.entity.wechat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Wx_config implements Serializable {

    private Integer config_id;

    private String access_token;

    private String jsapi_ticket;

    private Timestamp create_time;

}
