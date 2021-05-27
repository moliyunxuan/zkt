package com.example.zkt.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.zkt.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;
import cn.leancloud.AVObject;
import cn.leancloud.AVUser;
import cn.leancloud.types.AVNull;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText ed_old_password;
    private EditText ed_new_password;
    private EditText ed_new_repassword;
    private Button btn_changePassword;
    private ImageView iv_password_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initView();
        //左上角返回
        iv_password_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //修改密码
        btn_changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = ed_old_password.getText().toString();
                String newPassword = ed_new_password.getText().toString();
                String newRePassword = ed_new_repassword.getText().toString();
                if (oldPassword.isEmpty()) {
                    ed_old_password.setError("原密码不能为空！！");
                    return;
                }
                if (newPassword.isEmpty() || newPassword.length() < 4 || newPassword.length() > 10) {
                    ed_new_password.setError("请输入4到10位密码");
                    return;
                }
                if (!newPassword.equals(newRePassword)) {
                    ed_new_repassword.setError("两次输入密码不相等！！");
                    return;
                }

                AVUser.getCurrentUser().put("password",newPassword);
                AVUser.getCurrentUser().saveInBackground().subscribe(new Observer<AVObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AVObject avObject) {

                        Log.d("msg","更改密码成功");
                        Toast.makeText(getApplicationContext(),"更改密码成功",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });




                

          
                

            }
        });

    }


    private void initView() {

        iv_password_back = findViewById(R.id.iv_password_back);
        btn_changePassword = findViewById(R.id.btn_changePassword);
        ed_old_password = findViewById(R.id.ed_old_password);
        ed_new_password = findViewById(R.id.ed_new_password);
        ed_new_repassword = findViewById(R.id.ed_new_repassword);
    }
}
