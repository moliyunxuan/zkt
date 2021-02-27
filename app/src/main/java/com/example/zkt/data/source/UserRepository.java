package com.example.zkt.data.source;


import android.util.Log;
import android.widget.Toast;

import com.example.zkt.activity.RegisterActivity;
import com.example.zkt.bean.User;

import cn.leancloud.AVException;
import cn.leancloud.AVUser;
import cn.leancloud.callback.LogInCallback;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static cn.bmob.v3.Bmob.getApplicationContext;

public class UserRepository implements UserDataSource{


    @Override
    public void register(User user) {
        AVUser avUser = new AVUser();
        String phone = user.userPhone;
        String password = user.userPassword;
        avUser.setUsername(phone);
        avUser.setPassword(password);
        avUser.setMobilePhoneNumber(phone);


        avUser.signUpInBackground().subscribe(new Observer<AVUser>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(AVUser avUser) {
                // 注册成功
                Log.d("hint", "注册成功 ");

            }

            @Override
            public void onError(Throwable e) {
                // 注册失败
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onComplete() {

            }
        });


    }
    /**
     * 用户登录，登录失败后需要判断是否是因为手机号未验证，如果是则让用户验证。
     * @param user
     */
    @Override
    public void login(User user) {



        AVUser.loginByMobilePhoneNumber(user.userPhone,user.userPassword).subscribe(new Observer<AVUser>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(AVUser avUser) {
                // 登录成功
                Log.d("hint", "登录成功 ");
            }

            @Override
            public void onError(Throwable e) {
                // 登录失败
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onComplete() {

            }
        });

    }

    @Override
    public void requestPhoneVerify(String phone) {

    }

    @Override
    public void saveUserInfo(User user) {

    }
}


