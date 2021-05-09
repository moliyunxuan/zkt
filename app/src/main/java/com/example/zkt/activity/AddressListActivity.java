package com.example.zkt.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.zkt.R;
import com.example.zkt.adapter.AddressListAdapter;
import com.example.zkt.base.BaseApplication;
import com.example.zkt.data.DataManager;
import com.example.zkt.data.dao.Address;
import com.example.zkt.ui.widget.EnjoyshopToolBar;
import com.example.zkt.util.PreferencesUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.leancloud.AVUser;

public class AddressListActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    EnjoyshopToolBar mToolBar;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerview;

    private AddressListAdapter mAdapter;
    private List<Address> mAddressDataList;
    /**
     * 默认设置的地址
     */
    private int isDefaultPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        ButterKnife.bind(this);
        initToolbar();
        initView();
    }


    @Override
    protected void onResume() {
        super.onResume();
        initAddress();
        isDefaultPosition = PreferencesUtils.getInt(this, "isDefaultPosition", 0);
    }



    private void initView() {

        if (mAdapter == null) {
            mAdapter = new AddressListAdapter(mAddressDataList);
            mRecyclerview.setAdapter(mAdapter);
            mRecyclerview.setLayoutManager(new LinearLayoutManager(AddressListActivity.this));
            mRecyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration
                    .HORIZONTAL));

            mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    Address address = (Address)adapter.getData().get(position);
                    switch (view.getId()) {
                        case R.id.txt_edit:
                            updateAddress(address);
                            break;
                        case R.id.txt_del:
                            delAddress(address);
                            break;
                        case R.id.cb_is_defualt:
                            chooseDefult(mAddressDataList, position);
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

    /**
     * 初始化地址信息
     * 商业项目这里是请求接口
     */
    private void initAddress() {

        Long userId = Long.valueOf(AVUser.getCurrentUser().getMobilePhoneNumber());
        mAddressDataList = DataManager.queryAddress(userId);
        if (mAddressDataList != null && mAddressDataList.size() > 0) {
            for (int i = 0; i < mAddressDataList.size(); i++) {
                mAdapter.setNewData(mAddressDataList);
            }
        }else {
            mAddressDataList.clear();
            mAdapter.setNewData(mAddressDataList);
        }
    }


    private void updateAddress(Address address) {
        jumpAddressAdd(address);
    }

    private void delAddress(Address address) {
        Long addressId = address.getAddressId();
        DataManager.deleteAddress(addressId);
        initAddress();
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 需要改变2个对象的值.一个是 之前默认的.一个是当前设置默认的
     * @param mAddressDataList
     * @param position
     */
    private void chooseDefult(List<Address> mAddressDataList, int position) {

        Address preAddress = mAddressDataList.get(isDefaultPosition);
        Address nowAddress = mAddressDataList.get(position);

        isDefaultPosition = position;
        PreferencesUtils.putInt(this, "isDefaultPosition", isDefaultPosition);

        changeBean(preAddress);
        changeBean(nowAddress);

        initAddress();
        mAdapter.notifyDataSetChanged();

    }

    private void changeBean(Address address) {

        Long addressId = address.getAddressId();
        address.setAddressId(addressId);
        address.setIsDefaultAddress(!address.getIsDefaultAddress());
        DataManager.updateAddress(address);

    }

    private void initToolbar() {

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到添加地址界面
                jumpAddressAdd(null);
            }
        });
    }

    private void jumpAddressAdd(Address address) {
        Intent intent = new Intent(AddressListActivity.this, AddressAddActivity.class);
        //这里不能使用序列化
        if (address != null) {
            intent.putExtra("addressId", address.getAddressId());
            intent.putExtra("name", address.getName());
            intent.putExtra("phone", address.getPhone());
            intent.putExtra("BigAddress", address.getBigAddress());
            intent.putExtra("SmallAddress", address.getSmallAddress());
        }
        startActivity(intent);
    }
}