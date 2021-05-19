package com.example.zkt.mvp.model;

import android.util.Log;

import com.example.zkt.mvp.IModel;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

public class ModelTest {

    private static final String TAG = ModelTest.class.getSimpleName();

    private String AppKey = "95d8dfdc81c6443a";
    private String AppSecret = "84c847602d9a493b8f476ea4f4c0d43c";

    private static ModelTest mInstance;

    public static ModelTest getInstance() {
        mInstance = new ModelTest();
        return mInstance;
    }

    //统一下单
    public void createOrder(String name, String price, final IModel.AsyncCallback callback) {
        String URL = "https://admin.zhanzhangfu.com/order/createOrder";
        OkGo.<String>post(URL).cacheMode(com.lzy.okgo.cache.CacheMode.FIRST_CACHE_THEN_REQUEST)
                .headers("Payment-Key", AppKey)
                .headers("Payment-Secret", AppSecret)
                .params("price", price)
                .params("name", name)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        Log.d("createOrder", response.body());
                        if (response.code() == 200) {
                            callback.onSuccess(response.body());
                        } else {
                            callback.onError(response.code());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<String> response) {
                        super.onError(response);
                        callback.onError(response.code());
                    }
                });
    }

    //订单支付状态查询
    public void getOrderState(String orderId, final IModel.AsyncCallback callback) {
        String URL = "https://admin.zhanzhangfu.com/order/onlinePayFindResult";
        OkGo.<String>get(URL).cacheMode(com.lzy.okgo.cache.CacheMode.FIRST_CACHE_THEN_REQUEST)
                .headers("Payment-Key", AppKey)
                .headers("Payment-Secret", AppSecret)
                .params("orderId", orderId)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        Log.d("getOrderState", response.body());
                        if (response.code() == 200) {
                            callback.onSuccess(response.body());
                        } else {
                            callback.onError(response.code());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<String> response) {
                        super.onError(response);
                        callback.onError(response.code());
                    }
                });
    }

    /**
     * 轮询收款码接口  此接口用于随机调取一个收款码、及相关信息
     * { "msg": "查询成功", "code": "10001", "onecode": "http://zyphoto.itluntan.cn/20210308205144", "codetype": "wxcode", "qrcode": "alipayqr://platformapi/startapp?saId=10000007&qrcode=https%3A%2F%2Fqr.alipay.com%2Ffkx152655c8lyjz0qqxag7a", "listcode": "[{\"codetype\":2,\"codeurl\":\"http://zyphoto.itluntan.cn/20210309003034\",\"datetime\":\"2021-03-09 00:30:37\",\"id\":12,\"qrcode\":\"alipayqr://platformapi/startapp?saId=10000007&qrcode=https%3A%2F%2Fqr.alipay.com%2Ffkx152655c8lyjz0qqxag7a\",\"remarks\":\"\",\"sid\":\"20210309-5405\",\"state\":0,\"uid\":\"cd373bac-f5f0-402c-bf39-0fba5571aa50\"},{\"codetype\":1,\"codeurl\":\"http://zyphoto.itluntan.cn/20210309003047\",\"datetime\":\"2021-03-09 00:30:49\",\"id\":13,\"qrcode\":\"\",\"remarks\":\"\",\"sid\":\"20210309-1115\",\"state\":0,\"uid\":\"cd373bac-f5f0-402c-bf39-0fba5571aa50\"}]", }
     */
    public void randomCode(final IModel.AsyncCallback callback) {
        String URL = "https://admin.zhanzhangfu.com/randomCode";
        OkGo.<String>get(URL).cacheMode(com.lzy.okgo.cache.CacheMode.FIRST_CACHE_THEN_REQUEST)
                .params("key", AppKey)
                //1.代表微信   2.代表支付宝
                .params("type", "2")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        Log.d("randomCode", response.body());
                        if (response.code() == 200) {
                            callback.onSuccess(response.body());
                        } else {
                            callback.onError(response.code());
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<String> response) {
                        super.onError(response);
                        callback.onError(response.code());
                    }
                });
    }

}
