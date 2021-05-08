package com.example.zkt.mvp.biz;

public interface OnShoppingCartChangeListener {
    void onDataChange(String selectCount, String selectMoney);
    void onSelectItem(boolean isSelectedAll);
}
