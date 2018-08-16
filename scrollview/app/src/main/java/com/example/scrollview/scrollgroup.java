package com.example.scrollview;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by 张翔宇 on 2018/4/2.
 */

public class scrollgroup extends ViewGroup {
    int leftborder;
    int rightborder;
    Scroller scroller;
    int scrollx;
    int downx;
    int firstx;
    int lastx;
    private int mTouchSlop;

    public scrollgroup(Context context) {
        super(context);
    }

    public scrollgroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller=new Scroller(context);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        // 获取TouchSlop值
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);


    }

    public scrollgroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size=getChildCount();
        int measurwidth=0;
        int measurheight=0;
        for(int i=0;i<size;i++){
            View childview=getChildAt(i);
            measureChild(childview,widthMeasureSpec,heightMeasureSpec);

        }
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        for(int i=0;i<getChildCount();i++){
            View childView = getChildAt(i);
            // 为ScrollerLayout中的每一个子控件在水平方向上进行布局
            childView.layout(i * childView.getMeasuredWidth(), 0, (i + 1) * childView.getMeasuredWidth(),childView.getMeasuredHeight());
            Log.d("tag",childView.getMeasuredWidth()+" ");
        }
        leftborder=getChildAt(0).getLeft();
        rightborder=getChildAt(getChildCount()-1).getRight();
        Log.d("tag",getMeasuredWidth()+"父布局");
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downx= (int) ev.getRawX();
                firstx=downx;
                break;
            case MotionEvent.ACTION_MOVE:
                lastx= (int) ev.getRawX();
                int movex=Math.abs(firstx-lastx);
                if(movex>mTouchSlop){
                return true;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                lastx= (int) event.getRawX();
                scrollx=firstx-lastx;
                if(getScrollX()+scrollx<leftborder){
                    scrollTo(leftborder,0);
                }else if(getScrollX()+getWidth()+scrollx>rightborder){
                    scrollTo(rightborder-getWidth(),0);
                }
                else
                {
                    scrollBy(scrollx,0);
                }
                firstx=lastx;
                break;
            case MotionEvent.ACTION_UP:
                int treasurex=(getScrollX()+getWidth()/2)/getWidth();
                int dx=treasurex*getWidth()-getScrollX();
                scroller.startScroll(getScrollX(),0,dx,0);
                invalidate();
                break;


        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        if(scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

}
