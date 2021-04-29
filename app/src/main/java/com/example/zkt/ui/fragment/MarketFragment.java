package com.example.zkt.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cjj.MaterialRefreshLayout;
import com.example.zkt.R;
import com.example.zkt.activity.GoodsDetailsActivity;
import com.example.zkt.adapter.CategoryAdapter;
import com.example.zkt.adapter.GoodsAdapter;
import com.example.zkt.base.BaseFragment;
import com.example.zkt.bean.Category;
import com.example.zkt.bean.Goods;
import com.sunfusheng.marqueeview.MarqueeView;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.leancloud.AVQuery;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;



public class MarketFragment extends BaseFragment {

    private boolean isclick = false;
    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE   = 2;
    private int state = STATE_NORMAL;         //正常情况

    //分类
    @BindView(R.id.recyclerview_category)
    RecyclerView mRecyclerView;
    //商品
    @BindView(R.id.recyclerview_wares)
    RecyclerView mRecyclerviewWares;
    //上拉刷新
    @BindView(R.id.refresh_layout)
    MaterialRefreshLayout mRefreshLayout;
    //翻页公告
    @BindView(R.id.vf_hotmessage)
    MarqueeView mVfHotMessage;

    private long current_cid;//左侧Category分类，用于保证只点击一次


    private CategoryAdapter mCategoryAdapter;
    private GoodsAdapter mGoodsAdapter;
    private List<Goods> datas;                            //商品信息
    private List<Category> categoryFirst = new ArrayList<>();//商品分类
    private List<String> mVFMessagesList;                 //上下轮播的信息

    private int currPage  = 1;       //当前是第几页
    private int totalPage = 1;       //一共有多少页
    private int pageSize  = 10;      //每页数目

    @Override
    protected void init() {
        mVFMessagesList = new ArrayList<>();
        requestCategoryData();      // 热门商品数据
        requestMessageData();        //轮播信息数据


    }


    /**
     * 请求分类数据
     **/

    private void requestCategoryData() {

        AVQuery<Category> query = new AVQuery<>("Category");
        query.findInBackground().subscribe(new Observer<List<Category>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Category> categories) {
                Log.d("分类","获取分类数据成功");
                categoryFirst=categories;
                showCategoryData();
                defaultClick();        //默认选中第0个商品类型
            }

            @Override
            public void onError(Throwable e) {
                Log.d("分类","获取分类数据失败");
                Toast.makeText(getActivity().getApplication(),e.getMessage(),Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onComplete() {

            }
        });
    }


    //默认选中第0个商品类型
    private void defaultClick() {
        if(!isclick) {
            Category category = categoryFirst.get(0);
            long cid = category.getcid();
            requestWares(cid);
        }

    }

    /**
     * 展示分类数据
     */

    private void showCategoryData() {
        mCategoryAdapter = new CategoryAdapter(categoryFirst);

        mCategoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Category category = (Category) adapter.getData().get(position);
                long cid = category.getcid();
                String Cname = category.getCname();
                isclick = true;
                defaultClick();
                if(current_cid !=  cid){
                    requestWares(cid);
                    current_cid = cid;
                }

            }
        });
        mRecyclerView.setAdapter(mCategoryAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        if (getActivity() != null) {
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        }
    }

    /**
     * 请求商品信息
     **/

    private void requestWares(long CategoryId) {

        AVQuery<Goods> startDateQuery = new AVQuery<>("Goods");
        startDateQuery.whereEqualTo(Goods.TYPE,CategoryId);
        startDateQuery.findInBackground().subscribe(new Observer<List<Goods>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Goods> list) {
                Log.d("hint","获取商品数据成功");

                datas = list;

                showGoodsData();

            }

            @Override
            public void onError(Throwable e) {
                Log.d("hint","获取商品数据失败");
                Toast.makeText(getActivity().getApplication(),e.getMessage(),Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onComplete() {

            }
        });




    }
    /**
     * 展示商品
     */

    private void showGoodsData() {

        switch (state) {
            case STATE_NORMAL:

                mGoodsAdapter = new GoodsAdapter(datas);
                mGoodsAdapter.setOnItemClickListener(new BaseQuickAdapter
                        .OnItemClickListener() {

                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                      Goods goods = (Goods) adapter.getData().get(position);
                        Intent intent = new Intent();
                        intent.putExtra("ObjectId",goods.getObjectId());
                        intent.setClass(getContext(),GoodsDetailsActivity.class);
                        startActivity(intent);
                    }
                });


                mRecyclerviewWares.setAdapter(mGoodsAdapter);
                mRecyclerviewWares.setLayoutManager(new GridLayoutManager(getContext(), 1));
                mRecyclerviewWares.setItemAnimator(new DefaultItemAnimator());
                mRecyclerviewWares.addItemDecoration(new DividerItemDecoration(getContext(),
                        DividerItemDecoration.HORIZONTAL));
                break;

        }
    }

    /**
         * 请求翻页公告数据
         **/

    private void requestMessageData() {
        mVFMessagesList.add("每日生鲜 限时特价");
        mVFMessagesList.add("新用户下单 即领38元红包");
        mVFMessagesList.add("免配送费 点击立即加购");
        mVFMessagesList.add("新鲜低价 就是实惠");
        mVFMessagesList.add("生鲜果蔬粮油 天天限时特惠");

        if (!mVFMessagesList.isEmpty()) {
            mVfHotMessage.setVisibility(View.VISIBLE);
            mVfHotMessage.startWithList(mVFMessagesList);
        } else {
            mVfHotMessage.setVisibility(View.GONE);
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        mVfHotMessage.startFlipping();
    }


    @Override
    protected int getContentResourseId() {
        return R.layout.fragment_market;
    }

}