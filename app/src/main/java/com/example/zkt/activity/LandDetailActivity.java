package com.example.zkt.activity;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.zkt.R;
import com.example.zkt.adapter.GoodsCommentAdapter;


import com.example.zkt.databinding.ActivityLandDetailBinding;
import com.example.zkt.ui.fragment.ExplainFragment;
import com.example.zkt.ui.fragment.InformaticaFragment;
import com.example.zkt.ui.widget.PopupWindowCheckChoose;
import com.example.zkt.util.ButtonClickUtils;
import com.example.zkt.util.DensityUtils;
import com.example.zkt.util.GlideUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.gyf.barlibrary.ImmersionBar;
import com.yuruiyin.appbarlayoutbehavior.AppBarLayoutBehavior;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import lsp.scrollchooseview.ScrollChooseView;


public class LandDetailActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, View.OnClickListener {

    //沉浸式状态栏
    protected ImmersionBar mImmersionBar;
    float comments_height;
    ActivityLandDetailBinding binding;
    private boolean isScrolling = false;
    private boolean isCommentClick = false;

    private ArrayList<TextView> textViews = new ArrayList<>();

    //关于下方fragment的切换
    private static final int HOME_ONE = 0;
    private static final int HOME_TWO = 1;
    private static final int HOME_THREE = 2;
    private int index;
    private int currentTabIndex = 0;

    InformaticaFragment fragment_one;//基本信息（详情）
    ExplainFragment fragment_two;  //认养介绍（详情）
    ExplainFragment fragment_three;//认养保障（详情）

    private TextView[] mTabs;
    private TextView[] mTabs_second;
    private FragmentManager manager;
    private ArrayList<Fragment> list_fragment = new ArrayList<Fragment>();


    private TextView mTvLandName;         //土地名称
    private TextView mTvDescription;      //土地内容
    private ImageView mLandPhoto;         //土地照片
    private TextView mTvLandPrice;         //商品价格

    private TextView tv_time;
    private ImageView iv_time;
    private RelativeLayout rl_content;
    private TextView tv_buy;
    private ImageView iv_service;


    private PopupWindowCheckChoose mPopup;
    private ArrayList<String> mList=new ArrayList();

    private ScrollChooseView scrollChooseView;
    String titles[] = new String[]{"无托管", "半托管", "全托管"};
    private int picIds[] = new int[]{
            R.mipmap.land_no, R.mipmap.land_half,
            R.mipmap.land_all
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_land_detail);
        mTvLandName = findViewById(R.id.txt_product_name);
        mTvDescription = findViewById(R.id.txt_product_des);
        mTvLandPrice = findViewById(R.id.txt_price);
        mLandPhoto = findViewById(R.id.land_detail_img);
        rl_content = findViewById(R.id.rl_content_land);
        iv_time = findViewById(R.id.image_alawys);
        tv_time = findViewById(R.id.tv_time_land);
        scrollChooseView = (ScrollChooseView) findViewById(R.id.id_scv);
        tv_buy = (TextView) findViewById(R.id.tv_buy) ;
        iv_service = (ImageView) findViewById(R.id.image_service);




        setListener();
        initFragment();
        initArray();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) binding.txtStatus.getLayoutParams();
        layoutParams.height = DensityUtils.getStatusBarHeight();
        mImmersionBar = ImmersionBar.with(this).statusBarDarkFont(true);
        mImmersionBar.init();
        initProduct();
        iv_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),IntroduceActivity.class);
                startActivity(intent);
            }
        });


        //选择服务种类
       scrollChooseView.setTitles(titles);
        scrollChooseView.setPicIds(picIds);
        scrollChooseView.setOnScrollEndListener(new ScrollChooseView.OnScrollEndListener() {
            @Override
            public void currentPosition(int position) {
                Log.d("msg", "当前positin=" + position + " " + titles[position]);
            }
        });


        mList=getPopList();
        mPopup = new PopupWindowCheckChoose(this, mList);
        mPopup.setTagTxt("选择租赁周期")//设置顶部title的内容
                .setButtomTxt("取消")//设置底部按钮内容
                .setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);//单选

        rl_content.setOnClickListener(v -> {
            mPopup.showPop(tv_time);
        });

        //单选
        mPopup.setOnEventLisenter(positionList -> {
            mPopup.dismiss();
            tv_time.setText(mList.get(positionList.get(0)));
        });

        tv_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),LandOrderActivity.class);
                startActivity(intent);
            }
        });


    }

    /**
     * 弹窗数据
     *
     * @return
     */


    @SuppressLint("ClickableViewAccessibility")
    private void setListener() {
        binding.setOnClickListener(this);
        binding.appBar.addOnOffsetChangedListener(this);

        //评论的点击
        binding.txtComment.setOnTouchListener((View v, MotionEvent ev) -> {
                    switch (ev.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                            if (binding.relativeTitle.getVisibility() == View.VISIBLE && !isScrolling && !binding.txtComment.isSelected()) {
                                isCommentClick = true;
                                binding.nestedScrollView.scrollTo(0, 0);
                                binding.nestedScrollView.smoothScrollTo(0, 0);
                                CoordinatorLayout.Behavior behavior =
                                        ((CoordinatorLayout.LayoutParams) binding.appBar.getLayoutParams()).getBehavior();
                                AppBarLayoutBehavior appBarLayoutBehavior = (AppBarLayoutBehavior) behavior;
                                appBarLayoutBehavior.onInterceptTouchEvent(binding.coordinator, binding.appBar, ev);

                                setAppBarLayoutOffset(binding.appBar, (int) -comments_height);
                                binding.linearTherother.setVisibility(View.GONE);
                                selectTitle(binding.txtComment);
                            }
                            break;
                    }
                    return false;
                }
        );


        //土地商品的点击
        binding.txtProduct.setOnTouchListener((View v, MotionEvent ev) -> {
                    switch (ev.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                            if (!binding.txtProduct.isSelected()) {
                                isScrolling = true;
                                binding.nestedScrollView.scrollTo(0, 0);
                                binding.nestedScrollView.smoothScrollTo(0, 0);
                                CoordinatorLayout.Behavior behavior =
                                        ((CoordinatorLayout.LayoutParams) binding.appBar.getLayoutParams()).getBehavior();
                                AppBarLayoutBehavior appBarLayoutBehavior = (AppBarLayoutBehavior) behavior;
                                appBarLayoutBehavior.onInterceptTouchEvent(binding.coordinator, binding.appBar, ev);
                                binding.appBar.setExpanded(true, true);
                                selectTitle(binding.txtProduct);
                            }
                            break;
                    }
                    return false;
                }
        );

        //详情的点击
        binding.txtDetail.setOnTouchListener((View v, MotionEvent ev) -> {
                    switch (ev.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                            if (binding.relativeTitle.getVisibility() == View.VISIBLE && !isScrolling && !binding.txtDetail.isSelected()) {
                                binding.nestedScrollView.scrollTo(0, 0);
                                binding.nestedScrollView.smoothScrollTo(0, 0);
                                CoordinatorLayout.Behavior behavior =
                                        ((CoordinatorLayout.LayoutParams) binding.appBar.getLayoutParams()).getBehavior();
                                AppBarLayoutBehavior appBarLayoutBehavior = (AppBarLayoutBehavior) behavior;
                                appBarLayoutBehavior.onInterceptTouchEvent(binding.coordinator, binding.appBar, ev);
                                setAppBarLayoutOffset(binding.appBar, -(int) (binding.appBar.getTotalScrollRange() - getResources().getDimension(R.dimen.dp_50) - DensityUtils.getStatusBarHeight()));
                                selectTitle(binding.txtDetail);
                            }
                            break;
                    }
                    return false;
                }
        );

        binding.nestedScrollView.setFadingEdgeLength(0);






    }

    @SuppressLint("CheckResult")
    private void initProduct() {
        String ObjectId = getIntent().getStringExtra("ObjectId");
        Log.d("hint", "获取成功,该土地的ObjectId是"+ ObjectId);
        AVQuery<AVObject> query = new AVQuery<>("Land");
        query.getInBackground(ObjectId).subscribe(new Observer<AVObject>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(AVObject land) {
                Log.d("hint", "获取土地详细信息成功,该土地是"+land.getString("lname"));
                mTvLandName.setText(land.getString("lname"));
                mTvDescription.setText(land.getString("lcontent"));
                GlideUtils.load(getApplicationContext(),land.getAVFile("lpicture").getUrl(), mLandPhoto);
                mTvLandPrice.setText("￥"+land.getInt("lprice"));
            }

            @Override
            public void onError(Throwable e) {

                Log.d("hint", "获取土地详细信息失败");
                Log.d("hint", "获取土地详细信息失败愿意："+e.getMessage());

            }

            @Override
            public void onComplete() {

            }
        });


        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");

        GoodsCommentAdapter commentsAdapter = new GoodsCommentAdapter();
        commentsAdapter.setDataList(arrayList);
        binding.recyclerViewComment.setAdapter(commentsAdapter);

        //这个方法是在获取商品详情接口后调用的。目的是填充数据，且测量评论区所占高度
        Observable.timer(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> {
            measure(binding.appBar.getTotalScrollRange());
        });

    }

    public ArrayList<String> getPopList() {
        ArrayList<String> popList = new ArrayList<>();
        popList.add("一季度（90天）");
        popList.add("半年");
        popList.add("一年");
        return popList;
    }

    private void measure(int total) {
        if (comments_height == 0) {
            comments_height = total - getResources().getDimension(R.dimen.dp_100) - DensityUtils.getStatusBarHeight() - binding.recyclerViewComment.getHeight();
        }
    }

    private void initArray() {

        textViews.add(binding.txtProduct);
        textViews.add(binding.txtDetail);
        textViews.add(binding.txtComment);

    }

    private void initFragment() {

        manager = getSupportFragmentManager();
        mTabs = new TextView[3];
        mTabs[0] = binding.txtTopBase;
        mTabs[1] = binding.txtTopExplain;
        mTabs[2] = binding.txtTopFuwu;


        mTabs_second = new TextView[3];
        mTabs_second[0] = binding.txtBottomBase;
        mTabs_second[1] = binding.txtBottomExplain;
        mTabs_second[2] = binding.txtBottomFuwu;

        fragment_one = new InformaticaFragment();
        fragment_two = new ExplainFragment(1);   //type=1表示详情里面的认养介绍
        fragment_three = new ExplainFragment(2); //type=2表示详情里面的认养保障

        list_fragment.add(fragment_one);
        list_fragment.add(fragment_two);
        list_fragment.add(fragment_three);
        switchFragment(R.id.txt_top_base);

    }


    @Override
    public void onClick(View v) {

        //防止快速点击
        if (ButtonClickUtils.isFastClick()) {
            return;
        }

        switch (v.getId()) {
            case R.id.txt_bottom_base:
            case R.id.txt_top_base:
                switchFragment(R.id.txt_top_base);//基本信息（详情）
                break;


            case R.id.txt_bottom_explain:

            case R.id.txt_top_explain:
                switchFragment(R.id.txt_top_explain);//认养介绍(详情)
                break;


            case R.id.txt_bottom_fuwu:
            case R.id.txt_top_fuwu:
                switchFragment(R.id.txt_top_fuwu);//认养保障(详情)
                break;
        }
    }

    private void switchFragment(int id) {
        FragmentTransaction ft = manager.beginTransaction();
        TextView relativeLayout = (TextView) findViewById(id);
        String tag = (String) relativeLayout.getTag();
        Fragment f = manager.findFragmentByTag(tag);
        if (f == null) {
            int num = Integer.parseInt(tag);
            ft.add(R.id.framLayout, list_fragment.get(num), tag);
        }

        for (int i = 0; i < list_fragment.size(); i++) {
            Fragment fragment = list_fragment.get(i);
            if (fragment.getTag() != null) {
                if (fragment.getTag().equals(tag)) {
                    ft.show(fragment);
                } else {
                    ft.hide(fragment);
                }
            }
        }
        ft.commitAllowingStateLoss();
        switch (id) {
            case R.id.txt_top_base://首页
                index = HOME_ONE;
                break;
            case R.id.txt_top_explain:
                index = HOME_TWO;
                break;
            case R.id.txt_top_fuwu:
                index = HOME_THREE;
                break;
        }
        mTabs[currentTabIndex].setSelected(false);
        mTabs_second[currentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        mTabs[index].setSelected(true);
        mTabs_second[index].setSelected(true);
        currentTabIndex = index;

    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {

        if (Math.abs(i) >= 10) {
            if (binding.relativeTitle.getVisibility() == View.GONE) {
                binding.relativeTitle.setVisibility(View.VISIBLE);
                binding.txtStatus.setVisibility(View.VISIBLE);

                Animation animation = AnimationUtils.loadAnimation(LandDetailActivity.this, R.anim.alpha_detail_come);
                binding.relativeTitle.setAnimation(animation);
                binding.txtStatus.setAnimation(animation);
                animation.start();
            }
        } else {
            if (binding.relativeTitle.getVisibility() == View.VISIBLE) {
                isScrolling = false;
                binding.relativeTitle.setVisibility(View.GONE);
                binding.txtStatus.setVisibility(View.INVISIBLE);
                Animation animation = AnimationUtils.loadAnimation(LandDetailActivity.this, R.anim.alpha_detail_go);
                binding.relativeTitle.setAnimation(animation);
                binding.txtStatus.setAnimation(animation);
                animation.start();
            }
        }


        if (comments_height != 0 && Math.abs(i) >= comments_height && Math.abs(i) < appBarLayout.getTotalScrollRange() - getResources().getDimension(R.dimen.dp_50) - DensityUtils.getStatusBarHeight()) {
            //选中评论
            if (!binding.txtComment.isSelected()) {
                selectTitle(binding.txtComment);
            }
            binding.linearTherother.setVisibility(View.GONE);
            //选中
        } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange() - getResources().getDimension(R.dimen.dp_50) - DensityUtils.getStatusBarHeight()) {
            //选中详情
            if (!isCommentClick) {
                if (!binding.txtDetail.isSelected()) {
                    selectTitle(binding.txtDetail);
                }
                binding.linearTherother.setVisibility(View.VISIBLE);
            }
            isCommentClick = false;
            //选中土地
        } else {
            if (!binding.txtProduct.isSelected()) {
                selectTitle(binding.txtProduct);
            }
            binding.linearTherother.setVisibility(View.GONE);
        }


    }

    private void selectTitle(TextView txtComment) {

        for (int i = 0; i < textViews.size(); i++) {
            if (txtComment == textViews.get(i)) {
                if (!textViews.get(i).isSelected()) {
                    textViews.get(i).setSelected(true);
                    textViews.get(i).setScaleX(1.3f);
                    textViews.get(i).setScaleY(1.3f);
                }
            } else {
                if (textViews.get(i).isSelected()) {
                    textViews.get(i).setSelected(false);
                    textViews.get(i).setScaleX(1.0f);
                    textViews.get(i).setScaleY(1.0f);
                }
            }

        }
    }
    /**
     * 设置appbar偏移量
     *
     * @param appBar
     * @param offset
     */
    public void setAppBarLayoutOffset(AppBarLayout appBar, int offset) {
        CoordinatorLayout.Behavior behavior =
                ((CoordinatorLayout.LayoutParams) appBar.getLayoutParams()).getBehavior();
        if (behavior instanceof AppBarLayout.Behavior) {

            AppBarLayout.Behavior appBarLayoutBehavior = (AppBarLayout.Behavior) behavior;
            int topAndBottomOffset = appBarLayoutBehavior.getTopAndBottomOffset();


            if (topAndBottomOffset != offset) {
                ValueAnimator valueAnimator = ValueAnimator.ofInt(appBarLayoutBehavior.getTopAndBottomOffset(), offset);
                valueAnimator.setDuration(500);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int offetOther = (int) animation.getAnimatedValue();
                        appBarLayoutBehavior.setTopAndBottomOffset(offetOther);
                        if (binding.relativeTitle.getVisibility() == View.GONE) {
                            binding.relativeTitle.setVisibility(View.VISIBLE);
                            binding.txtStatus.setVisibility(View.VISIBLE);

                            Animation animation_appBarScroll = AnimationUtils.loadAnimation(LandDetailActivity.this, R.anim.alpha_detail_come);
                            binding.relativeTitle.setAnimation(animation_appBarScroll);
                            binding.txtStatus.setAnimation(animation_appBarScroll);
                            animation_appBarScroll.start();
                        }

                    }
                });
                valueAnimator.start();
            }
        }
    }
}