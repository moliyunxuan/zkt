package com.example.zkt.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zkt.R;
import com.example.zkt.adapter.GoodsOrderAdapter;
import com.example.zkt.bean.OrderBean;
import com.example.zkt.ui.widget.FullyLinearLayoutManager;
import com.facebook.drawee.gestures.GestureDetector;
import com.yuruiyin.appbarlayoutbehavior.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateOrderActivity extends AppCompatActivity implements View.OnClickListener {

    //微信支付渠道
    private static final String CHANNEL_WECHAT = "wx";
    //支付支付渠道
    private static final String CHANNEL_ALIPAY = "alipay";


    @BindView(R.id.txt_order)
    TextView txtOrder;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.rl_alipay)
    RelativeLayout mLayoutAlipay;
    @BindView(R.id.rl_wechat)
    RelativeLayout mLayoutWechat;

    @BindView(R.id.rb_alipay)
    View mRbAlipay;
    @BindView(R.id.rb_webchat)
    View mRbWechat;

    @BindView(R.id.btn_createOrder)
    Button mBtnCreateOrder;
    @BindView(R.id.txt_total)
    TextView       mTxtTotal;


   // private CartShopProvider  cartProvider;
    private GoodsOrderAdapter mAdapter;
    private String            orderNum;
    private String payChannel = CHANNEL_ALIPAY;           //默认为支付宝支付
    private float amount;

    private String TotalMoney;

    private int goodsNumber;

    private HashMap<String, RelativeLayout> channels = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);
        ButterKnife.bind(this);
        showData();
        initView();
        mBtnCreateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),PayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("data", TotalMoney);
                bundle.putInt("goodsNumber",goodsNumber);
                intent.putExtra("bun", bundle);
                startActivity(intent);
            }
        });







    }

    private void showData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        goodsNumber = bundle.getInt("goodsNumber");
        TotalMoney = bundle.getString("Totalmoney");
        ArrayList<String> shoppingImages = bundle.getStringArrayList("shoppingImages");


        List<OrderBean> orderBeans= new ArrayList<>();


        for (int i=0;i<shoppingImages.size();i++){

            OrderBean orderBean =new OrderBean();
            orderBean.setGoodsImages(shoppingImages.get(i));
            orderBean.setTotalMoney(TotalMoney);
            orderBeans.add(orderBean);
        }




        mAdapter = new GoodsOrderAdapter(orderBeans);

        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(this);
        //recyclerView外面嵌套ScrollView.数据显示不全
        layoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setAdapter(mAdapter);

    }

    private void initView() {


        channels.put(CHANNEL_ALIPAY, mLayoutAlipay);
        channels.put(CHANNEL_WECHAT, mLayoutWechat);
        mLayoutAlipay.setOnClickListener(this);
        mLayoutWechat.setOnClickListener(this);
        mTxtTotal.setText("应付款： ￥" +TotalMoney);

    }

    @Override
    public void onClick(View v) {
        selectPayChannle(v.getTag().toString());
    }

    @OnClick(R.id.rl_addr)
    public void chooseAddress(View view) {
        Intent intent = new Intent(CreateOrderActivity.this, AddressListActivity.class);
        startActivityForResult(intent, 2);
    }

    /**
     * 当前的支付渠道 以及三个支付渠道互斥 的功能
     */

    /**
     * 当前的支付渠道 以及三个支付渠道互斥 的功能
     */
    public void selectPayChannle(String paychannel) {

        for (Map.Entry<String, RelativeLayout> entry : channels.entrySet()) {
            payChannel = paychannel;
            RelativeLayout rb = entry.getValue();
            if (entry.getKey().equals(payChannel)) {
                View viewCheckBox = rb.getChildAt(2);      //这个是类似checkBox的控件
                viewCheckBox.setBackgroundResource(R.drawable.icon_check_true);
            } else {
                View viewCheckBox = rb.getChildAt(2);      //这个是类似checkBox的控件
                viewCheckBox.setBackgroundResource(R.drawable.icon_check_false);
            }

        }
    }


    @OnClick(R.id.btn_createOrder)
    public void createNewOrder(View view) {
        postNewOrder();     //提交订单
    }


    private void postNewOrder() {
    }
}