package activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.excelergo.niceexp.MyGestureDetectorlistener;

import adapter.SkinChangeAdapter;
import adapter.URLSQLiteAdapter;
import entity.PageHistory;
import utils.BitmapFileutil;
import view.MyWebView;
import com.example.excelergo.niceexp.R;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import static fragment.Fragment2.css2;


public class NewsActivity extends AppCompatActivity implements View.OnTouchListener {
private MyWebView webView3;
private URLSQLiteAdapter adapter;
int skinposition;
private RelativeLayout titl_rl;
private SkinChangeAdapter skinChangeAdapter;
private GestureDetector detector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        skinChangeAdapter=new SkinChangeAdapter();
        webView3=(MyWebView) findViewById(R.id.wv);
        detector=new GestureDetector(this,detectorlistener);
        webView3.setOnTouchListener(this);
        titl_rl=findViewById(R.id.title_bar_news);
        loadimg();
        initWebView();

        adapter=new URLSQLiteAdapter(this);
        Intent intent=getIntent();
        String url =intent.getStringExtra("url");
        webView3.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

        });

        webView3.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                int currentMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                if(currentMode==Configuration.UI_MODE_NIGHT_YES){
                    view.loadUrl(css2);
                    view.setVisibility(newProgress==100? View.VISIBLE:View.GONE);
                }
            }
        });
        if(!url.equals("")){
            webView3.loadUrl(url);
            PageHistory pageHistory=new PageHistory();
            pageHistory.setTitle(webView3.getTitle());
            pageHistory.setUrl(webView3.getUrl());
            adapter.insertHistory(pageHistory);
            Bitmap icon=webView3.getFavicon();
            if(icon!=null){
               BitmapFileutil.saveImage(icon);
            }

        }
    }
    private void initWebView(){
        WebSettings webSettings=webView3.getSettings();
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
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setAppCacheEnabled(true);//开启 Application Caches 功能
    }
    private void loadimg() {
        SharedPreferences sharedPreferences = getSharedPreferences("img", MODE_PRIVATE);
        skinposition = sharedPreferences.getInt("img", 0);
        if (skinposition >= 0) {
            titl_rl.setBackgroundResource(skinChangeAdapter.skins2.get(skinposition).getLogo());
        }
    }
    private MyGestureDetectorlistener detectorlistener = new MyGestureDetectorlistener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {
            float xAxis = e2.getX() - e1.getX();
            float yAxis = e2.getY() - e1.getY();
            Log.i("手势", String.valueOf(xAxis));
            if (Math.abs(yAxis) <= 100) {
                if (xAxis > 400 && webView3.canGoBack() && Math.abs(v) > 300) {
                    webView3.goBack();
                } else if (xAxis < -400 && webView3.canGoForward() && Math.abs(v) > 300) {
                    webView3.goForward();
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
