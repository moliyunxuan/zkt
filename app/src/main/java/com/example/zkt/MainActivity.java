package com.example.zkt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
                FragmentTransaction transaction = fragmentManager.beginTransaction();//创建一个事务
                transaction.replace(R.id.rl_content, new HomeFragment());
                transaction.commit();//事务一定要提交，replace才会有效
                break;
            }
            case R.id.item_2: {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.rl_content, new LandFragment());
                transaction.commit();
                break;
            }
            case R.id.item_3: {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.rl_content, new MarketFragment());
                transaction.commit();
                break;
            }
            case R.id.item_4: {

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.rl_content, new MineFragment());
                transaction.commit();
                break;
            }
            default:
                break;

        }
    }
}