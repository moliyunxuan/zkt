package com.example.zkt.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.example.zkt.R;
import com.example.zkt.ui.fragment.AnimalOrderFragment;
import com.example.zkt.ui.fragment.GoodsOrderFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class GoodsOrderActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;



    private String[] titles = new String[]{"待支付","待发货","待收货","已完成","售后"};//托管类型

    private List<GoodsOrderFragment> pageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_order);
        viewPager = (ViewPager) findViewById(R.id.privilege_viewPager_goods);
        tabLayout = (TabLayout) findViewById(R.id.privilege_tabLayout_goods);
        initViewPager();
        initTabNormal();
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }
    private void initTabNormal() {
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initViewPager() {


        pageFragment = new ArrayList<>();

        //未完成支付state=1,支付完成后state=2
        pageFragment.add(GoodsOrderFragment.newInstance(titles[0],this,1));

        for (int i = 1; i < titles.length; i++) {
            pageFragment.add(GoodsOrderFragment.newInstance(titles[i],this,i+1));
        }

        viewPager.setAdapter(new GoodsOrderActivity.ViewPagerAdapter(getSupportFragmentManager(),1,pageFragment));

    }


    /**
     * ViewPager的适配器。
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {

        List<GoodsOrderFragment> fragmentList;

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, List<GoodsOrderFragment> fragmentList) {
            super(fm, behavior);
            this.fragmentList = fragmentList;
        }


        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentList.get(position).getTitle();
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            //   super.destroyItem(container, position, object);
        }
    }
}