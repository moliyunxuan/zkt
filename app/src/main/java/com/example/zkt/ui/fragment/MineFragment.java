package com.example.zkt.ui.fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zkt.BuildConfig;
import com.example.zkt.MainActivity;
import com.example.zkt.R;
import com.example.zkt.activity.ClipImageActivity;
import com.example.zkt.bean.User;
import com.example.zkt.ui.widget.CircleImageView;
import com.example.zkt.util.BitMapUtil;
import com.example.zkt.util.FileUtil;
import com.example.zkt.util.GlideUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.leancloud.AVFile;
import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import cn.leancloud.AVUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import lombok.SneakyThrows;
import retrofit2.http.Url;

import static android.app.Activity.RESULT_OK;

import static com.example.zkt.util.FileUtil.getRealFilePathFromUri;




public class MineFragment extends Fragment implements View.OnClickListener {

    //请求相机
    private static final int REQUEST_CAPTURE = 100;
    //请求相册
    private static final int REQUEST_PICK = 101;
    //请求截图
    private static final int REQUEST_CROP_PHOTO = 102;
    //请求访问外部存储
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 103;
    //请求写入外部存储
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 104;
    //头像
    private CircleImageView headImage;
    //用户名
    private TextView nickName;
    //调用照相机返回图片文件
    private File tempFile;
    //当前登录用户
    private AVUser avUser;
    //图片网址
    private  String avatar;


    private static final String IMAGE_FILE_CROP_NAME = "temp_crop_head_image.jpg";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        headImage = view.findViewById(R.id.iv_userinfo_pic);
        nickName=view.findViewById(R.id.tv_userinfo_name);

        avUser = AVUser.getCurrentUser();

        if(avUser!=null) {
            nickName.setText(avUser.getString("nickName"));
        }

        String mobilePhoneNumber = avUser.getMobilePhoneNumber();
        AVQuery<AVObject> query = new AVQuery<>("_User");
        query.whereEqualTo("mobilePhoneNumber",mobilePhoneNumber);
        query.getFirstInBackground().subscribe(new Observer<AVObject>() {
            public void onSubscribe(Disposable disposable) {}

            public void onNext(AVObject todo) {
                Toast.makeText(getActivity(),"成功找到用户"+mobilePhoneNumber,Toast.LENGTH_SHORT).show();
                Log.d("msg", "找到用户： "+mobilePhoneNumber);
                 avatar = todo.getAVFile("avatar").getUrl();
                Log.d("msg", "找到头像，网址为： "+avatar);

            }
            public void onError(Throwable throwable) {
                Toast.makeText(getActivity(),"無法找到用户"+mobilePhoneNumber,Toast.LENGTH_SHORT).show();
                Log.d("msg", "失敗原因： "+throwable);
            }
            public void onComplete() {}
        });
        //通过AvUser的手机号码查询对应的User
        //通过User查询用户头像的url
        //能够获取到用户头像的url，但不能正常显示
        //初步判断头像是用的是CircleImageView
        //建议使用setImageBitmap
       //headImage.setImageBitmap(returnBitMap(avatar));
        //headImage.setImageURI(Uri.parse(avatar));
        headImage.setImageBitmap(new BitMapUtil().returnBitMap(avatar));
        headImage.setOnClickListener(this);
        return view;
    }





    @Override
    public void onClick(View v) {
        uploadHeadImage();
    }

    /**
     * 上传头像
     */
    private void uploadHeadImage() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_popupwindow, null);
        TextView btnCarema = (TextView) view.findViewById(R.id.btn_camera);
        TextView btnPhoto = (TextView) view.findViewById(R.id.btn_photo);
        TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        View parent = LayoutInflater.from(getActivity()).inflate(R.layout.activity_main, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //popupWindow在弹窗的时候背景半透明

        final WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
        params.alpha = 0.5f;
        getActivity().getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                getActivity().getWindow().setAttributes(params);
            }
        });

        btnCarema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到调用系统相机
                    gotoCamera();
                }
                popupWindow.dismiss();
            }
        });
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请READ_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到相册
                    gotoPhoto();
                }
                popupWindow.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }


    /**
     * 外部存储权限申请返回
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoCamera();
            }
        } else if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoPhoto();
            }
        }
    }


    /**
     * 跳转到相册
     */
    private void gotoPhoto() {
        Log.d("evan", "*****************打开图库********************");
        //跳转到调用系统图库
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);
    }


    /**
     * 跳转到照相机
     */
    private void gotoCamera() {
        Log.d("evan", "*****************打开相机********************");
        //创建拍照存储的图片文件
        tempFile = new File(FileUtil.checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"), System.currentTimeMillis() + ".jpg");

        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //设置7.0中共享文件，分享路径定义在xml/file_paths.xml
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".fileProvider", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        }
        startActivityForResult(intent, REQUEST_CAPTURE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == RESULT_OK) {
                    gotoClipActivity(Uri.fromFile(tempFile));
                }
                break;
            case REQUEST_PICK:  //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    gotoClipActivity(uri);
                }
                break;
            case REQUEST_CROP_PHOTO:  //剪切图片返回
                if (resultCode == RESULT_OK) {
                    final Uri uri = intent.getData();
                    if (uri == null) {
                        return;
                    }
                    String cropImagePath = getRealFilePathFromUri(getContext(), uri);
                    Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);
                    headImage.setImageBitmap(bitMap);
                    //此处后面可以将bitMap转为二进制上传后台网络
                    //将bitmap转换成file
                    String userId = avUser.getObjectId();
                    AVFile file = new AVFile("avatar"+userId,saveBitmapFile(bitMap));

                    file.saveInBackground().subscribe(new Observer<AVFile>() {
                        public void onSubscribe(Disposable disposable) {}
                        public void onNext(AVFile avFile) {
                            System.out.println("文件保存完成。objectId：" + file.getObjectId());
//                            avUser.put("avatar",avFile);
//                            avUser.save();
                        }
                        public void onError(Throwable throwable) {
                            // 保存失败，可能是文件无法被读取，或者上传过程中出现问题

                        }
                        public void onComplete() {}
                    });
                }
                break;






        }
    }






    /**
     * 打开截图界面
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(getContext(), ClipImageActivity.class);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }

    //将bitmap转换成file
    public File saveBitmapFile(Bitmap bitmap) {
        File file = new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_CROP_NAME);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }








}