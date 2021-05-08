package com.example.zkt.ui.fragment;

import android.content.Intent;
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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.zkt.R;
import com.example.zkt.activity.GoodsDetailsActivity;
import com.example.zkt.activity.LandDetailActivity;
import com.example.zkt.adapter.LandAdapter;
import com.example.zkt.bean.Goods;
import com.example.zkt.bean.Land;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class PageFragment extends Fragment {


    private RecyclerView recyclerView;

    private List<AVObject> datas;
    private LandAdapter mLandAdapter;

    String title;
    private View view;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1) {
                datas = (List<AVObject>) msg.obj;
            }
            init(view);
            Log.e("Land","查询土地种类为 "+datas.get(0).toJSONString()+" 的土地成功");
        }
    };





    public PageFragment() {

    }


    public static PageFragment newInstance(String title) {
        PageFragment fragment = new PageFragment();
        fragment.title = title;
        return fragment;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        if(mView != null){
//            return mView;
//        }
        view = inflater.inflate(R.layout.fragment_page, container, false);
        recyclerView = view.findViewById(R.id.page_rv);
        getLand();
//        init(view);

        return view;
    }

    private void init(View view) {
        Log.e("Land","查询土地种类为 "+datas.get(0).toJSONString()+" 的土地成功");
        mLandAdapter = new LandAdapter(datas);
        mLandAdapter.setOnItemClickListener(new BaseQuickAdapter
                .OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AVObject land = (AVObject) adapter.getData().get(position);
                Intent intent = new Intent();
                intent.putExtra("ObjectId",land.getObjectId());
                intent.setClass(getContext(), LandDetailActivity.class);
                startActivity(intent);
            }
        });


        recyclerView.setAdapter(mLandAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.HORIZONTAL));


    }

    private void getLand() {
        AVQuery<AVObject> query = new AVQuery<>("Land");
        query.whereEqualTo("lkind",title);
        query.findInBackground().subscribe(new Observer<List<AVObject>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<AVObject> lands) {
                Log.d("hint","查询土地种类为 "+title+" 的土地成功");
//                datas=lands;
                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = lands;
                //4、发送消息
                handler.sendMessage(msg);

//                Log.e("Land","查询土地种类为 "+datas.get(0).toJSONString()+" 的土地成功");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("hint","查询土地种类为 "+title+" 的土地失败 "+"失败原因 "+e.getMessage());

            }

            @Override
            public void onComplete() {

            }
        });


    }

    public String getTitle() {
        return title;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


}