package com.example.zkt.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zkt.MainActivity;
import com.example.zkt.R;
import com.example.zkt.bean.Category;
import com.example.zkt.bean.Goods;

import cn.leancloud.AVLogger;
import cn.leancloud.AVOSCloud;
import cn.leancloud.AVObject;
import cn.leancloud.AVUser;
import cn.leancloud.core.AVOSService;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static cn.bmob.v3.Bmob.getApplicationContext;

public class LoginActivity extends AppCompatActivity {


    private EditText et_userPhone;
    private EditText et_psw;
    private TextView tv_register;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AVOSCloud.setLogLevel(AVLogger.Level.DEBUG);

        // 提供 this、App ID、App Key、Server Host 作为参数
        // 注意这里千万不要调用 cn.leancloud.core.AVOSCloud 的 initialize 方法，否则会出现 NetworkOnMainThread 等错误。
        AVOSCloud.initialize(this, "no7WRqvJp5Qvh0BwuPjxny98-gzGzoHsz", "rRGPQ4KgvdN0nFI4kvTNkqXW", "https://api.moliyunxuan.com");

        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        et_userPhone =(EditText) findViewById(R.id.met_userPhone);
        et_psw = (EditText)findViewById(R.id.met_pwd);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_register = (TextView) findViewById(R.id.tv_register);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                LoginActivity.this.finish();
            }
        });
    }

    private void login() {
        final String userphone = et_userPhone.getText().toString();
        final String password = et_psw.getText().toString();
        // 校验手机号
        if (TextUtils.isEmpty(userphone)) {
            Toast.makeText(LoginActivity.this,"请输入手机号码",Toast.LENGTH_SHORT).show();
            return;
        } else if (userphone.length() != 11) {
            Toast.makeText(LoginActivity.this,"请填写11位手机号",Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(LoginActivity.this,"请输入密码",Toast.LENGTH_LONG).show();
            return;
        }
        AVUser.loginByMobilePhoneNumber(userphone,password).subscribe(new Observer<AVUser>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(AVUser avUser) {
                // 登录成功
                Log.d("hint", "登录成功 ");
                LoginActivity.this.finish();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));

            }

            @Override
            public void onError(Throwable e) {
                // 登录失败
                Log.d("hint", "登录失败 ");
                Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onComplete() {

            }
        });

    }







}