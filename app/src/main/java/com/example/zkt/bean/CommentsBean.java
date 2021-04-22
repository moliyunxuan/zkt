package com.example.zkt.bean;

import java.io.Serializable;

public class CommentsBean implements Serializable {
    private int commentId; //评论id
    private String content;//评论内容
    private SenderBean sender;//评论者信息

    public SenderBean getToReplyUser() {
        return toReplyUser;
    }

    public void setToReplyUser(SenderBean toReplyUser) {
        this.toReplyUser = toReplyUser;
    }

    private SenderBean toReplyUser; //回复评论者

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SenderBean getSender() {
        return sender;
    }

    public void setSender(SenderBean sender) {
        this.sender = sender;
    }

}