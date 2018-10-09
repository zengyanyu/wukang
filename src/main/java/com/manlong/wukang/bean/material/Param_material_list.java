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
public class Param_material_list implements Serializable {
    /**
     * type:素材类型（image/video/voice/news）
     * offset:从全部素材的该偏移位置开始返回，0表示从第一个素材 返回
     * count:返回素材的数量，取值在1到20之间
     */
    private String type;
    private Integer offset;
    private Integer count;
}
