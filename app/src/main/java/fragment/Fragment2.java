package fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.webkit.JavascriptInterface;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.PullToRefreshView;
import com.example.excelergo.niceexp.CheckAppPermissions;
import com.example.excelergo.niceexp.MyGestureDetectorlistener;

import activity.SearchHistoryActivity;
import adapter.MyMutiWindowAdapter;
import adapter.MySettingRecyclerAdapter;
import adapter.URLSQLiteAdapter;
import entity.PageHistory;
import interfaces.GoBackEndCallBack;
import interfaces.OnProgressChangeCallBack;
import interfaces.OnScrollChangeCallback;
import utils.BitmapFileutil;
import utils.FileDownloadutil;
import utils.GetFileFromUrlUtil;
import utils.GetUrlHtmlUtil;
import utils.ImgeClipUtil;
import view.MyScrollView;
import view.MyWebView;
import com.example.excelergo.niceexp.R;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebBackForwardList;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebHistoryItem;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import java.io.File;
import java.util.Objects;
import static com.example.excelergo.niceexp.MainActivity.bottom_bar;
import static com.example.excelergo.niceexp.MainActivity.popupmenu;
import static com.example.excelergo.niceexp.MainActivity.refreshLayout;
import static com.example.excelergo.niceexp.MainActivity.share_page;
import static com.example.excelergo.niceexp.MainActivity.title_bar;


public class Fragment2 extends Fragment implements View.OnTouchListener,View.OnClickListener {
    private EditText editText;
    public static MyWebView webView;
    public static RelativeLayout url_bar;
    private MyScrollView scrollView_f2;
    private MyMutiWindowAdapter windowAdapter;
    private ProgressBar progressBar;
    private ImageView imageGoback, imageSetting, imageWindow,imageGoforward;
    public static LinearLayout option_button;
    private URLSQLiteAdapter adapter;
    private String originUrl = "https://www.qq.com";
    private ImageView imageView_main_url, img_control;
    int lastLocation;
    String lasturl;
    private GestureDetector detector;
    private RelativeLayout et_area;
    public static RelativeLayout url_hint;
    int currentMode ;
    public static Handler handler_page;
    public ProgressDialog dialog;
    public static int pageSlide,noImge,noCache;
   /* public static String css = "javascript: (function() {\n" +
            "  \n" +
            "    css = document.createElement('link');\n" +
            "    css.id = 'xxx_browser_2014';\n" +
            "    css.rel = 'stylesheet';\n" +
            "    css.href = 'data:text/css,html,body,applet,object,h1,h2,h3,h4,h5,h6,blockquote,pre,abbr,acronym,address,big,cite,code,del,dfn,em,font,img,ins,kbd,q,p,s,samp,small,strike,strong,sub,sup,tt,var,b,u,i,center,dl,dt,dd,ol,ul,li,fieldset,form,label,legend,table,caption,tbody,tfoot,thead,th,td{background:rgba(0,0,0,1) !important;color:#000 !important;border-color:#000 !important;scaleable:true;}div,input,button,textarea,select,option,optgroup{background-color:#000 !important;color:#000 !important;border-color:#fff !important;scaleable:true;}a,a *{color:#ffffff !important; text-decoration:none !important;!important;background-color:rgba(0,0,0,1) !important;}a:active,a:hover,a:active *,a:hover *{color:#1F72D0 !important;background-color:rgba(0,0,0,1) !important;}p,span{font color:#FF0000 !important;color:#ffffff !important;background-color:rgba(0,0,0,1) !important;}html{-webkit-filter: contrast(100%)}';\n" +
            "    document.getElementsByTagName('head')[0].appendChild(css);\n" +
            "  \n" +
            "})();";*/
    public static String css2;
    public Fragment2() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, container, false);
        adapter=new URLSQLiteAdapter(getContext());
        currentMode=getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        initView(view);//初始化控件;
        initListener();//初始化点击监听
        //弹出上次程序未正常关闭浏览记录的窗口
        //弹出上次浏览的记录
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                url_hint.setVisibility(View.GONE);
            }
        },3000);
        webView();//初始化webview的各种配置
        loadSwitchState();
        detector = new GestureDetector(getActivity(), detectorlistener);//实例化手势监听器

        return view;
    }
    private void loadSwitchState(){
        SharedPreferences sp= Objects.requireNonNull(getContext()).getSharedPreferences("switch",Context.MODE_PRIVATE);
        pageSlide=sp.getInt("pageSlide",-1);
        noImge=sp.getInt("noImge",-1);
        noCache=sp.getInt("noCache",-1);
        if(noImge==1){
            WebSettings settings=webView.getSettings();
            settings.setLoadsImagesAutomatically(false);
            webView.reload();
        }

    }
    //获取当前网址的标题和url
    private String[] getCurrentUrl() {
        String s = "";
        String s1 = "";
        WebBackForwardList backForwardList = webView.copyBackForwardList();
        if (backForwardList != null && backForwardList.getSize() != 0) {
            WebHistoryItem historyItem = backForwardList.getCurrentItem();
            if(historyItem!=null) {
                s = historyItem.getUrl();
                s1 = historyItem.getTitle();
            }
        }
        String[] content = {s, s1};
        return content;
    }
//获取浏览网址记录并存储到数据库
    private void myLastUrl() {
        BitmapFileutil.createDir();//创建文件夹
        WebBackForwardList backForwardList = webView.copyBackForwardList();
        if (backForwardList != null && backForwardList.getSize() != 0) {
            int currentIndex = backForwardList.getCurrentIndex();
            WebHistoryItem historyItem = backForwardList.getItemAtIndex(currentIndex);
                if (historyItem != null) {
                    String backUrl = historyItem.getUrl();
                    String backTitle = historyItem.getTitle();
                    Bitmap icon = historyItem.getFavicon();
                    PageHistory pageHistory = new PageHistory();
                    pageHistory.setTitle(backTitle);
                    pageHistory.setUrl(backUrl);
                    if(noCache!=2) {
                        adapter.insertHistory(pageHistory);
                    }
                if (icon != null) {
                    BitmapFileutil.saveImage(icon);//保存网址图标到本地
                }
            }

        }

    }
    private void initView(View view) {
        //scrollView_f2=view.findViewById(R.id.sv_f2);
        url_bar=view.findViewById(R.id.url_bar);
        webView=view.findViewById(R.id.wv_music);
        editText = view.findViewById(R.id.et_url);
        progressBar = view.findViewById(R.id.pbar);
        imageGoback = view.findViewById(R.id.iv_option_left);
        imageSetting = view.findViewById(R.id.iv_option_center);
        imageWindow = view.findViewById(R.id.iv_option_right);
        imageGoforward=view.findViewById(R.id.iv_option_left2);
        option_button = view.findViewById(R.id.bottombar);
        imageView_main_url = view.findViewById(R.id.url_main_title);
        img_control = view.findViewById(R.id.control_webview);
        et_area = view.findViewById(R.id.et_area);
        url_hint=view.findViewById(R.id.url_hint);
        webView.addJavascriptInterface(new InJavaScriptLocalObject(), "java_obj");
        option_button.setVisibility(View.GONE);
        editText.requestFocus();
    }
    private void initListener(){
        webView.setOnTouchListener(this);//给WebView绑定触摸监听
        imageSetting.setOnClickListener(this);
        imageGoback.setOnClickListener(this);
        imageGoforward.setOnClickListener(this);
        imageWindow.setOnClickListener(this);
        et_area.setOnClickListener(this);
        url_bar.setOnClickListener(this);
        editText.setOnClickListener(this);

    }
    final class InJavaScriptLocalObject {
        @JavascriptInterface
        public void getSource(String html) {
            Log.i("html网址", html);

        }
    }

    private void webView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDomStorageEnabled(true); // 开启 DOM storage API 功能
        webSettings.setDatabaseEnabled(true);   //开启 database storage API 功能
        webSettings.setAppCacheEnabled(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setAppCacheEnabled(true);//开启 Application Caches 功能
        CookieSyncManager.createInstance(getContext());
        CookieSyncManager.createInstance(getContext()).sync();
        //长按退出全屏
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(title_bar.getVisibility()==View.GONE) {
                    TranslateAnimation animation=new TranslateAnimation(Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,-1.0f,Animation.RELATIVE_TO_SELF,0.0f);
                    animation.setDuration(500);
                    TranslateAnimation animation2=new TranslateAnimation(Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,1.0f,Animation.RELATIVE_TO_SELF,0.0f);
                    animation2.setDuration(500);
                    title_bar.startAnimation(animation);
                    url_bar.startAnimation(animation);
                    bottom_bar.startAnimation(animation2);
                    title_bar.setVisibility(View.VISIBLE);
                    bottom_bar.setVisibility(View.VISIBLE);
                    url_bar.setVisibility(View.VISIBLE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    return true;
                }
                return false;
            }
        });
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(final String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                CheckAppPermissions.geFilePermission(getActivity());
                new AlertDialog.Builder(getContext())
                        .setMessage("即将下载文件?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface di, int i) {
                               dialog=new ProgressDialog(getContext()) ;
                               GetFileFromUrlUtil.getFileFromUrl(url, handler_page, new OnProgressChangeCallBack() {
                                   @Override
                                   public void OnChangeState(int length, int total) {
                                       dialog.setMax(length);
                                       dialog.setProgress(total);
                                       Log.i("状态",String.valueOf(total));
                                   }
                               });


                                dialog.setIcon(R.drawable.appicon);
                                dialog.setTitle("NiceExp提示您");
                                dialog.setMessage("文件下载中");
                                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                dialog.setCancelable(false);
                                dialog.setButton("取消", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialogInterface, int i) {
                                   GetFileFromUrlUtil.setCallBack(new GoBackEndCallBack() {
                                       @Override
                                       public void BackEnd() {
                                           FileDownloadutil.deleteFile();
                                       }

                                   });
                                   }
                               });
                                dialog.setButton2("后台下载", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialogInterface, int i) {
                                       dialog.dismiss();
                                   }
                               });
                                dialog.show();

                        }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface di, int i) {
                                di.cancel();
                            }
                        })
                        .show();



            }
        });
        //点击滑倒网页顶部位置
        img_control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              webView.scrollTo(0,0);
              Log.i("滑动","有用");
            }
        });
        //设置根据WebView的滑动底部选项栏的动作
        webView.setChangeCallback(new OnScrollChangeCallback() {
            @Override
            public void onScroll(int dx, final int dy, int dx_change, int dy_change) {
                Log.i("滚动", String.valueOf(dy));
                refreshLayout.setEnabled(false);
                if (dy == 0) {
                    refreshLayout.setEnabled(true);
                    refreshLayout.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            webView.reload();
                            refreshLayout.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    refreshLayout.setRefreshing(false);
                                }
                            },1000);
                        }
                    });
                }
                if(!webView.canScrollVertically(View.SCROLL_AXIS_VERTICAL)){
                    refreshLayout.setEnabled(false);
                }
                if (dy_change > 80) {
                        inserthistory(dy);
                        if (img_control.getVisibility() == View.VISIBLE) {
                        //控制按钮
                        img_control.startAnimation(arrowShowOffAnimation());
                        TranslateAnimation translateAnimation2 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
                        translateAnimation2.setDuration(500);
                        //底部选项栏
                        option_button.startAnimation(translateAnimation2);
                        TranslateAnimation urlAnoimation=new TranslateAnimation(Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,-1.0f);
                        urlAnoimation.setDuration(500);
                        AlphaAnimation urlAlpha=new AlphaAnimation(1.0f,0.0f);
                        urlAlpha.setDuration(500);
                        AnimationSet set=new AnimationSet(true);
                        set.addAnimation(urlAnoimation);
                        set.addAnimation(urlAlpha);
                        //顶部网址栏
                        //url_bar.startAnimation(set);

                    }
                    url_bar.setVisibility(View.GONE);
                    option_button.setVisibility(View.GONE);
                    img_control.setVisibility(View.GONE);
                } else if (dy_change < -80) {
                    if (img_control.getVisibility() == View.GONE) {
                        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                        translateAnimation.setDuration(1000);
                        img_control.startAnimation(translateAnimation);
                        TranslateAnimation translateAnimation2 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                        translateAnimation2.setDuration(500);
                        option_button.startAnimation(translateAnimation2);
                        TranslateAnimation urlAnoimation=new TranslateAnimation(Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,-1.0f,Animation.RELATIVE_TO_SELF,0.0f);
                        urlAnoimation.setDuration(500);
                        AlphaAnimation urlAlpha=new AlphaAnimation(0.0f,1.0f);
                        urlAlpha.setDuration(500);
                        AnimationSet set=new AnimationSet(true);
                        set.addAnimation(urlAnoimation);
                        set.addAnimation(urlAlpha);
                        url_bar.startAnimation(set);
                    }
                    url_bar.setVisibility(View.VISIBLE);
                    img_control.setVisibility(View.VISIBLE);
                    option_button.setVisibility(View.VISIBLE);

                }
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(!url.startsWith("http")){
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);



            }

            @Override
            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:window.java_obj.getSource('<head>'+" +
                        "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                super.onPageFinished(view, url);
                if(webView.canGoBack()){
                    imageGoback.setImageResource(R.drawable.backspace);
                }else {
                    imageGoback.setImageResource(R.drawable.backspace4);
                }
                if(webView.canGoForward()){
                    imageGoforward.setImageResource(R.drawable.backspace2);
                }else {
                    imageGoforward.setImageResource(R.drawable.backspace3);
                }
                myLastUrl();
            }

        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setMax(100);
                progressBar.setProgress(newProgress);
                progressBar.setVisibility(newProgress==100?View.GONE:View.VISIBLE);
                editText.setText(getCurrentUrl()[1]);
                insertSearchHistory();

                    Bitmap bitmap = ImgeClipUtil.getLocalBitmap(BitmapFileutil.ImgPath+ webView.getTitle() + ".jpg");//加载从网络存储到本地的图标
                    if (bitmap != null) {
                        imageView_main_url.setImageBitmap(ImgeClipUtil.ClipSquareBitmap(bitmap, 200, bitmap.getWidth()));//对图标进行裁切处理
                    } else {
                        imageView_main_url.setImageResource(R.drawable.unknown2);
                    }

                    //夜间模式webview的设置
                    if (currentMode == Configuration.UI_MODE_NIGHT_YES) {
                       view.loadUrl(css2);
                        //view.loadUrl(css);
                        //view.loadUrl("javascript:function()");
                        view.setVisibility(newProgress==100?View.VISIBLE:View.INVISIBLE);
                    }

            }
        });
        handler_page=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 11:
                        Log.i("html内容",msg.obj.toString());
                        //webView.loadDataWithBaseURL(null,msg.obj.toString(),"text/html","utf-8",null);
                        break;
                    case 99:
                        dialog.dismiss();
                        Toast.makeText(getContext(),"取消下载",Toast.LENGTH_SHORT).show();
                        break;
                    case 100:
                        dialog.dismiss();
                        new AlertDialog.Builder(getActivity())
                                .setMessage("是否打开文件?")
                                .setPositiveButton("打开", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //打开文件
                                        String fileLocation=FileDownloadutil.FilePath+FileDownloadutil.fileName;
                                        File file=new File(fileLocation);
                                        Uri uri=null;
                                        if(Build.VERSION.SDK_INT<=Build.VERSION_CODES.M){
                                            uri=Uri.fromFile(file);
                                        }else {
                                            uri=FileProvider.getUriForFile(getActivity(),"com.example.excelergo.niceexp.provider",file);
                                        }
                                        String type=FileDownloadutil.getMimeType(file);
                                        Intent intent=new Intent();
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION| Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                        intent.setAction(Intent.ACTION_VIEW);
                                        intent.setDataAndType(uri,type);
                                        startActivity(intent);

                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                }).show();

                        break;
                }
            }
        };
        GetUrlHtmlUtil.getUrlHtml(originUrl,handler_page);
        webView.loadUrl(originUrl);


    }

    private AnimationSet arrowShowOffAnimation(){
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        translateAnimation.setDuration(1000);
        RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(rotateAnimation);
        set.addAnimation(translateAnimation);
        return set;
     }

    private void insertSearchHistory(){
        String oldurl=getCurrentUrl()[0];
        String oldtitle=getCurrentUrl()[1];
        SharedPreferences sp= Objects.requireNonNull(getContext()).getSharedPreferences("olddata",Context.MODE_PRIVATE);
        if(sp!=null) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("oldurl", oldurl);
            editor.putString("oldtitle", oldtitle);
            editor.apply();
        }
            Log.i("轻型数据库olddata", oldurl);

    }
    //当前网页浏览记录插入SharedPreferences
    private void inserthistory(int location) {
        lastLocation=location;
        lasturl=getCurrentUrl()[0];
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("locationdata", Context.MODE_PRIVATE);
        if(sharedPreferences!=null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("history", lastLocation);
            editor.putString("lasturl", lasturl);
            editor.apply();
        }
    }

//webview触摸监听
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (popupmenu.getVisibility() == View.VISIBLE) {
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.popup_menu_anim2);
            popupmenu.startAnimation(animation);
            popupmenu.setVisibility(View.GONE);
        }
        if(share_page.getVisibility()==View.VISIBLE){
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.popup_menu_anim2);
            share_page.startAnimation(animation);
            share_page.setVisibility(View.GONE);
        }
        editText.clearFocus();
        return detector.onTouchEvent(motionEvent);

    }
    private MyGestureDetectorlistener detectorlistener = new MyGestureDetectorlistener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, final float v, float v1) {
            final float xAxis = e2.getX() - e1.getX();
            final float yAxis = e2.getY() - e1.getY();
            Log.i("手势", String.valueOf(xAxis));
            if(pageSlide==0) {
                if (Math.abs(yAxis) <= 100) {
                    if (xAxis > 300 && webView.canGoBack() && Math.abs(v) > 300) {
                        webView.goBack();
                    } else if (xAxis < -300 && webView.canGoForward() && Math.abs(v) > 300) {
                        webView.goForward();
                    }
                }
            }
            return false;
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_option_center:
                if (popupmenu.getVisibility() == View.GONE) {
                    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.popup_menu_anim);
                    popupmenu.startAnimation(animation);
                    popupmenu.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.et_url:
                Intent intent2=new Intent(getActivity(), SearchHistoryActivity.class);
                startActivity(intent2);
                break;
            case R.id.iv_option_right:
                webView.loadUrl(originUrl);
                break;
            case R.id.iv_option_left:
                webView.goBack();
                break;
            case R.id.iv_option_left2:
                webView.goForward();
                break;
        }
    }

}