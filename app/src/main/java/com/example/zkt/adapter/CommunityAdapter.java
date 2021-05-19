package com.example.zkt.adapter;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.SpannableString;
import android.text.TextUtils;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.zkt.R;
import com.example.zkt.bean.CommentConfig;
import com.example.zkt.bean.CommentsBean;
import com.example.zkt.bean.DynamicBean;
import com.example.zkt.bean.ImageBean;
import com.example.zkt.bean.PraiseBean;
import com.example.zkt.ui.widget.CommentListView;
import com.example.zkt.ui.widget.NineGridLayoutView;
import com.example.zkt.ui.widget.PraiseListView;
import com.example.zkt.util.DataTest;
import com.example.zkt.util.DateUtils;
import com.example.zkt.util.Emoji;
import com.example.zkt.util.EmojiUtils;
import com.example.zkt.util.StringUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import cn.leancloud.AVUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.example.zkt.R.id.commentList;
/**
 * author：moliyunxuan
 * date：created by 2021/4/20
 * description：社区适配器
 */

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder> {

    public final static int TYPE_HEAD = 0;
    public final static int TYPE_CONTENT = 1;
    public static final int HEADVIEW_SIZE = 1;
    private Context mContext;
    private OnItemButtonClickListener mListener = null;//单击事件监听器
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private List<DynamicBean> datas;
    private int commonPosition;

    private String deleteItems[] = {"删除评论"};
    private long mLastTime = 0;

    public CommunityAdapter(Context context, OnItemButtonClickListener listener) {
        this.mContext = context;
        this.options = new DisplayImageOptions.Builder().cacheInMemory()
                .cacheOnDisc().showImageOnFail(R.drawable.default_img)
                .showStubImage(R.drawable.default_img)
                .showImageForEmptyUri(R.drawable.default_img).build();

        this.mListener = listener;
    }

    @NonNull
    @Override
    public CommunityAdapter.CommunityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CommunityAdapter.CommunityViewHolder viewHolder = null;
        if (viewType == TYPE_HEAD) {
            View headView = LayoutInflater.from(mContext).inflate(R.layout.head_community_layout, parent, false);
            viewHolder = new HeaderViewHolder(headView);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.community_item_layout, parent, false);
            viewHolder = new CommunityViewHolder(view);
        }
        return viewHolder;

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEAD;
        } else {
            return TYPE_CONTENT;
        }
    }

    public List<DynamicBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DynamicBean> datas) {
        this.datas = datas;
    }


    @Override
    public void onBindViewHolder(@NonNull CommunityAdapter.CommunityViewHolder holder, int position) {

        if (getItemViewType(position) == TYPE_HEAD) {
            //  此处可处理头部数据
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
//           imageLoader.displayImage(TextUtils.isEmpty(DataTest.curUser.getAvatar()) ? "" : DataTest.curUser.getAvatar(), headerViewHolder.imageViewHead, options);
//            headerViewHolder.tvNickName.setText(DataTest.curUser.getNick());


            String mobilePhoneNumber = AVUser.getCurrentUser().getMobilePhoneNumber();
            AVQuery<AVObject> query = new AVQuery<>("_User");
            query.whereEqualTo("mobilePhoneNumber",mobilePhoneNumber);
            query.getFirstInBackground().subscribe(new Observer<AVObject>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(AVObject avObject) {

                    imageLoader.displayImage(TextUtils.isEmpty(avObject.getAVFile("avatar").getUrl()) ? "" : avObject.getAVFile("avatar").getUrl(), headerViewHolder.imageViewHead, options);
                    headerViewHolder.tvNickName.setText( avObject.getString("nickName"));
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });


        } else {
            final int circlePosition = position - HEADVIEW_SIZE;
            commonPosition = circlePosition;
            final DynamicBean campusNes = datas.get(circlePosition);
            holder.setData(circlePosition, campusNes);
        }
    }

    @Override
    public int getItemCount() {
        // 有head需要加1
        if (null != datas && datas.size() > 0) {
            Log.d("msg","datas不为空");
            return datas.size() + 1;

        }
        Log.d("msg","datas为空");
        return 0;
    }

    public class HeaderViewHolder extends CommunityAdapter.CommunityViewHolder {
        ImageView imageViewHead;
        ImageView imageViewBg;
        TextView tvNickName;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            this.imageViewHead = (ImageView) itemView.findViewById(R.id.imageViewHead);
            this.imageViewBg = (ImageView) itemView.findViewById(R.id.imageViewBg);
            this.tvNickName = (TextView) itemView.findViewById(R.id.tvNickName);
        }
    }

    class CommunityViewHolder extends RecyclerView.ViewHolder {

        ImageView userPicture;    // 发送者头像小图地址
        TextView usernameTxt;     // 用户名称
        TextView content;         // 内容
        TextView time;            // 时间
        LinearLayout likesLayout;
        TextView likes;           // 点赞
        ImageView likesPicture;   // 点赞图标
        ImageView albumComment;   // 评论图标
        ImageView parise_icon;     // 赞图标
        TextView comments;        // 评论总数
        TextView deleteTv;
        NineGridLayoutView nineGridLayoutView;
        LinearLayout praiseListLayout; //点赞名单layout
        LinearLayout linearlayoutComment; //评论layout
        LinearLayout linearlayoutAll; //评论和赞总layout
        CommentListView commentListView; //评论列表
        PraiseListView praiseListView;//点赞列表
        View line;
        public CommunityViewHolder(@NonNull View convertView) {
            super(convertView);
            this.userPicture = (ImageView) convertView.findViewById(R.id.album_picture);
            this.usernameTxt = (TextView) convertView.findViewById(R.id.album_user_name_txt);
            this.content = (TextView) convertView.findViewById(R.id.album_content_txt);
            this.time = (TextView) convertView.findViewById(R.id.album_time);
            this.likesLayout = (LinearLayout) convertView.findViewById(R.id.album_praise_layout);
            this.likes = (TextView) convertView.findViewById(R.id.album_praise_number);
            this.likesPicture = (ImageView) convertView.findViewById(R.id.album_praise);
            this.albumComment = (ImageView) convertView.findViewById(R.id.album_comment);
            this.parise_icon = (ImageView) convertView.findViewById(R.id.parise_icon);
            this.comments = (TextView) convertView.findViewById(R.id.album_comment_number);
            this.deleteTv = (TextView) convertView.findViewById(R.id.tv_delete);
            this.nineGridLayoutView = (NineGridLayoutView) convertView.findViewById(R.id.layout_nine_grid);
            this.praiseListLayout = (LinearLayout) convertView.findViewById(R.id.album_praise_list_layout);
            this.linearlayoutComment = (LinearLayout) convertView.findViewById(R.id.linearlayout_comment);
            this.linearlayoutAll = (LinearLayout) convertView.findViewById(R.id.linearlayoutAll);
            this.commentListView = (CommentListView) convertView.findViewById(commentList);
            this.praiseListView = (PraiseListView) convertView.findViewById(R.id.praiseListView);
            this.line = convertView.findViewById(R.id.line);
        }

        /**
         * 设置数据
         *
         * @param circlePosition
         * @param DynamicBean
         */
        public void setData(final int circlePosition, final DynamicBean DynamicBean) {
            Log.i("[app]", "DynamicBean=" + DynamicBean);
            this.userPicture.setTag(circlePosition + "");
            if (null != DynamicBean.getSender()) {
                imageLoader.displayImage(TextUtils.isEmpty(DynamicBean.getSender().getAvatar()) ? "" : DynamicBean.getSender().getAvatar(), this.userPicture, options);
                this.usernameTxt.setText(DynamicBean.getSender().getNick());

            }
            String dateSource = DynamicBean.getDt();
            this.time.setText(DateUtils.getModularizationDate(Long.parseLong(dateSource)));
            // 判断接受到的是否有表情图片，有则替换
            if (!StringUtils.isEmpty(DynamicBean.getContent())) {
                for (int i = 0; i < EmojiUtils.picStr.length; i++) {
                    if (DynamicBean.getContent().contains("[" + EmojiUtils.picStr[i] + "]")) {
                        String s1 = DynamicBean.getContent();
                        String s = s1.replaceAll("\\[" + EmojiUtils.picStr[i] + "\\]", "<f" + EmojiUtils.picStr1[i] + ">");
                        DynamicBean.setContent(s);
                    }
                }
            }
            if (DynamicBean.getContent() != null && DynamicBean.getContent().contains("<f") && DynamicBean.getContent().contains(">")) {
                this.content.setText("");
                String message = DynamicBean.getContent();
                List<Object> results = new ArrayList<Object>();
                List<String> ems = new ArrayList<String>();
                Pattern patter = Pattern.compile("<f[\\w]*>");
                Matcher matcher = patter.matcher(DynamicBean.getContent());
                while (matcher.find()) {
                    ems.add(matcher.group());
                }
                for (int i = 0; i < ems.size(); i++) {
                    if (message.startsWith("<f")) {
                        results.add(message.substring(0, 6));
                        message = message.substring(6, message.length());
                        if (message.length() > 0 && !message.startsWith("<f")) {
                            if (message.contains("<f") && message.contains(">")) {
                                int emsIndex = message.indexOf("<");
                                String itemMes = message.substring(0, emsIndex);
                                results.add(itemMes);
                                message = message.substring(emsIndex, message.length());
                            } else {
                                results.add(message);
                            }
                        }
                    } else {
                        int emsIndex = message.indexOf("<");
                        String itemMes = message.substring(0, emsIndex);
                        results.add(itemMes);
                        message = message.substring(emsIndex, message.length());
                        results.add(message.substring(0, 6));
                        message = message.substring(6, message.length());
                    }
                }
                ArrayList<SpannableString> list = new ArrayList<SpannableString>();
                for (int i = 0; i < results.size(); i++) {
                    list.add(null);
                }
                for (int i = 0; i < results.size(); i++) {
                    if (results.get(i).toString().startsWith("<f")) {
                        String emPath = results.get(i).toString().replace("<", "");
                        emPath = emPath.replace(">", "");
                        emPath = emPath.substring(1, 4);
                        list.set(i, Emoji.getImg(mContext, emPath));
                    }
                }
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i) != null) {
                        results.set(i, list.get(i));
                    }
                }
                for (int i = 0; i < results.size(); i++) {
                    this.content.append((CharSequence) results.get(i));
                }
            } else if (!TextUtils.isEmpty(DynamicBean.getContent())) {
                this.content.setText(DynamicBean.getContent());
                this.content.setVisibility(View.VISIBLE);
            } else {
                this.content.setVisibility(View.GONE);
            }
            // 处理图片
            Collection<ImageBean> imageList = DynamicBean.getImages();
            if (imageList != null && imageList.size() > 0) { //图片展示
                Iterator iterator = imageList.iterator();
                int index = 0;
                int length;

                length = imageList.size();
                final String urls[] = new String[length];
                while (iterator.hasNext()) {
                    ImageBean image = (ImageBean) iterator.next();
                    urls[index] = image.getUrl();
                    index++;
                }
                List<String> urlLists = new ArrayList<String>();
                Collections.addAll(urlLists, urls);
                this.nineGridLayoutView.setVisibility(View.VISIBLE);
                this.nineGridLayoutView.setUrlList(urlLists);
            } else {
                this.nineGridLayoutView.setVisibility(View.GONE);
            }
            // 处理点赞列表
            final List<PraiseBean> praiseBeanList = DynamicBean.getPraiseList();
            // 处理评论列表
            final List<CommentsBean> commentsBeanList = DynamicBean.getComments();
            final boolean hasPraise = DynamicBean.hasPraise();
            final boolean hasComment = DynamicBean.hasComment();
            if (hasPraise || hasComment) {
                this.linearlayoutAll.setVisibility(View.VISIBLE);
                //处理点赞列表
                if (hasPraise) {
                    if (hasComment) {
                        this.line.setVisibility(View.VISIBLE);
                    } else {
                        this.line.setVisibility(View.GONE);
                    }
                    this.praiseListLayout.setVisibility(View.VISIBLE);
                    this.praiseListView.setVisibility(View.VISIBLE);
                    this.parise_icon.setVisibility(View.VISIBLE);
                    this.praiseListView.setDatas(praiseBeanList);
                    this.praiseListView.setOnItemClickListener(new PraiseListView.OnItemClickListener() {
                        @Override
                        public void onClick(int position) {
                            String userName = praiseBeanList.get(position).getSenderBean().getUsername();
                            String userId = praiseBeanList.get(position).getSenderBean().getId();
                            Toast.makeText(mContext, userName + " &id = " + userId, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    this.praiseListLayout.setVisibility(View.GONE);
                }
                // 处理评论列表
                if (hasComment) {
                    this.commentListView.setVisibility(View.VISIBLE);
                    this.commentListView.setDatas(commentsBeanList);
                    // 处理评论列表点击事件
                    this.commentListView.setOnItemClickListener(new CommentListView.OnItemClickListener() {
                        @Override
                        public void onItemClick(int commentPosition) {
                            final CommentsBean commentsBean = commentsBeanList.get(commentPosition);
                            if (DataTest.curUser.getId().equals(commentsBean.getSender().getId())) {//删除自己的评论
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                builder.setItems(deleteItems, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        mListener.onDeleteItemButtonClick(circlePosition, commentsBean.getCommentId());
                                    }
                                });
                                builder.create().show();
                            } else { //回复别人的评论
                                CommentConfig config = new CommentConfig();
                                config.circlePosition = circlePosition;
                                config.commentPosition = commentPosition;
                                config.commentType = CommentConfig.Type.REPLY;
                                config.replyUser = commentsBean.getSender();
                                mListener.onItemButtonClick(config);
                            }
                        }
                    });
                } else {
                    this.commentListView.setVisibility(View.GONE);
                }
            } else {
                this.linearlayoutAll.setVisibility(View.GONE);
            }
            //判断是否已点赞
            final String curUserPraiseId =DynamicBean.getCurUserPraiseId(DataTest.curUser.getId());
            if (!TextUtils.isEmpty(curUserPraiseId)) {
                this.likes.setText("取消");
                this.likesPicture.setBackgroundResource(R.drawable.dynamic_praise_s);
            } else {
                this.likes.setText("赞");
                this.likesPicture.setBackgroundResource(R.drawable.dynamic_praise_n);
            }
            this.likesLayout.setTag(circlePosition + "");
            this.likesLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (System.currentTimeMillis() - mLastTime < 700)//防止快速点击操作
                        return;
                    mLastTime = System.currentTimeMillis();
                    if ("赞".equals(likes.getText().toString().trim())) {
                        if (!(hasPraise && hasComment)) {//如果赞和评论都为空，此处需要做处理
                            linearlayoutAll.setVisibility(View.VISIBLE);
                            line.setVisibility(View.VISIBLE);
                            praiseListLayout.setVisibility(View.VISIBLE);
                            praiseListView.setVisibility(View.VISIBLE);
                            parise_icon.setVisibility(View.VISIBLE);
                        }
                        mListener.addPraise(circlePosition);
                        // 此处可调用接口
                    } else {
                        // 取消点赞
                        mListener.deletePraise(circlePosition, curUserPraiseId);
                        // 此处可调用接口
                    }
                }
            });
            // 评论
            this.linearlayoutComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommentConfig config = new CommentConfig();
                    config.circlePosition = circlePosition;
                    config.commentType = CommentConfig.Type.PUBLIC;
                    mListener.onItemButtonClick(config);
                }
            });
            // 删除功能
            this.deleteTv.setTag(circlePosition + "");
            if (null != DynamicBean.getSender()) {
                this.deleteTv.setText(DataTest.curUser.getId().equals(TextUtils.isEmpty(DynamicBean.getSender().getId()) ? "" : DynamicBean.getSender().getId()) ? "删除" : "");
            }
            this.deleteTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext); // MainActivity.this必须是当前activity的context，不能通过getApplicationContex获取context
                    builder.setTitle("操作提示");
                    builder.setMessage("是否删除朋友圈动态？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Toast.makeText(mContext, "点击了删除", Toast.LENGTH_SHORT);
                        }
                    });

                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
            });
        }
    }
    /**
     * 对外开放接口
     */
    public interface OnItemButtonClickListener {
        // 回复别人的评论
        void onItemButtonClick(CommentConfig config);

        // 删除动态
        void onDeleteItemButtonClick(int position, int commentId);

        // 添加赞
        void addPraise(int mCirclePosition);

        // 取消赞
        void deletePraise(int mCirclePosition, String mFavorId);
    }

}