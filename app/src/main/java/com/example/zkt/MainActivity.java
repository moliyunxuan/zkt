package com.example.zkt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.example.zkt.bean.Category;
import com.example.zkt.bean.Goods;
import com.example.zkt.ui.fragment.HomeFragment;
import com.example.zkt.ui.fragment.LandFragment;
import com.example.zkt.ui.fragment.MarketFragment;
import com.example.zkt.ui.fragment.MineFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.leancloud.AVLogger;
import cn.leancloud.AVOSCloud;
import cn.leancloud.AVObject;
import cn.leancloud.core.AVOSService;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager;
    private RelativeLayout rl_content;
    //当前Fragment位置
    private  int position;

    //fragment集合
    private ArrayList<Fragment> fragmentlist;
    //当前Fragment
    private Fragment current_frag;

    @BindView(R.id.bottomNavigationView)
    BottomNavigationView bottomNavigationView;
    Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        rl_content = findViewById(R.id.rl_content);
        unbinder = ButterKnife.bind(this);

        AVObject.registerSubclass(Category.class);
        AVObject.registerSubclass(Goods.class);

        initialize();
    }


    private void initialize() {
        bottomNavigationView.setOnNavigationItemSelectedListener(this);//设置 NavigationItemSelected 事件监听
        //  BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);//改变 BottomNavigationView 默认的效果
        fragmentlist= new ArrayList<Fragment>();
        fragmentlist.add(new HomeFragment());
        fragmentlist.add(new LandFragment());
        fragmentlist.add(new MarketFragment());
        fragmentlist.add(new MineFragment());
        //选中第一个item,对应第一个fragment
        bottomNavigationView.setSelectedItemId(R.id.item_1);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    //NavigationItemSelected 事件监听
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        changePageFragment(item.getItemId());
        return true;
    }

    private void changePageFragment(int itemId) {

        switch (itemId) {
            case R.id.item_1: {
                position = 0;
                break;
            }
            case R.id.item_2: {
                position = 1;
                break;
            }
            case R.id.item_3: {
                position = 2;
                break;
            }
            case R.id.item_4: {
                position = 3;
                break;
            }
            default:
                position = 0;
                break;

        }
        Fragment next_frag = getFragment();
        switchFragment(current_frag,next_frag);
    }

    //获取当前Fragment
    private Fragment getFragment() {
        Fragment fragment = fragmentlist.get(position);
        return  fragment;
    }

    /**
     *切换Fragemnt
     * 用于保证不多生成Fragemnt
     */

    private Fragment switchFragment(Fragment fromFragment,Fragment toFragment){
        if(fromFragment != toFragment){
            //将当前Fragment设置为toFragment
            current_frag = toFragment;
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            //切换
            // 判断toFragment有没有被添加
            if(!toFragment.isAdded()){
                if(fromFragment != null){
                    transaction.hide(fromFragment);
                }
                if(toFragment !=null){
                    transaction.add(R.id.rl_content,toFragment).commit();
                }
            }else{
                if(fromFragment != null){
                    transaction.hide(fromFragment);
                }
                if(toFragment !=null){
                    transaction.show(toFragment).commit();
                }

            }
        }
        bottomNavigationView.getMenu().getItem(position).setChecked(true);
        return toFragment;
    }

    //用于Fragment中跳转用
    public void jumpPageFragment(int itemId){
        changePageFragment(itemId);
    }

}