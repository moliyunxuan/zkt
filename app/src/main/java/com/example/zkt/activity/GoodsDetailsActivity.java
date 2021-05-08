package com.example.zkt.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zkt.R;
import com.example.zkt.bean.Goods;
import com.example.zkt.ui.widget.ObserverScrollView;
import com.example.zkt.util.GlideUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import cn.leancloud.AVUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class GoodsDetailsActivity extends AppCompatActivity implements ObserverScrollView.ScrollViewListener {

    //@BindView(R.id.detail_goods_name)   //商品名称
    private TextView mTvGoodsName;
    //@BindView(R.id.detail_goods_content)//商品内容
    private TextView mTvDescription;
    //@BindView(R.id.detail_goods_sales)  //商品销售量
    private TextView mTvSales;
    //@BindView(R.id.detail_goods_photo)  //商品照片
    private ImageView mGoodsPhoto;
    //@BindView(R.id.detail_goods_price)  //商品价格
    private TextView mGoodsPrice;
    //@BindView(R.id.detail_goods_value)  //价值
    private TextView mGoodsValue;
    //@BindView(R.id.detail_buy_price)    //到手价
    private TextView mBuyPrice;

    @BindView(R.id.detail_scroll_view)
    ObserverScrollView mScrollView;

    @BindView(R.id.detail_desc_check_detail_layout)
    RelativeLayout  mDescCheckDetailLayout;
    @BindView(R.id.detail_desc_wv_description)      //本单详情
    WebView mDescWvDescription;
    @BindView(R.id.detail_desc_wv_tips)              //温馨提示
    WebView mDescWvTips;
    @BindView(R.id.detail_desc_list_recommend)
    ListView mDescListRecommend;                     //看了又看

    @BindView(R.id.detail_title_layout)
    LinearLayout mTitleLayout;                       //商品详情页标题栏

    //@BindView(R.id.detail_layout_buy_btn)            //购买
    private Button mLayoutBuyBtn;

    private String userId ;

    //网络是否请求成功
    private boolean isRequestSuccess = false;
    //是否收藏
    private boolean isFavor = false;
    //收藏按钮是否点击
    private boolean isClickFavor = false;

    private  String ObjectId;

    private boolean flag = false;//该标志表示本商品的店铺不在购物车中

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);

        initView();
        initData();
        setViewWithIntentData();
        initScrollViewListener();
        showIsFavor();

    }

    private void initView() {
        mTvGoodsName = findViewById(R.id.detail_goods_name);
        mTvDescription = findViewById(R.id.detail_goods_content);
        mTvSales = findViewById(R.id.detail_goods_sales);
        mGoodsPhoto = findViewById(R.id.detail_goods_photo);
        mGoodsPrice =findViewById(R.id.detail_goods_price);
        mGoodsValue=findViewById(R.id.detail_goods_value);
        mBuyPrice = findViewById(R.id.detail_buy_price);
        mLayoutBuyBtn = findViewById(R.id.detail_layout_buy_btn);
        mLayoutBuyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ObjectId = getIntent().getStringExtra("ObjectId");
                Log.d("hint", "获取成功,该商品的ObjectId是"+ ObjectId);
                AVQuery<AVObject> query = new AVQuery<>("Goods");
                query.getInBackground(ObjectId).subscribe(new Observer<AVObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AVObject goods) {

                        AVQuery<AVObject> query = new AVQuery<>("shoppingCar");
                        query.whereEqualTo("merchantName",goods.getString("shopName"));
                        query.whereEqualTo("owner",userId);
                        query.getFirstInBackground().subscribe(new Observer<AVObject>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(AVObject shoppingCar) {


                                flag=true;
                                HashMap<Object, Object> goodsItem = new HashMap<>();
                                goodsItem.put("goodsId", goods.getObjectId());
                                goodsItem.put("goodsName", goods.getString("gname"));
                                goodsItem.put("goodsLogo", goods.getAVFile("gphoto").getUrl());
                                goodsItem.put("price",goods.getNumber("gprice"));//售价
                                goodsItem.put("marketPrice", goods.getNumber("gvalue"));//市场价
                                goodsItem.put("pdtDesc", goods.getString("gcontent"));//描述
                                goodsItem.put("number", 1);//购买数量,默认都为1
                                shoppingCar.addUnique("goods", goodsItem);
                                shoppingCar.saveInBackground().subscribe(new Observer<AVObject>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(AVObject avObject) {

                                        Log.d("msg", "新增加一条商品信息");
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                        Log.d("msg", "新增加一条商品失败");
                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("msg","查找购物车信息失败");

                            }

                            @Override
                            public void onComplete() {

                                if(flag==false)
                                {
                                    AVObject shoppingCar = new AVObject("shoppingCar");
                                    shoppingCar.put("merchantName",goods.getString("shopName"));//店铺名称
                                    shoppingCar.put("merID",goods.getInt("shopId"));//店铺id
                                    shoppingCar.put("owner",userId);
                                    ArrayList<HashMap> goodsBean = new ArrayList<>();
                                    HashMap<Object, Object> goodsItem = new HashMap<>();
                                    goodsItem.put("goodsId",goods.getObjectId());
                                    goodsItem.put("goodsName",goods.getString("gname"));
                                    goodsItem.put("goodsLogo", goods.getAVFile("gphoto").getUrl());
                                    goodsItem.put("price",goods.getNumber("gprice"));//售价
                                    goodsItem.put("marketPrice",goods.getNumber("gvalue"));//市场价
                                    goodsItem.put("pdtDesc",goods.getString("gcontent"));//描述
                                    goodsItem.put("number",1);//购买数量,默认都为1
                                    goodsBean.add(goodsItem);

                                    shoppingCar.put("goods",goodsBean);
                                    shoppingCar.saveInBackground().subscribe(new Observer<AVObject>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onNext(AVObject avObject) {
                                            Log.d("msg","新增加一条购物车信息");

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            Log.d("msg","增加购物车信息失败");

                                        }

                                        @Override
                                        public void onComplete() {

                                        }
                                    });
                                }


                            }
                        });





                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
















//                AVQuery<AVObject> query = new AVQuery<>("Goods");
//                query.getInBackground(ObjectId).subscribe(new Observer<AVObject>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(AVObject goods) {
//                        AVObject shoppingCar = new AVObject("shoppingCar");
//                        shoppingCar.put("goodsName",goods.getString("gname"));//商品名字
//                        shoppingCar.put("goodsLogo",goods.getAVFile("gphoto").getUrl());//商品logo
//                        shoppingCar.put("price",goods.getNumber("gprice"));//售价
//                        shoppingCar.put("marketPrice",goods.getNumber("gvalue"));//市场价
//                        shoppingCar.put("pdtDesc",goods.getString("gcontent"));//描述
//                        shoppingCar.put("number",1);//购买数量,默认都为1
//                        shoppingCar.put("isChildSelected",false);//是否选中，默认falase
//                        shoppingCar.put("isEditing",false);//是否编辑状态，默认falase
//                        shoppingCar.put("owner", AVUser.getCurrentUser().getObjectId());//加入购物车的人，这里用objectid
//                        shoppingCar.put("merchantName",goods.getString("shopName"));//店铺名称，默认为找块田自营
//                        shoppingCar.saveInBackground().subscribe(new Observer<AVObject>() {
//                            @Override
//                            public void onSubscribe(Disposable d) {
//
//                            }
//
//                            @Override
//                            public void onNext(AVObject avObject) {
//                                Log.d("msg","新增加一条购物车信息");
//
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                Log.d("msg","增加购物车信息失败");
//
//                            }
//
//                            @Override
//                            public void onComplete() {
//
//                            }
//                        });
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });



            }
        });
    }

    private void showIsFavor() {


    }

    private void initScrollViewListener() {


    }

    private void setViewWithIntentData() {


    }



    private void initData() {
        userId = AVUser.getCurrentUser().getObjectId();
        ObjectId = getIntent().getStringExtra("ObjectId");
        Log.d("hint", "获取成功,该商品的ObjectId是"+ ObjectId);

        AVQuery<AVObject> query = new AVQuery<>("Goods");
        query.getInBackground(ObjectId).subscribe(new Observer<AVObject>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(AVObject goods) {

               Log.d("hint", "获取商品信息成功,该商品是"+goods.getString("gname"));
               mTvGoodsName.setText(goods.getString("gname"));
               mTvDescription.setText(goods.getString("gcontent"));
               GlideUtils.load(getApplicationContext(),goods.getAVFile("gphoto").getUrl(), mGoodsPhoto);
//               mGoodsPhoto.setImageURI(Uri.parse(goods.getAVFile("gphoto").getUrl()));
               mTvSales.setText(goods.getInt("gsale")+"件已售");
               mGoodsPrice.setText("￥"+goods.getInt("gprice"));
               mGoodsValue.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//添加删除线
               mBuyPrice.setText("￥"+goods.getInt("gprice"));

            }

            @Override
            public void onError(Throwable e) {
                Log.d("hint", "获取商品信息失败");
                Log.d("hint", "获取商品信息失败"+e.getMessage());

            }

            @Override
            public void onComplete() {

            }
        });










    }

    @Override
    public void onScroll(ObserverScrollView scrollView, int x, int y, int oldX, int oldY) {


    }
}