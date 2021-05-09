package com.example.zkt.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.zkt.MainActivity;
import com.example.zkt.R;
import com.example.zkt.adapter.MyAdapter;
import com.example.zkt.adapter.SwipeCardLayoutManager;
import com.example.zkt.spannable.ItemRemovedListener;
import com.example.zkt.ui.widget.SwipeCardRecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class AnimalActivity extends AppCompatActivity {


    private SwipeCardRecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal);
        mRecyclerView = (SwipeCardRecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new SwipeCardLayoutManager());
        AVQuery query = new AVQuery("Animal");
        query.findInBackground().subscribe(new Observer<List<AVObject>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<AVObject> animals) {

                mAdapter = new MyAdapter(getApplicationContext(), animals);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setRemovedListener(new ItemRemovedListener() {
                    @Override
                    public void onRightRemoved() {
//                        Toast.makeText(AnimalActivity.this, animals.get(animals.size() - 1) + " was right removed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLeftRemoved() {
//                        Toast.makeText(AnimalActivity.this, animals.get(animals.size() - 1) + " was left removed", Toast.LENGTH_SHORT).show();
                    }
                });
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