package com.example.zkt.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.example.zkt.R;
import com.example.zkt.adapter.DynamicPhotoChooseAdapter;
import com.example.zkt.bean.DynamicBean;
import com.example.zkt.bean.DynamicPhotoItem;
import com.example.zkt.bean.User;
import com.example.zkt.util.DialogBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.leancloud.AVFile;
import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import cn.leancloud.AVUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.iwf.photopicker.PhotoPicker;

public class CreateDynamicActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 0x01;

    private TextView cancel;
    private TextView send;
    private TextView contentCount;
    private EditText editContent;
    private GridView gridView;
    private DynamicPhotoChooseAdapter mDynamicPhotoChooseAdapter;
    private Dialog mLoadingDialog;
    private Dialog mLoadingFinishDialog;
    private String userName;
    private String avatar;


    private  ArrayList<String> images;
    private  ArrayList<DynamicPhotoItem> photoItems;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_create_dynamic);
        initview();
        init();

    }


    private void initview() {
        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        contentCount = findViewById(R.id.tv_content_count);
        send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });
        editContent = findViewById(R.id.edit_content);
        gridView = findViewById(R.id.gridView);
    }



    public void init() {
        String mobilePhoneNumber = AVUser.getCurrentUser().getMobilePhoneNumber();
        AVQuery<AVObject> query = new AVQuery<>("_User");
        query.whereEqualTo("mobilePhoneNumber",mobilePhoneNumber);
        query.getFirstInBackground().subscribe(new Observer<AVObject>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(AVObject avObject) {
                userName = avObject.getString("nickName");
                Log.d("msg","动态发送者 "+userName);
                avatar = avObject.getAVFile("avatar").getUrl();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        editContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contentCount.setText(s.length() + "/1000");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDynamicPhotoChooseAdapter = new DynamicPhotoChooseAdapter(CreateDynamicActivity.this);
        gridView.setAdapter(mDynamicPhotoChooseAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mDynamicPhotoChooseAdapter.getCount() - 1) {
                    PhotoPicker.builder()
                            .setPhotoCount(9)
                            .setShowCamera(true)
                            .setShowGif(true)
                            .setPreviewEnabled(false)
                            .start(CreateDynamicActivity.this, PhotoPicker.REQUEST_CODE);
                }
            }
        });

        mLoadingDialog = DialogBuilder.createLoadingDialog(CreateDynamicActivity.this, "正在上传");
        mLoadingFinishDialog = DialogBuilder.createLoadingfinishDialog(CreateDynamicActivity.this, "上传完成");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 选择结果回调
        if (requestCode == PhotoPicker.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            List<DynamicPhotoItem> list = new ArrayList<>();
            if (pathList.size() != 0) {
                for (String path : pathList) {
                    list.add(new DynamicPhotoItem(path, false));
                }
            }
            mDynamicPhotoChooseAdapter.addData(list);
            gridView.setAdapter(mDynamicPhotoChooseAdapter);
        }
    }

    private void send() {
        mLoadingDialog.show();

        List<AVFile> fileList = new ArrayList<>();

        String content = editContent.getText().toString();//动态内容
        images = new ArrayList<>();//动态集合
        photoItems = (ArrayList<DynamicPhotoItem>) mDynamicPhotoChooseAdapter.getData();
        for (int i = 0; i < photoItems.size() - 1; i++) {

            AVFile file = new AVFile("dynamicPhoto",new File(photoItems.get(i).getFilePath()));
            file.saveInBackground().subscribe(new Observer<AVFile>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(AVFile avFile) {
                    System.out.println("文件保存完成。objectId：" + file.getObjectId());
                    fileList.add(avFile);
                    images.add(avFile.getUrl());
                    Log.d("msg","图片url "+avFile.getUrl());

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                    //此处有问题，就是保存一张图片，运行一次保存动态操作
                    //不放在onComplete里面会有数据延迟

                    HashMap<Object, Object> sender = new HashMap<>();//发送者信息
                    sender.put("nickname", userName);
                    Log.d("msg222","发动态的人是 "+userName);
                    sender.put("avatar", avatar);

                    AVObject dynamicBean = new AVObject("DynamicBean");
                    dynamicBean.put("content", content);
                    dynamicBean.put("images", images);
                    dynamicBean.put("sender", sender);

                    dynamicBean.saveInBackground().subscribe(new Observer<AVObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(AVObject avObject) {

                            mLoadingDialog.dismiss();
                            mLoadingFinishDialog.show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mLoadingFinishDialog.dismiss();
                                    finish();
                                }
                            }, 500);


                        }

                        @Override
                        public void onError(Throwable e) {

                            Log.d("msg", "动态保存失败" + e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });



                }
            });





        }


    }






}