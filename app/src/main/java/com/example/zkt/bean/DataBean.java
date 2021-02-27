package com.example.zkt.bean;

import com.example.zkt.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 轮播图
 */
public class DataBean {
    public Integer imageRes;
    public int viewType;
    public String title;

    public DataBean(Integer imageRes, String title, int viewType) {
        this.imageRes = imageRes;
        this.title = title;
        this.viewType = viewType;
    }


    public static List<DataBean> getTestData() {
        List<DataBean> list = new ArrayList<>();
        list.add(new DataBean(R.mipmap.img1, "向往的生活",1));
        list.add(new DataBean(R.mipmap.img2, "实施乡村振兴战略",2));
        list.add(new DataBean(R.mipmap.img3, "生态农场·贴近自然·自然更健康",1));
        list.add(new DataBean(R.mipmap.img4, "乡村振兴志愿者招募",2));
        return list;
    }
}
