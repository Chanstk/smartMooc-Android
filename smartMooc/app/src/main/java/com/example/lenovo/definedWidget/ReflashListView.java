package com.example.lenovo.definedWidget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lenovo.smartMooc.R;

import io.vov.vitamio.utils.Log;

/**
 * Created by lenovo on 2015/11/10.
 */
public class ReflashListView extends ListView implements AbsListView.OnScrollListener {
    private View header;
    private int headerHeight;//顶部header 高度
    private int firstVisibleItem;//当前第一个可见的item索引
    private boolean isRemark;//标记 当前是在第一个item摁下
    private int startY;//摁下的坐标y值
    private int state;//当前状态
    final int NONE = 0;//正常状态
    final int PULL = 1;//提示下拉状态
    final int RELEASE = 2;//提示松开刷新状态
    final int REFLASHING = 3;//刷新时状态
    private int scrollState;//listview当前滚动状态
    reflashListener listener;//刷新数据接口

    public ReflashListView(Context context) {
        super(context);
        init(context);
    }

    public ReflashListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ReflashListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    public void reflesh(){
        state = REFLASHING;
        reflashViewByState();
    }
    /*
    初始化
     */
    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        header = inflater.inflate(R.layout.header_layout, null);
        measureView(header);
        headerHeight = header.getMeasuredHeight();
        Log.i("header", "headerHeight = " + headerHeight);
        topPadding(-headerHeight);
        this.addHeaderView(header);
        this.setOnScrollListener(this);
    }

    /*
    通知父布局获取header宽高度
     */
    private void measureView(View view) {
        ViewGroup.LayoutParams p = view.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int width = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int height;
        int tempHeight = p.height;
        if (tempHeight > 0) {
            height = MeasureSpec.makeMeasureSpec(tempHeight, MeasureSpec.EXACTLY);
        } else {
            height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        view.measure(width, height);
    }

    /*
    设置header 布局的上边距
     */
    private void topPadding(int topPadding) {
        header.setPadding(header.getPaddingLeft(), topPadding, header.getPaddingRight(), header.getPaddingBottom());
        header.invalidate();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollState = scrollState;
    }

    //firstVisibleItem第一个可见item的索引
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (firstVisibleItem == 0) {
                    isRemark = true;
                    startY = (int) ev.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                onMove(ev);
                break;
            case MotionEvent.ACTION_UP:
                if (state == RELEASE) {
                    state = REFLASHING;
                    //加载最新数据
                    reflashViewByState();
                    listener.onReflash();
                } else if (state == PULL) {
                    state = NONE;
                    isRemark = false;
                    reflashViewByState();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }


    /*
          判断移动过程中的操作
     */
    private void onMove(MotionEvent ev) {
        if (!isRemark)
            return;
        int tempY = (int) ev.getY();
        int space =( tempY - startY)/3;//移动距离
        int topPadding = space - headerHeight;//移动过程中顶部布局padding
        switch (state) {
            case NONE:
                if (space > 0) {
                        state = PULL;
                        reflashViewByState();
                }
            break;
            case PULL:
                topPadding(topPadding);
                if (space > headerHeight + 30 && scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    state = RELEASE;
                    reflashViewByState();
                }
                break;
            case RELEASE:
                topPadding(topPadding);
                if (space < headerHeight + 30) {
                    state = PULL;
                    reflashViewByState();
                } else if (space <= 0) {
                    state = NONE;
                    reflashViewByState();
                    isRemark = false;
                }
                break;
            case REFLASHING:
                break;
        }
    }

    /*
     根据当前状态显示不同布局
     */
    private void reflashViewByState() {
        TextView tip = (TextView) header.findViewById(R.id.tip);
        ImageView arrow = (ImageView) header.findViewById(R.id.arrow);
        ProgressBar progressBar = (ProgressBar) header.findViewById(R.id.progress);
        RotateAnimation anim = new RotateAnimation(0, 180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(500);
        anim.setFillAfter(true);
        RotateAnimation anim1 = new RotateAnimation(180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        anim1.setDuration(500);
        anim1.setFillAfter(true);
        switch (state) {
            case NONE:
                arrow.clearAnimation();
                topPadding(-headerHeight);
                break;
            case PULL:
                arrow.setVisibility(VISIBLE);
                progressBar.setVisibility(GONE);
                tip.setText("下拉可以刷新!");
                arrow.clearAnimation();
                break;
            case RELEASE:
                arrow.setVisibility(VISIBLE);
                progressBar.setVisibility(GONE);
                tip.setText("松开刷新");
                arrow.clearAnimation();
                arrow.setAnimation(anim);
                break;
            case REFLASHING:
                topPadding(headerHeight-150);
                arrow.setVisibility(GONE);
                progressBar.setVisibility(VISIBLE);
                tip.setText("正在刷新...");
                arrow.clearAnimation();
                break;
        }
    }
    /*
    获取完数据
     */
    public void reflashComplete(){
        state = NONE;
        isRemark = false;
        reflashViewByState();
    }
    public void setInterface(reflashListener listener){
        this.listener = listener;
    }
    /*
        刷新数据接口
     */
    public interface reflashListener{
        public void onReflash();
    }
}

