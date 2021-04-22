package com.example.zkt.bean;

import java.io.Serializable;

/**
 * author：moliyunxuan
 * date：created by 2021/4/21
 * description：动态图片bean
 */
public class ImageBean implements Serializable {
    private String url;//图片url

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
