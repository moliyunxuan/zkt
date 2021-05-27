package com.example.zkt.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.example.zkt.R;
import com.example.zkt.adapter.LandOrderAdapter;

import java.util.List;

import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import cn.leancloud.AVUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LandOrderActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<AVObject> datas;
    private View view;
    private Context mContext;
    private LandOrderAdapter mLandOrderAdapter;



    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1) {
                datas = (List<AVObject>) msg.obj;
            }
            init();

        }


    };

    private void init() {

        mContext =this;
        mLandOrderAdapter = new LandOrderAdapter(mContext,datas);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 1));
        mRecyclerView.setAdapter(mLandOrderAdapter);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_land_order);

        mRecyclerView = (RecyclerView) findViewById(R.id.rc_land_order);
        getLandOrder();
    }

    private void getLandOrder() {



        AVQuery query = new AVQuery("LandOrder");
        query.whereEqualTo("owner", AVUser.getCurrentUser().getMobilePhoneNumber());
        query.findInBackground().subscribe(new Observer<List<AVObject>>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<AVObject> landOrder) {

                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = landOrder;
                //4、发送消息
                handler.sendMessage(msg);

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });






    }
}