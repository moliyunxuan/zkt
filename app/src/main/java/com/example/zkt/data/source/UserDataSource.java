package com.example.zkt.data.source;

import com.example.zkt.bean.User;

public interface UserDataSource {
    /** 注册 */
    void register(User user);

    /** 登录 */
    void login(User user);

    /** 通过手机号获取验证码 */
    void requestPhoneVerify(String phone);


    /** 保存用户信息 */
    void saveUserInfo(User user);

}
