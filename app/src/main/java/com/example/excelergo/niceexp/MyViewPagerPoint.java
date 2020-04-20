package com.example.excelergo.niceexp;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MyViewPagerPoint extends LinearLayout {
    private Context mContex;
    private int pNumber;
    public MyViewPagerPoint(Context context) {
        super(context);
    }
    public MyViewPagerPoint(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContex=context;


    }
    public MyViewPagerPoint(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContex = context;
    }

    public void setPagerPoint(int number){
        this.pNumber=number;
    }
    public void setpPosition(int position) {
       this.removeAllViews();
       for(int i=0;i<pNumber;i++){
           ImageView imageView=new ImageView(mContex);
           if(i==position){
               imageView.setImageResource(R.drawable.point_on);
           }else {
               imageView.setImageResource(R.drawable.point_off);
           }
           this.addView(imageView);
       }
    }
}
