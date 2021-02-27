package com.example.zkt.base;


public interface IBaseView {
    /**
     * 显示加载
     */
    void showLoading() ;

    /**
     * 隐藏加载
     */
    void loadSuccess() ;

    /**
     * 加载失败
     */
    void showError();

    /**
     * 显示失败信息
     */
    void showErrorMsg(String msg);
}
