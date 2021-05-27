package com.example.zkt.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zkt.R;
import com.example.zkt.adapter.AnimalOrderAdapter;
import com.example.zkt.adapter.GoodsOrderAdapter;
import com.example.zkt.adapter.GoodsPayOrderAdapter;

import java.util.List;

import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import cn.leancloud.AVUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GoodsOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoodsOrderFragment extends Fragment {

    private RecyclerView mRecyclerView;

    private GoodsPayOrderAdapter mGoodsPayOrderAdapter;

    String title;

    private List<AVObject> datas;

    private View view;
    private Context mContext;
    private int state;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1) {
                datas = (List<AVObject>) msg.obj;
            }
            init(view);
        }
    };




    public GoodsOrderFragment() {
        // Required empty public constructor
    }


    public static GoodsOrderFragment newInstance(String title,Context context,int state) {
        GoodsOrderFragment fragment = new  GoodsOrderFragment();
        fragment.title = title;
        fragment.mContext = context;
        fragment.state =state;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_goods_order, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rc_goods_order);

        getGoodsOrder();

        return view;

    }

    private void getGoodsOrder() {


        AVQuery query = new AVQuery("GoodsOrder");
        query.whereEqualTo("owner", AVUser.getCurrentUser().getMobilePhoneNumber());
        query.whereEqualTo("state",state);
        query.findInBackground().subscribe(new Observer<List<AVObject>>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<AVObject> animalOrder) {

                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = animalOrder;
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


    public String getTitle() {
        return title;
    }


    private void init(View view) {
        mGoodsPayOrderAdapter = new GoodsPayOrderAdapter(mContext, datas,title);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.HORIZONTAL));
        mRecyclerView.setAdapter(mGoodsPayOrderAdapter);

    }




}