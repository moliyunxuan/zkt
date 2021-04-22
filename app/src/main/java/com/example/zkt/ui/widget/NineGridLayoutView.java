package com.example.zkt.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.example.zkt.util.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;


/**
 * author：moliyunxuan
 * date：created by 2021/4/21
 * description：社区九宫格
 */
public class NineGridLayoutView extends NineGridLayout {


    protected static final int MAX_W_H_RATIO = 3;

    public NineGridLayoutView(Context context) {
        super(context);
    }

    public NineGridLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean displayOneImage(RatioImageView imageView, String url, int parentWidth) {

        ImageLoaderUtil.displayImage(mContext, imageView, url, ImageLoaderUtil.getPhotoImageOption(), new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();

                int newW;
                int newH;
                if (h > w * MAX_W_H_RATIO) {//h:w = 5:3
                    newW = parentWidth / 2;
                    newH = newW * 5 / 3;
                } else if (h < w) {//h:w = 2:3
                    newW = parentWidth * 2 / 3;
                    newH = newW * 2 / 3;
                } else {//newH:h = newW :w
                    newW = parentWidth / 2;
                    newH = h * newW / w;
                }
                setOneImageLayoutParams(imageView, newW, newH);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        return false;
    }

    @Override
    protected void displayImage(RatioImageView imageView, String url) {
        ImageLoaderUtil.getImageLoader(mContext).displayImage(url, imageView, ImageLoaderUtil.getPhotoImageOption());
    }

    @Override
    protected void onClickImage(int position, String url, List<String> urlList) {

        String urlStr[] = new String[urlList.size()];
        imageBrowser(position, urlList.toArray(urlStr));
    }
    /**
     * 跳转相册
     *
     * @param position
     * @param urls
     */
    private void imageBrowser(int position, String[] urls) {
//        Intent intent = new Intent();
//        // 此处可跳转到大图页面
//        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
//        intent.putExtra("image_urls", urls);
//        intent.putExtra("image_index", position);
//        mContext.startActivity(intent);
    }
}