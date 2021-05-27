package com.example.zkt.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.zkt.R;
import com.example.zkt.adapter.MyExpandableListAdapter;
import com.example.zkt.data.http.VollyHelperNew;
import com.example.zkt.mvp.biz.ShoppingCartHttpBiz;
import com.example.zkt.mvp.model.ShoppingCartBean;
import com.example.zkt.mvp.biz.OnShoppingCartChangeListener;
import com.example.zkt.mvp.biz.ShoppingCartBiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import cn.leancloud.AVUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class ShoppingCarActivity extends AppCompatActivity {

    @BindView(R.id.expandableListView)
    ExpandableListView expandableListView;
    @BindView(R.id.ivSelectAll)
    ImageView ivSelectAll;
    @BindView(R.id.btnSettle)
    TextView btnSettle;
    @BindView(R.id.tvCountMoney)
    TextView tvCountMoney;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.rlShoppingCartEmpty)
    RelativeLayout rlShoppingCartEmpty;
    @BindView(R.id.rlBottomBar)
    RelativeLayout rlBottomBar;

    private List<ShoppingCartBean> mListGoods = new ArrayList<>();
    private MyExpandableListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_car);
        ButterKnife.bind(this);
        setAdapter();
        requestShoppingCartList();
        initView();
    }

    private void setAdapter() {
        adapter = new MyExpandableListAdapter(this);
        expandableListView.setAdapter(adapter);
        adapter.setOnShoppingCartChangeListener(new OnShoppingCartChangeListener() {
            @Override
            public void onDataChange(String selectCount, String selectMoney) {
                int goodsCount = ShoppingCartBiz.getGoodsCount();
//                if (!isNetworkOk) {//网络状态判断暂时不显示
//                }
                if (goodsCount == 0) {
                    showEmpty(true);
                } else {
                    showEmpty(false);//其实不需要做这个判断，因为没有商品的时候，必须退出去添加商品；
                }
                String countMoney = String.format(getResources().getString(R.string.count_money), selectMoney);
                String countGoods = String.format(getResources().getString(R.string.count_goods), selectCount);
                String title = String.format(getResources().getString(R.string.shop_title), goodsCount + "");
                tvCountMoney.setText(countMoney);
                btnSettle.setText(countGoods);
                tvTitle.setText(title);
            }

            @Override
            public void onSelectItem(boolean isSelectedAll) {
                ShoppingCartBiz.checkItem(isSelectedAll, ivSelectAll);
            }
        });
        //通过监听器关联Activity和Adapter的关系，解耦；
        View.OnClickListener listener = adapter.getAdapterListener();
        if (listener != null) {
            //即使换了一个新的Adapter，也要将“全选事件”传递给adapter处理；
            ivSelectAll.setOnClickListener(adapter.getAdapterListener());
            //结算时，一般是需要将数据传给订单界面的
            btnSettle.setOnClickListener(adapter.getAdapterListener());
        }
    }

    public void showEmpty(boolean isEmpty) {
        if (isEmpty) {
            expandableListView.setVisibility(View.GONE);
            rlShoppingCartEmpty.setVisibility(View.VISIBLE);
            rlBottomBar.setVisibility(View.GONE);
        } else {
            expandableListView.setVisibility(View.VISIBLE);
            rlShoppingCartEmpty.setVisibility(View.GONE);
            rlBottomBar.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        expandableListView.setGroupIndicator(null);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });
    }

    /** 获取购物车列表的数据（数据和网络请求也是非通用部分） */
    private void requestShoppingCartList() {
        ShoppingCartBiz.delAllGoods();

        AVQuery<AVObject> query = new AVQuery<>("shoppingCar");
        query.whereEqualTo("owner", AVUser.getCurrentUser().getObjectId());
        query.findInBackground().subscribe(new Observer<List<AVObject>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<AVObject> shoppingCars) {

                for(AVObject shoppingCar : shoppingCars)
                {
                    ShoppingCartBean shoppingCartBean = new ShoppingCartBean();

                    ArrayList<ShoppingCartBean.Goods> goods = new ArrayList<>();

                    //商品list集合
                    List goodsBeans = shoppingCar.getList("goods");
                    for(int i = 0; i < goodsBeans.size(); i++){
                        //遍历每件商品后加入goods集合
                        ShoppingCartBean.Goods goodsItem= new ShoppingCartBean.Goods();
                        HashMap<Object, Object> goodsBean = (HashMap<Object, Object>) goodsBeans.get(i);
                        goodsItem.setGoodsName((String) goodsBean.get("goodsName"));
                        goodsItem.setGoodsLogo((String) goodsBean.get("goodsLogo"));
                        goodsItem.setPrice(String.valueOf(goodsBean.get("price")));
                        goodsItem.setMkPrice(String.valueOf(goodsBean.get("marketPrice")));
                        goodsItem.setNumber(String.valueOf(goodsBean.get("number")));
                        goodsItem.setPdtDesc((String) goodsBean.get("pdtDesc"));
                        ShoppingCartBiz.addGoodToCart((String) goodsBean.get("goodsId"), "1");
                        goods.add(goodsItem);
                    }


                    shoppingCartBean.setMerchantName(shoppingCar.getString("merchantName"));
                    shoppingCartBean.setMerID(String.valueOf(shoppingCar.getInt("merID")));
                    shoppingCartBean.setGoods(goods);

                    mListGoods.add(shoppingCartBean);


//                    Log.d("msg","购物车店铺名字 "+shoppingCartBean.getMerchantName());

                }


            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {


                Log.d("msg","购物车店铺数量 "+mListGoods.size());
                ShoppingCartBiz.updateShopList(mListGoods);
                updateListView();

            }
        });


    }

    private void updateListView() {
        adapter.setList(mListGoods);
        adapter.notifyDataSetChanged();
        expandAllGroup();
    }

    /**
     * 展开所有组
     */
    private void expandAllGroup() {
        for (int i = 0; i < mListGoods.size(); i++) {
            expandableListView.expandGroup(i);
        }
    }

    /** 测试添加数据 ，添加的动作是通用的，但数据上只是添加ID而已，数据非通用 */
    private void testAddGood() {
        ShoppingCartBiz.addGoodToCart("279457f3-4692-43bf-9676-fa9ab9155c38", "6");
        ShoppingCartBiz.addGoodToCart("95fbe11d-7303-4b9f-8ca4-537d06ce2f8a", "8");
        ShoppingCartBiz.addGoodToCart("8c6e52fb-d57c-45ee-8f05-50905138801b", "9");
        ShoppingCartBiz.addGoodToCart("7d6e52fb-d57c-45ee-8f05-50905138801d", "3");
        ShoppingCartBiz.addGoodToCart("7d6e52fb-d57c-45ee-8f05-50905138801e", "3");
        ShoppingCartBiz.addGoodToCart("7d6e52fb-d57c-45ee-8f05-50905138801f", "3");
        ShoppingCartBiz.addGoodToCart("7d6e52fb-d57c-45ee-8f05-50905138801g", "3");
        ShoppingCartBiz.addGoodToCart("7d6e52fb-d57c-45ee-8f05-50905138801h", "3");
    }

}