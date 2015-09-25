package com.ogaclejapan.arclayout.demo.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by Bruce Too
 * On 9/25/15.
 * At 11:30
 */
public class NoTouchLayout extends FrameLayout{
    public NoTouchLayout(Context context) {
        super(context);
    }

    public NoTouchLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoTouchLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NoTouchLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!childCanTouch) {
            return super.onTouchEvent(event);
        }

        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(!childCanTouch) {
            return super.onInterceptTouchEvent(ev);
        }
        return true;
    }

    public boolean isChildCanTouch() {
        return childCanTouch;
    }


    public void setChildCanTouch(boolean childCanTouch) {
        this.childCanTouch = childCanTouch;
    }


    private boolean childCanTouch = true;
}
