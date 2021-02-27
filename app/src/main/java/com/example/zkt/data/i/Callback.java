package com.example.zkt.data.i;
/**
 * @date 2021/28
 * @desc 网络请求公共回调接口
 */
public interface Callback {
    /** 请求成功 */
    void requestSuccess();
    /** 请求失败 */
    void requestFail(Error e);
}
