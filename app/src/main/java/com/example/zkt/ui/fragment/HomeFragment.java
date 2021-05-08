package com.example.zkt.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.zkt.MainActivity;
import com.example.zkt.R;
import com.example.zkt.activity.CommunityActivity;
import com.example.zkt.activity.GoodsDetailsActivity;
import com.example.zkt.activity.LandDetailActivity;
import com.example.zkt.adapter.HotGridViewAdapter;
import com.example.zkt.adapter.ImageAdapter;
import com.example.zkt.adapter.RecommendGridViewAdapter;
import com.example.zkt.adapter.TopLineAdapter;
import com.example.zkt.base.Constants;
import com.example.zkt.bean.DataBean;
import com.example.zkt.bean.Goods;
import com.example.zkt.bean.Land;
import com.example.zkt.util.ListUtil;
import com.google.android.material.snackbar.Snackbar;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.transformer.ZoomOutPageTransformer;
import com.youth.banner.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class HomeFragment extends Fragment {

    @BindView(R.id.banner)
    Banner mBanner;

    @BindView(R.id.bannertop)//上下滚动的轮播文字
            Banner mbannertop;

    @BindView(R.id.iv_plant)  //农场认种
            ImageView iv_plant;

    @BindView(R.id.iv_shop)  //农场商城
            ImageView iv_shop;


    @BindView(R.id.hot_gridview)  //热评土地
            GridView hotProductGridView;

    @BindView(R.id.recommend_gridview)//推荐商品
            GridView recommendProductGridView;

    @BindView(R.id.refresh_layout) //下拉刷新的layout
            SwipeRefreshLayout mRefreshLayout;

    /**
     * 热评和推荐的两个进度条
     */
    @BindView(R.id.hot_progress)
    ProgressBar hotProgressBar;

    @BindView(R.id.recommend_progress)
    ProgressBar recommendProgressBar;

    @BindView(R.id.community_ll)
    LinearLayout communityLinearLayout;

    /**
     * 一次请求到分页土地数据
     */
    private List<Land> landItem = new ArrayList<>();


    /**
     * 一次请求到分页商品数据
     */
    private List<Goods> goodsItem = new ArrayList<>();

    /**
     * 热评土地List
     */
    private List<Land> listItemHot = new ArrayList<>();

    /**
     * 推荐商品List
     */
    private List<Goods> listItemRecommend = new ArrayList<>();


    private HotGridViewAdapter hotLandGridViewAdapter;

    private RecommendGridViewAdapter recommendProductGridViewAdapter;


    private Context mContext;

    private Unbinder unbinder;
    private View view;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1) {
                landItem = (List<Land>) msg.obj;
            }
            if(msg.what == 2) {
                goodsItem = (List<Goods>) msg.obj;
            }

            showList();

        }
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        //转存context
        mContext = getActivity().getApplicationContext();
        view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this,view);

        initBanner();

        initList();

        //iv_plant农场认领跳转
        iv_plant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity)getActivity();
                mainActivity.jumpPageFragment(R.id.item_2);
            }
        });

        //iv_shop农场商城跳转
        iv_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity)getActivity();
                mainActivity.jumpPageFragment(R.id.item_3);

            }
        });



        // 刷新监听。
        mRefreshLayout.setOnRefreshListener(mRefreshListener);

        communityLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CommunityActivity.class));
            }
        });

        return view;
    }


    /**
     * 刷新
     */
    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

            //获取商品分页数据
            initList();
//            showList();
            //判断刷新动画
            if (mRefreshLayout.isRefreshing()) {
                //停止动画
                mRefreshLayout.setRefreshing(false);
            }
        }
    };

    /**
     * 展示商品和土地数据
     *
     **/
    private void showList() {
        //随机两款土地作为热销土地
        listItemHot = ListUtil.getRandomList(landItem, 2);
        //模拟获取热评土地
        hotLandGridViewAdapter = new HotGridViewAdapter(HomeFragment.this.getContext(),listItemHot);
        hotProductGridView.setAdapter(hotLandGridViewAdapter);
        //设置gridview点击事件
        hotProductGridView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getActivity(), LandDetailActivity.class);
            //将对应的土地id传到详情界面
            intent.putExtra("ObjectId", listItemHot.get(position).getObjectId());
            startActivity(intent);
        });
        //停止显示热评商品加载动画，显示商品信息
        hotProgressBar.setVisibility(View.GONE);
        hotProductGridView.setVisibility(View.VISIBLE);

        //随机两款商品作为热销商品
        listItemRecommend = ListUtil.getRandomList(goodsItem, 2);
        //模拟获取推荐商品
        recommendProductGridViewAdapter = new RecommendGridViewAdapter(HomeFragment.this.getContext(), listItemRecommend);
        recommendProductGridView.setAdapter(recommendProductGridViewAdapter);
        //设置gridView点击事件
        recommendProductGridView.setOnItemClickListener((parent, view, position, id) -> {

            Intent intent = new Intent(getActivity(), GoodsDetailsActivity.class);
            //将对应的商品id传到详情界面
            intent.putExtra("ObjectId", listItemRecommend.get(position).getObjectId());
            startActivity(intent);

        });

        //停止显示热评商品加载动画，显示商品信息
        recommendProgressBar.setVisibility(View.GONE);
        recommendProductGridView.setVisibility(View.VISIBLE);


    }

    /**
     * 获取商品和土地数据
     *
     **/
    private void initList() {

        //获得土地数据
        AVQuery<Land> startDateQuery = new AVQuery<>("Land");
        startDateQuery.findInBackground().subscribe(new Observer<List<Land>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }
            @Override
            public void onNext(List<Land> lands) {
                Log.d("hint","主页获取土地数据成功");
//                landItem = lands;
                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = lands;
                //4、发送消息
                handler.sendMessage(msg);
            }
            @Override
            public void onError(Throwable e) {
                Log.d("hint","主页获取土地数据失败"+e.getMessage());
            }
            @Override
            public void onComplete() {

            }
        });

        //获得土地数据
        AVQuery<Goods> query = new AVQuery<>("Goods");
        query.findInBackground().subscribe(new Observer<List<Goods>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }
            @Override
            public void onNext(List<Goods> goods) {
                Log.d("hint","主页获取商品数据成功");
//                goodsItem = goods;
                Message msg = Message.obtain();
                msg.what = 2;
                msg.obj = goods;
                //4、发送消息
                handler.sendMessage(msg);


            }
            @Override
            public void onError(Throwable e) {
                Log.d("hint","主页获取商品数据失败"+e.getMessage());

            }
            @Override
            public void onComplete() {

            }
        });
    }



    /**
     * 初始化轮播图和上下滚动的公告
     */
    private void initBanner() {
        mBanner.setAdapter(new ImageAdapter(DataBean.getTestData()));
        mBanner.setIndicator(new CircleIndicator(getActivity()));
        mBanner.setBannerGalleryMZ(20);

        mbannertop.setAdapter(new TopLineAdapter(DataBean.getTestData()))
                .setOrientation(Banner.VERTICAL)
                .setPageTransformer(new ZoomOutPageTransformer())
                .setOnBannerListener((data, position) -> {
                    Snackbar.make(mbannertop, ((DataBean) data).title, Snackbar.LENGTH_SHORT).show();
                    LogUtils.d("position：" + position);
                });
    }



    @Override
    public void onDestroyView() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroyView();
    }

}