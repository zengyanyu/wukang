package com.manlong.wukang.bean.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Button implements Serializable {

    private String name;//所有一级菜单、二级菜单都共有一个相同的属性，那就是name
}
