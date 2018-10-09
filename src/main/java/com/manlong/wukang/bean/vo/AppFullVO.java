package com.manlong.wukang.bean.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppFullVO {

    private String full_date;

    private Integer full_status; // 满的状态（10：全天 20：上午 30：下午）

}
