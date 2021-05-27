package com.example.zkt.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zkt.R;
import com.example.zkt.mvp.IModel;
import com.example.zkt.mvp.model.ModelTest;
import com.example.zkt.util.ToastUtils;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import cn.leancloud.AVObject;
import cn.leancloud.AVUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class PayActivity extends AppCompatActivity {

    private Button gotopay;
    private TextView tvname;
    private TextView tvprice;

    private String orderId = "";
    private String alipayqr = "";

    private  String money;
    private  int goodsNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        initData();
        initView();
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                getOrderState();
            }
        }
    };

    private Timer timer = new Timer(true);

    //任务
    private TimerTask task = new TimerTask() {
        public void run() {
            Message msg = new Message();
            msg.what = 1;
            handler.sendMessage(msg);
        }
    };



    private void initView() {

        gotopay = findViewById(R.id.gotopay);
        tvname = findViewById(R.id.tv_name);
        tvprice = findViewById(R.id.tv_price);
        gotopay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //下单成功，即把数据上传到云端
                //付款条件较为苛刻，后期可将该部分代码放到付款成功中

                String owner= AVUser.getCurrentUser().getMobilePhoneNumber();
                AVObject goodsOrder = new AVObject("GoodsOrder");
                goodsOrder.put("owner",owner);
                goodsOrder.put("state",1);
                goodsOrder.put("orderPrice",money);
                goodsOrder.put("goodsNumber",goodsNumber);
                goodsOrder.saveInBackground().subscribe(new Observer<AVObject>() {


                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AVObject avObject) {

                        Log.d("msg","创建商品订单成功");
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.d("msg","创建商品订单失败"+e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
                if (!TextUtils.isEmpty(alipayqr)) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(alipayqr)));
                } else {
                    Toast.makeText(getApplicationContext(),"数据初始化有问题",Toast.LENGTH_SHORT).show();
                    randomCode();
                }
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bun");
        money = bundle.getString("data");
        goodsNumber =bundle.getInt("goodsNumber");
        randomCode();
        createOrder();
    }

    private void createOrder() {

        ModelTest.getInstance().createOrder("XXXXXXXXXXXXXXXXXX开通站长付平台超级会员", money, new IModel.AsyncCallback() {
            @Override
            public void onSuccess(Object obj) {
                try {
                    JSONObject jsonObject = new JSONObject(obj.toString());
                    if (!jsonObject.getString("code").equals("10001")) {
                        Toast.makeText(getApplicationContext(),jsonObject.getString("msg") + "",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    tvname.setText(jsonObject.getString("name"));
                    tvprice.setText(jsonObject.getString("price"));
                    orderId = jsonObject.getString("orderId");
                    //启动定时器
                    timer.schedule(task, 0, 5000);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("msg111","问题1");
                    Toast.makeText(getApplicationContext(),"数据有误",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onError(Object obj) {
                Toast.makeText(getApplicationContext(),"服务器或者网络错误",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void randomCode() {

        ModelTest.getInstance().randomCode(new IModel.AsyncCallback() {
            @Override
            public void onSuccess(Object obj) {
                try {
                    JSONObject jsonObject = new JSONObject(obj.toString());
                    alipayqr = jsonObject.getString("qrcode");
                    Log.d("msg333","alipay: "+alipayqr);
                } catch (Exception e) {
                    Log.d("msg111","问题2");
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"数据有误",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Object obj) {
                Toast.makeText(getApplicationContext(),"服务器或者网络错误",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getOrderState() {
        ModelTest.getInstance().getOrderState(orderId, new IModel.AsyncCallback() {
            @Override
            public void onSuccess(Object obj) {
                try {
                    JSONObject jsonObject = new JSONObject(obj.toString());
                    if (!jsonObject.getString("code").equals("10001")) {
                        return;
                    }




                    AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(PayActivity.this);
                    alertdialogbuilder.setMessage("提示：恭喜您支付成功");
                    alertdialogbuilder.setPositiveButton("确定", null);
                    alertdialogbuilder.setNeutralButton("取消", null);
                    final AlertDialog alertdialog1 = alertdialogbuilder.create();
                    alertdialog1.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Object obj) {
            }
        });
    }
}