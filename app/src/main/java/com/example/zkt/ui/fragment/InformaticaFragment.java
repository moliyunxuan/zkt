package com.example.zkt.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zkt.R;
import com.example.zkt.adapter.MessageAdapter;

import java.util.ArrayList;

/**
 * 土地详情fragment中的图文
 * on 2020/3/11.
 */

public class InformaticaFragment extends Fragment {


    private MessageAdapter adapter;
    private ArrayList<Integer> images = new ArrayList<>();
    RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_informatica, container, false);

        images.add(R.mipmap.pic_1);
        images.add(R.mipmap.pic_2);
        images.add(R.mipmap.pic_3);
        images.add(R.mipmap.pic_4);
        images.add(R.mipmap.pic_5);
        images.add(R.mipmap.pic_6);
        images.add(R.mipmap.pic_7);
        images.add(R.mipmap.pic_8);
        images.add(R.mipmap.pic_9);
        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new MessageAdapter();
        adapter.setDataList(images);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
        return view;
    }
}