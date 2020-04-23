package com.example.excelergo.niceexp;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.squareup.okhttp.OkHttpClient;
import static com.example.excelergo.niceexp.Fragment2.css;
import static com.example.excelergo.niceexp.MainActivity.css2;

public class Fragment1 extends Fragment implements View.OnTouchListener{
public static WebView Webview2;
private GestureDetector detector;//手势监听
String temp,weather;
int currentMode ;
    private OkHttpClient okHttpClient=new OkHttpClient();
    public Fragment1() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment1, container, false);
        currentMode=getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        Webview2=view.findViewById(R.id.wv_f1);
        detector = new GestureDetector(getActivity(), detectorlistener);//实例化手势监听器
        Webview2.setOnTouchListener(this);//给WebView绑定触摸监听
        initWebView();

        Webview2.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);


            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

        });
        Webview2.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                if (currentMode == Configuration.UI_MODE_NIGHT_YES) {
                    view.loadUrl(css);
                    view.setVisibility(newProgress == 100 ? View.VISIBLE : View.INVISIBLE);
                }else {
                    view.setVisibility(newProgress == 100 ? View.VISIBLE : View.INVISIBLE);
                }
            }
        });
        Webview2.loadUrl("http://www.1001p.com/1");
        return view;

    }
    private void initWebView(){
        Webview2.setVisibility(View.GONE);
        WebSettings webSettings = Webview2.getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDomStorageEnabled(true); // 开启 DOM storage API 功能
        webSettings.setDatabaseEnabled(true);   //开启 database storage API 功能
        webSettings.setAppCacheEnabled(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.NORMAL);
        webSettings.setAppCacheEnabled(true);//开启 Application Caches 功能
    }
    //实现左右滑动切换网页
    private MyGestureDetectorlistener detectorlistener = new MyGestureDetectorlistener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {
            float xAxis = e2.getX() - e1.getX();
            float yAxis = e2.getY() - e1.getY();
            Log.i("手势1", String.valueOf(xAxis));
            if (Math.abs(yAxis) <= 100) {
                if (xAxis > 400 && Webview2.canGoBack() && Math.abs(v) > 300) {
                    Webview2.goBack();
                } else if (xAxis < -400 && Webview2.canGoForward() && Math.abs(v) > 300) {
                    Webview2.goForward();
                }
            }
            return false;
        }
    };

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return detector.onTouchEvent(motionEvent);
    }

}
