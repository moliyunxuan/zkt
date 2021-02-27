package com.example.zkt.data.source;

import android.accounts.Account;

import com.example.zkt.bean.Goods;
import com.example.zkt.bean.User;
import com.example.zkt.data.Error;
import com.example.zkt.data.i.Callback;

import java.util.List;

import cn.leancloud.AVException;
import cn.leancloud.AVQuery;
import cn.leancloud.callback.FindCallback;
import cn.leancloud.callback.SaveCallback;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class GoodsRepository implements GoodsDataSource {


    @Override
    public void queryAllGoods(final Callback callback) {

        AVQuery<Goods> query = new AVQuery<>("Goods");
        query.findInBackground().subscribe(new Observer<List<Goods>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Goods> list) {


            }

            @Override
            public void onError(Throwable e) {


            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void queryOneGood(long gid) {


    }

    @Override
    public void queryTypeGoods(int type) {
        AVQuery<Goods> startDateQuery = new AVQuery<>("Goods");
        startDateQuery.whereEqualTo(Goods.GID, type);
        startDateQuery.findInBackground().subscribe(new Observer<List<Goods>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Goods> list) {



            }

            @Override
            public void onError(Throwable e) {


            }

            @Override
            public void onComplete() {

            }
        });


    }

    @Override
    public void queryGoods(User user) {


    }
}
