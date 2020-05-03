package view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import interfaces.OnScrollChangeCallback;

public class MyScrollView extends ScrollView {
    private OnScrollChangeCallback mscrollChangeCallback;
    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(mscrollChangeCallback!=null){
            mscrollChangeCallback.onScroll(l,t,l-oldl,t-oldt);
        }
    }

    public void setMscrollChangeCallback(OnScrollChangeCallback mscrollChangeCallback) {
        this.mscrollChangeCallback = mscrollChangeCallback;
    }
}
