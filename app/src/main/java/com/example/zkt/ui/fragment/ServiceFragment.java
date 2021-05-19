package com.example.zkt.ui.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.zkt.R;
import com.example.zkt.activity.IntroduceActivity;
import com.example.zkt.bean.ServiceBean;
import com.example.zkt.util.GlideUtils;


public class ServiceFragment extends Fragment
{
    IntroduceActivity activity;
    private ImageView service;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_service_detail , null);
        service =  view.findViewById(R.id.service_image);

        Log.d("fragment" , "Created");
        activity = (IntroduceActivity) getActivity();
        activity.setOnChangeListener(new IntroduceActivity.OnChangeListener()
        {
            @Override
            public void changeService(ServiceBean serviceBean)
            {
                GlideUtils.load(getContext(),serviceBean.getService(),service);

            }
        });
        return view;
    }
}