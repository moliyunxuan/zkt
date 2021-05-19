package com.example.zkt.bean;

public class DynamicPhotoItem {
    private String filePath;
    private boolean isPick;//标识是不是+

    public DynamicPhotoItem() {
    }

    public DynamicPhotoItem(String filePath, boolean isPick) {
        this.filePath = filePath;
        this.isPick = isPick;
    }


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isPick() {
        return isPick;
    }

    public void setPick(boolean pick) {
        isPick = pick;
    }


}