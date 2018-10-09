package com.manlong.wukang.bean.material;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Material_item implements Serializable {

    private String media_id;
    private Material_content content;
    private String update_time;
}
