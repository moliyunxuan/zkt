package com.example.zkt.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.example.zkt.R;
import com.example.zkt.adapter.ServiceAdapter;
import com.example.zkt.bean.ServiceBean;
import com.example.zkt.ui.fragment.ServiceFragment;
import com.example.zkt.ui.widget.HorizontalListView;

import java.security.Provider;
import java.util.ArrayList;
import java.util.List;

public class IntroduceActivity extends AppCompatActivity {


    private HorizontalListView hlv;
    private ServiceAdapter myAdapter;
    private List<ServiceBean> serviceBeans;
    private OnChangeListener onchangedListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);

        hlv = (HorizontalListView) findViewById(R.id.hlv);
        serviceBeans= new ArrayList<>();
        initData();
        myAdapter = new ServiceAdapter(serviceBeans,this);
        hlv.setAdapter(myAdapter);
       ServiceFragment fragment = new ServiceFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.service_detail_container ,fragment);
        transaction.commit();
        hlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("msg", "position:" + position);
                myAdapter.setSelectedPosition(position);
                myAdapter.notifyDataSetInvalidated();

                if (onchangedListener != null)
                {
                    onchangedListener.changeService(serviceBeans.get(position));
                }
            }
        });

    }

    private void initData() {

        ServiceBean service1 = new ServiceBean();
        service1.setName("全托管");
        service1.setPrice("￥68.0/天");
        service1.setContent("包种植包采摘包配送");
        service1.setService("http://img.moliyunxuan.com/quan1.png");
        serviceBeans.add(service1);
        ServiceBean service2 = new ServiceBean();
        service2.setName("半托管");
        service2.setPrice("￥38.0/天");
        service2.setContent("仅包种植");
        service2.setService("http://img.moliyunxuan.com/ban1.png");
        serviceBeans.add(service2);
        ServiceBean service3 = new ServiceBean();
        service3.setName("无托管");
        service3.setPrice("￥0.0/天");
        service3.setContent("仅出租土地，需自行种植");
        service3.setService("http://img.moliyunxuan.com/wu1.png");
        serviceBeans.add(service3);
    }




    public void setOnChangeListener(OnChangeListener onChangeListener)
    {
        this.onchangedListener = onChangeListener;
    }

    public interface OnChangeListener
    {
        void changeService(ServiceBean serviceBean);
    }

}