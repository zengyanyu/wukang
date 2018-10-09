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
public class ReminderData implements Serializable {

    private Data first;
    private Data keyword1;
    private Data keyword2;
    private Data keyword3;
    private Data keyword4;
    private Data remark;
}
