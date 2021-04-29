package com.example.zkt.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zkt.MainActivity;
import com.example.zkt.R;
import com.example.zkt.bean.Category;
import com.example.zkt.bean.Goods;

import java.util.HashMap;
import java.util.Map;

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
    private Switch sw_remember_pwd;
    private SharedPreferences loginPreference;



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

        //隐藏密码
        et_psw.setTransformationMethod(PasswordTransformationMethod.getInstance());

        //记住密码
        loginPreference = getSharedPreferences("login", MODE_PRIVATE);
        ///要通过loginPreference去记录三个参数（checked，userName，password）
        boolean cheched = loginPreference.getBoolean("checked", false);
        if (cheched) {
            Map<String, Object> m = readLogin();
            if (m != null) {
                et_userPhone.setText((CharSequence) m.get("userName"));
                et_psw.setText((CharSequence) m.get("password"));
                sw_remember_pwd.setChecked(cheched);
            }
        }


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

    /**
     * 记住密码用
     * 读登录信息
     * @returnMap<String, Object>
     */
    public Map<String, Object> readLogin() {
        Map<String, Object> m = new HashMap<>();
        String userName = loginPreference.getString("userName", "");
        String password = loginPreference.getString("password", "");
        m.put("userName", userName);
        m.put("password", password);
        return m;
    }

    /**
     * 记住密码用，用于记录保存的数据
     * @param checked
     */
    public void configLoginInfo(boolean checked) {
        SharedPreferences.Editor editor = loginPreference.edit();
        editor.putBoolean("checked", sw_remember_pwd.isChecked());
        if (checked) {
            editor.putString("userName", et_userPhone.getText().toString());
            editor.putString("password", et_psw.getText().toString());
        } else {
            editor.remove("userName").remove("password");
        }
        editor.commit();
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