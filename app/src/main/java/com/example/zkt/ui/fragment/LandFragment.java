package com.example.zkt.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.zkt.R;
import com.example.zkt.bean.Goods;
import com.example.zkt.bean.Land;


import com.google.android.material.tabs.TabLayout;
import com.zhengsr.tablib.view.adapter.TabFlowAdapter;
import com.zhengsr.tablib.view.flow.TabFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 1.不能再fragment中用binview绑定ViewPager
 * 2.layout里面TabLayout正确表示
 * 3.用
 * Log.d("hint","topNewsAdapter" + (new ViewPagerAdapter(getFragmentManager(),0,pageFragment) == null));// 返回false,表明topNewsAdapter 不为空
 * Log.d("hint","mViewPager" + (viewPager == null));// 返回false,表明mViewPager 不为空
 */
public class
LandFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private String[] titles = new String[]{"蔬菜瓜果区","水果坚果区","粮食油料区","林木区"};//土地种类



    private List<PageFragment> pageFragment;
    private View view;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_land, container, false);
        viewPager =view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);

        initViewPager();
        initTabNormal();
        return view;

    }

    /**
     * 初始化ViewPager，方便后期与tabLayout关联
     */
    private void initViewPager() {

        pageFragment = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
           pageFragment.add(PageFragment.newInstance(titles[i]));
        }


        viewPager.setAdapter(new ViewPagerAdapter(getFragmentManager(),1,pageFragment));



    }

    /**
     * 初始化顶部标签
     */
    private void initTabNormal() {
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * ViewPager的适配器。
     */
    class ViewPagerAdapter extends FragmentPagerAdapter{

        List<PageFragment> fragmentList;

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, List<PageFragment> fragmentList) {
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