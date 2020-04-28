package com.example.excelergo.niceexp;

import android.content.Context;
import android.util.AttributeSet;

import com.tencent.smtt.sdk.WebView;

import utils.OnScrollChangeCallback;

public class MyWebView extends WebView {
    private OnScrollChangeCallback changeCallback;
    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (changeCallback != null) {
            changeCallback.onScroll(l, t, l - oldl, t - oldt);
        }
    }

    public void setChangeCallback(OnScrollChangeCallback changeCallback) {
        this.changeCallback = changeCallback;

    }
}
