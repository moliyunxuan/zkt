package com.example.zkt.bean;

import java.io.Serializable;

/**
 * date：created by 2021/4/21
 * descript：回复功能
 */
public class CommentConfig implements Serializable {
    public enum Type {
        PUBLIC("public"), REPLY("reply");
        private String value;

        Type(String value) {
            this.value = value;
        }
    }

    public int circlePosition; //位置
    public int commentPosition; //评论位置
    public SenderBean replyUser;
    public Type commentType;

    @Override
    public String toString() {
        String replyUserStr = "";
        if (replyUser != null) {
            replyUserStr = replyUser.toString();
        }
        return "circlePosition = " + circlePosition
                + "; commentPosition = " + commentPosition
                + "; commentType ＝ " + commentType
                + "; replyUser = " + replyUserStr;
    }
}
