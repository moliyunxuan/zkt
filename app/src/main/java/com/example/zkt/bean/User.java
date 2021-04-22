package com.example.zkt.bean;


import android.graphics.Bitmap;

import cn.leancloud.AVFile;
import cn.leancloud.AVUser;
import lombok.Data;

@Data
public class User extends AVUser {
    public String userPhone;
    public String userPassword;
    public String userName;
    public String nickName;

    public static final String AVATAR = "avatar";//用户头像





    public void setAvatar(AVFile avatar){
        put(AVATAR, avatar);
    }

    public AVFile getAvatar(){
        return getAVFile(AVATAR);
    }


}

