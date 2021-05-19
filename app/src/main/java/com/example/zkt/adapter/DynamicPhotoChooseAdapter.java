package com.example.zkt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.zkt.R;
import com.example.zkt.bean.DynamicPhotoItem;
import com.example.zkt.util.GlideUtils;


import java.util.ArrayList;
import java.util.List;


public class DynamicPhotoChooseAdapter extends BaseAdapter {

    private List<DynamicPhotoItem> albumBeanList;
    private Context mContext;

    public DynamicPhotoChooseAdapter(Context mContext) {
        albumBeanList = new ArrayList<>();
        albumBeanList.add(new DynamicPhotoItem("", true));
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return albumBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return albumBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final DynamicPhotoItem albumBean = albumBeanList.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.dynamic_gridview_item, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_add_photo);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (albumBean.isPick()) {
            viewHolder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.add_photo));
        } else {



            Glide.with(mContext)
                    .load(albumBean.getFilePath())
                    .placeholder(R.drawable.xiaolian)
                    .error(R.drawable.xiaolian)
                    .into(viewHolder.imageView);

//            GlideUtils.load(mContext,albumBean.getFilePath(),viewHolder.imageView);


        }
        return convertView;
    }

    public void addData(List<DynamicPhotoItem> mAlbumBeanList) {
        albumBeanList.remove(albumBeanList.size() - 1);
        albumBeanList.addAll(mAlbumBeanList);
        albumBeanList.add(new DynamicPhotoItem("", true));
//        notifyDataSetChanged();
    }

    public void removeData(int position) {
        removeData(albumBeanList.get(position));
    }

    public void removeData(DynamicPhotoItem albumBean) {
        if (albumBeanList != null && albumBeanList.contains(albumBean)) {
            //判断当前的数量
            switch (albumBeanList.size()) {
                case 1:
                case 2:
                case 3:
                    albumBeanList.remove(albumBean);
                    break;
                case 4:
                    albumBeanList.remove(albumBean);
                    if (!albumBeanList.get(albumBeanList.size() - 1).isPick()) {
                        albumBeanList.add(new DynamicPhotoItem("", true));
                    }
                    break;
            }
            this.notifyDataSetInvalidated();
        }
    }

    public List<DynamicPhotoItem> getData() {
        return albumBeanList;
    }

    private static class ViewHolder {
        public ImageView imageView;
    }



}