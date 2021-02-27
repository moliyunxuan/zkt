package com.example.zkt.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.zkt.activity.LoginActivity;
import com.example.zkt.bean.User;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.leancloud.AVUser;

public abstract class BaseFragment extends Fragment {

    private View mView;
    protected Bundle savedInstanceState;
    public Context mContext;
    protected LayoutInflater mInflater;
    Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mInflater = inflater;
        this.savedInstanceState=savedInstanceState;
        mView=mInflater.inflate(getContentResourseId(), null);
        unbinder= ButterKnife.bind(this,mView);
        init();
        return mView;
    }

    protected abstract void init();

    protected abstract int getContentResourseId();

    public void startActivity(Intent intent, boolean isNeedLogin){

        if (isNeedLogin) {
            User user =null;
            AVUser avUser= AVUser.currentUser();
            user.userPhone=avUser.getMobilePhoneNumber();
            if (user.userPhone != null) {
                super.startActivity(intent);    //需要登录,且已经登录.直接跳到目标activity中
            } else {
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                super.startActivity(loginIntent);
            }
        } else {
            super.startActivity(intent);
        }
    }
}
