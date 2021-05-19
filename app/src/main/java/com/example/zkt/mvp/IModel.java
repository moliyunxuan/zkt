package com.example.zkt.mvp;


//支付接口
public interface IModel {

    interface AsyncCallback {
        void onSuccess(Object obj);
        void onError(Object obj);
    }
}
