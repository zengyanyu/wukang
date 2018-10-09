package com.manlong.wukang.bean.other;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Template implements Serializable {
    private String touser;

    private String template_id;

    private String url;

    private String topcolor;

    private ReminderData data;
}
