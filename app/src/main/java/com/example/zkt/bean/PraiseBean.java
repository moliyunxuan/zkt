package com.example.zkt.bean;

import java.io.Serializable;


/**
 * author：moliyunxuan
 * date：created by 2021/4/20
 * description：点赞实体
 */
public class PraiseBean implements Serializable {
    private String id;
    private SenderBean senderBean;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SenderBean getSenderBean() {
        return senderBean;
    }

    public void setSenderBean(SenderBean senderBean) {
        this.senderBean = senderBean;
    }
}
