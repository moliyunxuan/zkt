package com.example.zkt.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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
import cn.leancloud.AVUser;
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
                        int i = animals.size()-1;
                        Toast.makeText(AnimalActivity.this, "认养成功", Toast.LENGTH_SHORT).show();
                        String owner= AVUser.getCurrentUser().getMobilePhoneNumber();
                        AVObject animalOrder = new AVObject("AnimalOrder");
                        animalOrder.put("name",animals.get(i).getString("name")+"认养 "+animals.get(i).getString("batch"));
                        animalOrder.put("growCycle",animals.get(i).getString("cycle"));
                        animalOrder.put("harvest",animals.get(i).getString("harvest"));
                        animalOrder.put("owner",owner);
                        animalOrder.put("shopName",animals.get(i).getString("kind"));
                        animalOrder.put("photo",animals.get(i).getString("image"));
                        animalOrder.put("price",animals.get(i).getInt("price"));
                        animalOrder.put("state",1);
                        animalOrder.saveInBackground().subscribe(new Observer<AVObject>() {


                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(AVObject avObject) {

                                Log.d("msg","认养成功");
                            }

                            @Override
                            public void onError(Throwable e) {

                                Log.d("msg","认养失败"+e);
                            }

                            @Override
                            public void onComplete() {

                            }
                        });

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