package com.manlong.wukang.entity.wechat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment implements Serializable {

    private Integer app_id;//主键
    private Integer app_item;//预约项目
    private Integer wx_user_id;//微信绑定的用户ID
    private String name;//预约人姓名
    private String phone_number;//联系电话
    private String id_card;//身份证号码
    private String wx_openid;//微信openID
    private Date app_date;//预约日期
    private String app_time;//预约时间段（String）
    private Integer app_apm;//预约上下午全天（10：全天 20：上午 30：下午）
    private String app_number;//预约号码
    private String local_number;//现场取号号码
    private Timestamp create_time;//创建时间
    private Timestamp update_time;//修改时间
    private Integer status = 0;//状态：0：正常   100：删除  50：取消预约


}
