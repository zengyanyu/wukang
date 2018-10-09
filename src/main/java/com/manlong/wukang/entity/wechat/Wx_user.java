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
public class Wx_user implements Serializable {

    private Integer wx_user_id;

    private String wx_user_openid;

    private String wx_user_name;

    private String wx_user_idcard;

    private String wx_user_phone;

    private Integer status = 0;

    private Timestamp create_time;

    private Timestamp update_time;
}
