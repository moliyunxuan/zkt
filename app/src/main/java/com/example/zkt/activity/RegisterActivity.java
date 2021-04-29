package com.example.zkt.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zkt.MainActivity;
import com.example.zkt.R;
import com.example.zkt.bean.User;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.jpush.sms.SMSSDK;
import cn.leancloud.AVUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static cn.bmob.v3.Bmob.getApplicationContext;

public class RegisterActivity extends AppCompatActivity {

    private TimerTask timerTask;
    private Timer timer;
    private int timess;
    private EditText mat_username;
    private EditText verifycode;
    private Button btn_verify;
    private Button btn_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Bmob.initialize(this, "46b599eb8d07e957ca17bbc3acb774f8");
        initview();

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validate();
            }
        });
    }

    //重写onPause()用于返回登陆界面
    @Override
    protected void onPause() {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        super.onPause();
    }



    private void initview() {
        mat_username = (EditText) findViewById(R.id.mat_username);
        verifycode = (EditText) findViewById(R.id.verifycode);
        btn_verify = (Button) findViewById(R.id.btn_verify);
        btn_signup = (Button) findViewById(R.id.btn_signup);
    }



    private void validate() {
        /**
         * 如果是自定义短信模板，此处替换为你在控制台设置的自定义短信模板名称；
         * 如果没有对应的自定义短信模板，则使用默认短信模板。
         */
        final String phoneNum = mat_username.getText().toString();
        if(TextUtils.isEmpty(phoneNum)){
            Toast.makeText(RegisterActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        btn_verify.setClickable(false);
        //开始倒计时
        startTimer();
        BmobSMS.requestSMSCode(phoneNum, "找块田APP", new QueryListener<Integer>() {
            @Override
            public void done(Integer smsId, BmobException e) {
                if (e == null) {//验证码发送成功
                    Log.i("hint", "短信id：" + smsId);//用于查询本次短信发送详情
                } else {
                    String errorMessage;
                    switch (e.getErrorCode()) {
                        case 9018:
                            errorMessage = "请输入手机号码";
                            break;
                        default:
                            errorMessage = "验证码获取失败";
                            break;

                    }
                    //失败后停止计时
                    stopTimer();
                    Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_LONG).show();
                    Log.e("hint", "done: " + e.getMessage());
                    Log.e("hint", "done:  code" + e.getErrorCode());
                }
            }
        });


        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup(phoneNum);
            }
        });
    }

    private void startTimer() {

        timess = (int) (SMSSDK.getInstance().getIntervalTime()/1000);
        btn_verify.setText(timess+"s");
        if(timerTask==null){
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timess--;
                            if(timess<=0){
                                stopTimer();
                                return;
                            }
                            btn_verify.setText(timess+"s");
                        }
                    });
                }
            };
        }
        if(timer==null){
            timer = new Timer();
        }
        timer.schedule(timerTask, 1000, 1000);
    }

    private void stopTimer() {
        if(timer!=null){
            timer.cancel();
            timer=null;
        }
        if(timerTask!=null){
            timerTask.cancel();
            timerTask=null;
        }
        btn_verify.setText("重新获取");
        btn_verify.setClickable(true);

    }

    private void signup(String phoneNnmber) {

        AVUser avUser = new AVUser();// 新建 AVUser 对象实例
        avUser.setUsername(phoneNnmber);
        avUser.setPassword("056498lw");
        avUser.setMobilePhoneNumber(phoneNnmber);
        avUser.signUpInBackground().subscribe(new Observer<AVUser>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(AVUser avUser) {
                // 注册成功
                Log.d("hint", "注册成功 ");
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                RegisterActivity.this.finish();

            }

            @Override
            public void onError(Throwable e) {
                // 注册失败
                Toast.makeText(RegisterActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onComplete() {

            }
        });



    }


}



