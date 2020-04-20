package com.example.excelergo.niceexp;

import android.view.GestureDetector;
import android.view.MotionEvent;


public abstract class MyGestureDetectorlistener implements GestureDetector.OnGestureListener {

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent){

    }

    @Override
    public abstract boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1);
}
