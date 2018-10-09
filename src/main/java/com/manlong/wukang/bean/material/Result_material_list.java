package com.manlong.wukang.bean.material;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Result_material_list implements Serializable {
    private Integer total_count;
    private Integer item_count;
    private List<Material_item> item;
}
