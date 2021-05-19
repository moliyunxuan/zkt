package com.example.zkt.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.zkt.R;
import com.example.zkt.adapter.CommunityAdapter;
import com.example.zkt.bean.CommentConfig;
import com.example.zkt.bean.CommentsBean;
import com.example.zkt.bean.DynamicBean;
import com.example.zkt.bean.ImageBean;
import com.example.zkt.bean.PraiseBean;
import com.example.zkt.bean.SenderBean;
import com.example.zkt.keyboard.KeyboardUtility;
import com.example.zkt.ui.widget.CommentListView;
import com.example.zkt.ui.widget.DivItemDecoration;
import com.example.zkt.util.DataTest;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;

import cn.leancloud.json.JSONObject;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.zkt.util.DataTest.createPraiseItemList;
import static com.example.zkt.util.DataTest.getDynamicByDate;

/**
 * author：moliyunxuan
 * date：created by 2021/4/21
 * description：社区首页
 */
public class CommunityActivity extends AppCompatActivity implements CommunityAdapter.OnItemButtonClickListener {
    private SuperRecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    private  CommunityAdapter adapter;
    private final static int TYPE_PULL_REFRESH = 1;
    private final static int TYPE_PULL_REFRESHMORE = 2;
    private CommentConfig commentConfig;
    private LinearLayout editTextBody;
    private EditText editText;
    private Button commentSend;
    private String TAG = CommunityActivity.class.getSimpleName();
    private RelativeLayout bodyLayout;
    private int currentKeyboardH;// 当前软键盘的高度
    private int screenHeight; // 当前屏幕高度
    private int editTextBodyHeight; // 当前输入框的高度
    private int selectCircleItemH; // 当前选中朋友圈的高度
    private int selectCommentItemOffset; // 当前选中评论item的偏移量
    private RelativeLayout topTitle;
    private OkHttpClient mHttpClient;
    private List<DynamicBean> list;
    private List<AVObject> Dynamics;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commmunity);
        this.init();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent();
                intent.setClass(getApplicationContext(),CreateDynamicActivity.class);
                startActivity(intent);
            }
        });
    }

    private void init() {
        this.initModule();
        this.initData();
        this.addListener();
        this.setViewTreeObserver();
    }

    private void initModule() {

        this.imageView = findViewById(R.id.iv_add_dynamic);
        this.recyclerView = (SuperRecyclerView) findViewById(R.id.community_recyclerView);
        this.editTextBody = (LinearLayout) findViewById(R.id.editTextBodyLl);
        this.editText = (EditText) findViewById(R.id.comment_et);
        this.commentSend = (Button) findViewById(R.id.comment_send);
        this.topTitle = (RelativeLayout) findViewById(R.id.top);
        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.addItemDecoration(new DivItemDecoration(2, true));
        this.recyclerView.getMoreProgressView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
    }

    private void initData() {
        getTweetListData();
        //实现自动下拉刷新功能
        this.recyclerView.getSwipeToRefresh().post(new Runnable() {
            @Override
            public void run() {
                recyclerView.setRefreshing(true);//执行下拉刷新的动画
                refreshListener.onRefresh();//执行数据加载操作
            }
        });
        this.adapter = new CommunityAdapter(this, this);
        this.recyclerView.setAdapter(adapter);
    }

    private void addListener() {
        this.refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadData(TYPE_PULL_REFRESH);
                    }
                }, 2000);
            }
        };
        this.recyclerView.setRefreshListener(refreshListener);
        this.recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                } else {
                }
            }
        });
        // 触发listView的时候关闭
        this.recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (editTextBody.getVisibility() == View.VISIBLE) {
                    updateEditTextBodyVisible(View.GONE, null);
                    return true;
                }
                return false;
            }
        });
        // 发布评论
        this.commentSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editText.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(CommunityActivity.this, "评论内容不能为空...", Toast.LENGTH_SHORT).show();
                    return;
                }
                CommentsBean commentsBean = null;
                if (commentConfig.commentType == CommentConfig.Type.PUBLIC) { //自己发表评论
                    commentsBean = DataTest.createPublicComment(content);
                } else if (commentConfig.commentType == CommentConfig.Type.REPLY) {//回复别人的评论
                    commentsBean = DataTest.createReplyComment(commentConfig.replyUser, content);
                }
                updateAddComment(commentConfig.circlePosition, commentsBean);
                updateEditTextBodyVisible(View.GONE, null);
            }
        });
    }

    /**
     * 加载数据
     *
     * @param loadType
     */
    public void loadData(int loadType) {
        updateLoadData(loadType, list);
    }

    /**
     * 更新数据，区分是上拉还是下拉
     *  @param loadType
     * @param datas
     */
    public void updateLoadData(int loadType, List<DynamicBean> datas) {
        if (loadType == TYPE_PULL_REFRESH) { //下拉刷新
            recyclerView.setRefreshing(false);
            adapter.setDatas(datas);
        } else if (loadType == TYPE_PULL_REFRESHMORE) {//加载底部更多
            adapter.getDatas().addAll(datas.subList(0, 3));//取5条
        }
        adapter.notifyDataSetChanged();
        if (null != adapter.getDatas() && adapter.getDatas().size() > 0) {
            if (adapter.getDatas().size() < 100 + CommunityAdapter.HEADVIEW_SIZE) {
                recyclerView.setupMoreListener(new OnMoreListener() {
                    @Override
                    public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadData(TYPE_PULL_REFRESHMORE);
                            }
                        }, 2000);

                    }
                }, 5);
            } else {
                recyclerView.removeMoreListener();
                recyclerView.hideMoreProgress();
            }
        } else {
            recyclerView.removeMoreListener();
            recyclerView.hideMoreProgress();
        }
    }

    @Override
    public void onItemButtonClick(CommentConfig config) {
        commentConfig = config;
        if (commentConfig.commentType == CommentConfig.Type.REPLY) {
            editText.setHint("回复" + commentConfig.replyUser.getUsername() + ":");
        }
        this.updateEditTextBodyVisible(View.VISIBLE, commentConfig);
    }

    @Override
    public void onDeleteItemButtonClick(int position, int commentId) {

    }

    @Override
    public void addPraise(int mCirclePosition) {
        PraiseBean praiseBean = DataTest.createCurUserPraiseItem();
        updateAddPraise(mCirclePosition, praiseBean);
    }

    /**
     * 设置软键盘的显示隐藏
     *
     * @param visibility
     */
    public void updateEditTextBodyVisible(int visibility, CommentConfig config) {
        this.commentConfig = config;
        this.editTextBody.setVisibility(visibility);
        // 计算偏移量
        this.measureCircleItemHighAndCommentItemOffset(commentConfig);
        if (View.VISIBLE == visibility) {
            editText.requestFocus();
            //弹出键盘
            KeyboardUtility.showSoftInput(editText.getContext(), editText);
        } else if (View.GONE == visibility) {
            //隐藏键盘
            KeyboardUtility.hideSoftInput(editText.getContext(), editText);
        }
    }

    /**
     * 取消赞
     *
     * @param mCirclePosition
     * @param mFavorId
     */
    @Override
    public void deletePraise(int mCirclePosition, String mFavorId) {
        DynamicBean item = adapter.getDatas().get(mCirclePosition);
        List<PraiseBean> items = item.getPraiseList();
        for (int i = 0; i < items.size(); i++) {
            if (mFavorId.equals(items.get(i).getId())) {
                items.remove(i);
                adapter.notifyDataSetChanged();
                return;
            }
        }
    }

    /**
     * 添加赞
     *
     * @param circlePosition
     * @param addItem
     */
    public void updateAddPraise(int circlePosition, PraiseBean addItem) {
        if (addItem != null) {
            DynamicBean item = adapter.getDatas().get(circlePosition);
            item.getPraiseList().add(addItem);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (editTextBody != null && editTextBody.getVisibility() == View.VISIBLE) {
                updateEditTextBodyVisible(View.GONE, null);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 添加评论
     *
     * @param circlePosition
     * @param addItem
     */
    public void updateAddComment(int circlePosition, CommentsBean addItem) {
        if (addItem != null) {
            DynamicBean item = adapter.getDatas().get(circlePosition);
            if (null != item.getComments()) {
                item.getComments().add(addItem);
            } else {
                List<CommentsBean> list = new ArrayList<>();
                list.add(addItem);
                item.setComments(list);
            }
            adapter.notifyDataSetChanged();
        }
        //清空评论文本
        this.editText.setText("");
    }

    /**
     * 计算软键盘的高度
     */
    private void setViewTreeObserver() {
        bodyLayout = (RelativeLayout) findViewById(R.id.bodyLayout);
        final ViewTreeObserver swipeRefreshLayoutVTO = bodyLayout.getViewTreeObserver();
        swipeRefreshLayoutVTO.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                bodyLayout.getWindowVisibleDisplayFrame(r);
                int statusBarH = getStatusBarHeight();//状态栏高度
                int screenH = bodyLayout.getRootView().getHeight();
                if (r.top != statusBarH) {
                    //在这个demo中r.top代表的是状态栏高度，在沉浸式状态栏时r.top＝0，通过getStatusBarHeight获取状态栏高度
                    r.top = statusBarH;
                }
                int keyboardH = screenH - (r.bottom - r.top);
                Log.d(TAG, "screenH＝ " + screenH + " &keyboardH = " + keyboardH + " &r.bottom=" + r.bottom + " &top=" + r.top + " &statusBarH=" + statusBarH);

                if (keyboardH == currentKeyboardH) {//有变化时才处理，否则会陷入死循环
                    return;
                }
                currentKeyboardH = keyboardH;
                screenHeight = screenH;//应用屏幕的高度
                editTextBodyHeight = editTextBody.getHeight();

                if (keyboardH < 150) {//说明是隐藏键盘的情况
                    updateEditTextBodyVisible(View.GONE, null);
                    return;
                }
                //偏移listview
                if (layoutManager != null && commentConfig != null) {
                    layoutManager.scrollToPositionWithOffset(commentConfig.circlePosition + CommunityAdapter.HEADVIEW_SIZE, getListViewOffset(commentConfig));
                }
            }
        });
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 计算偏移量
     *
     * @param commentConfig
     */
    private void measureCircleItemHighAndCommentItemOffset(CommentConfig commentConfig) {
        if (commentConfig == null)
            return;

        int firstPosition = layoutManager.findFirstVisibleItemPosition();
        //只能返回当前可见区域（列表可滚动）的子项
        View selectCircleItem = layoutManager.getChildAt(commentConfig.circlePosition + CommunityAdapter.HEADVIEW_SIZE - firstPosition);

        if (selectCircleItem != null) {
            selectCircleItemH = selectCircleItem.getHeight();
        }
        if (commentConfig.commentType == CommentConfig.Type.REPLY) {
            //回复评论的情况
            CommentListView commentLv = (CommentListView) selectCircleItem.findViewById(R.id.commentList);
            if (commentLv != null) {
                //找到要回复的评论view,计算出该view距离所属动态底部的距离
                View selectCommentItem = commentLv.getChildAt(commentConfig.commentPosition);
                if (selectCommentItem != null) {
                    //选择的commentItem距选择的CircleItem底部的距离
                    selectCommentItemOffset = 0;
                    View parentView = selectCommentItem;
                    do {
                        int subItemBottom = parentView.getBottom();
                        parentView = (View) parentView.getParent();
                        if (parentView != null) {
                            selectCommentItemOffset += (parentView.getHeight() - subItemBottom);
                        }
                    } while (parentView != null && parentView != selectCircleItem);
                }
            }
        }
    }

    /**
     * 测量偏移量
     *
     * @param commentConfig
     * @return
     */
    private int getListViewOffset(CommentConfig commentConfig) {
        if (commentConfig == null)
            return 0;

        //这里如果你的listview上面还有其它占高度的控件，则需要减去该控件高度，listview的headview除外。
        //int listviewOffset = mStcreenHeight - mSelectCircleItemH - mCurrentKeyboardH - mEditTextBodyHeight;
        int listviewOffset = screenHeight - selectCircleItemH - currentKeyboardH - editTextBodyHeight - topTitle.getHeight();
        if (commentConfig.commentType == CommentConfig.Type.REPLY) {
            //回复评论的情况
            listviewOffset = listviewOffset + selectCommentItemOffset;
        }
        Log.i(TAG, "listviewOffset : " + listviewOffset);
        return listviewOffset;
    }

    /**
     * 获取列表数据
     */
    public void getTweetListData() {


        AVQuery<AVObject> query = new AVQuery<>("DynamicBean");
        // 按 createdAt 降序排列
        query.orderByDescending("createdAt");
        query.findInBackground().subscribe(new Observer<List<AVObject>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<AVObject> avObjects) {
                Dynamics = avObjects;


            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {


                ArrayList<DynamicBean> dynamicList = new ArrayList<>();

                int circleId = 0;
                for (AVObject avObject : Dynamics) {

                    DynamicBean circle = new DynamicBean();
                    SenderBean senderBean = new SenderBean();

                    List<CommentsBean> commentsBeans = new ArrayList<>();
                    List<ImageBean> imageBeans = new ArrayList<>();
                    List<PraiseBean> praiseBeans = new ArrayList<>();


                    JSONObject sender = avObject.getJSONObject("sender");
                    String Sendernickname = sender.getString("nickname");
                    Log.d("msg1", "" + Sendernickname);
                    String SenderAvatar = sender.getString("avatar");
                    String content = avObject.getString("content");

                    List comments = avObject.getList("comments");
                    if(comments!=null)
                    {
                        for (int i = 0; i < comments.size(); i++) {
                            CommentsBean commentsBean = new CommentsBean();
                            HashMap<Object, Object> comment = (HashMap<Object, Object>) comments.get(i);
                            SenderBean commentman = new SenderBean();
                            Log.d("msg", "" + comment.get("content"));
                            commentsBean.setContent((String) comment.get("content"));
                            commentman.setUsername((String) comment.get("nikename"));

                            Log.d("msg11","查询到的评论名字"+ comment.get("nikename"));
                            commentman.setId((String) comment.get("id"));
                            commentsBean.setSender(commentman);
                            commentsBeans.add(commentsBean);
                        }

                    }


                    List images = avObject.getList("images");
                    if(images!=null)
                    {
                        for(int i=0;i<images.size();i++) {
                            ImageBean imageBean = new ImageBean();
                            String image = (String) images.get(i);
                            Log.d("msg2", ""+image);
                            imageBean.setUrl(image);
                            imageBeans.add(imageBean);
                        }
                    }

                    List praiseMans = avObject.getList("praiseMans");

                    if(praiseMans!=null)
                    {
                        for(int i=0;i<praiseMans.size();i++) {
                            PraiseBean praiseBean = new PraiseBean();
                            SenderBean praiseMan = new SenderBean();
                            praiseMan.setUsername((String) praiseMans.get(i));
                            praiseBean.setSenderBean(praiseMan);
                            praiseBeans.add(praiseBean);
                        }

                    }





                    senderBean.setNick(Sendernickname);
                    senderBean.setAvatar(SenderAvatar);

                    circle.setId(circleId++);
                    circle.setSender(senderBean);
                    circle.setContent(content);
                    circle.setComments(commentsBeans);
                    circle.setImages(imageBeans);
                    circle.setDt(String.valueOf(System.currentTimeMillis()));
                    circle.setPraiseList(praiseBeans);

                    dynamicList.add(circle);
                }
                list = dynamicList;
            }
        });
//
//    public void getTweetListData() {
//        String url = "http://thoughtworks-ios.herokuapp.com/user/jsmith/tweets";
//        mHttpClient = new OkHttpClient();
//        Request request = new Request.Builder().url(url).build();
//        mHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.d("msg","获取数据失败"+e);
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String json = response.body().string();
//                Log.d("msg","获取数据成功");
//                if (json != null) {
//                    Log.d("okHttp", json);
//                    Log.d("msg",""+json);
//                    list = DataTest.getCircleDyData(json);
//                    Log.d("msg121",""+list);
//                }
//            }
//        });
//    }

    }

}








