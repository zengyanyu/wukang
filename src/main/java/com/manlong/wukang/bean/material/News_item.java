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
public class News_item implements Serializable {

    private String title;
    private String thumb_media_id;
    private String show_cover_pic;
    private String author;
    private String digest;
    private String content;
    private String url;
    private String content_source_url;
}
