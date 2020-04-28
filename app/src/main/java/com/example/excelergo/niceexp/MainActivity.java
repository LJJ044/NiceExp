package com.example.excelergo.niceexp;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.PullToRefreshView;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.tencent.smtt.sdk.WebBackForwardList;
import com.tencent.smtt.sdk.WebHistoryItem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import utils.OnScrollChangeCallback;

import static com.example.excelergo.niceexp.Fragment1.Webview2;
import static com.example.excelergo.niceexp.Fragment2.option_button;
import static com.example.excelergo.niceexp.Fragment2.url_bar;
import static com.example.excelergo.niceexp.Fragment2.url_hint;
import static com.example.excelergo.niceexp.Fragment2.webView;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,ViewPager.OnPageChangeListener,View.OnClickListener {
    private MyFragmentViewPagerAdapter myFragmentViewPagerAdapter;
    public static RadioButton btn1,btn2,btn3,btn4;
    private RadioGroup radioGroup;
    public static MyViewPager viewPager;
    private ListView listView,listView2;
    private ImageView imageView,imageView2;
    private DrawerLayout drawerLayout,drawerLayout2;
    private MyDrawerAdapter myAdapterRight;
    private MyDrawerAdapterLeft myAdapterLeft;
    public static LinearLayout bottom_bar,share_page;
    public static RelativeLayout title_bar,wechat_rl,qq_rl;
    public static TextView textView,tv_location,tv_weather,tv_temp;
    private Handler handler;
    public static LinearLayout popupmenu;
    private PopupMenuAdapter popupMenuAdapter;
    private RecyclerView recyclerView;
    private SkinChangeAdapter skinChangeAdapter;
    public static LocationService locationService;//百度定位服务
    public static LocationClient mLocationClient;
    MyLocationListener myListener=new MyLocationListener();
    int lastLocation;
    String lasturl;
    int skinposition;
    long exitTime=0;
    public static PullToRefreshView refreshLayout;
    MyScrollView scrollView;
    String AppPath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/myinfo/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取权限
        Acp.getInstance(this).request(new AcpOptions.Builder().setPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA
        ).build(), new AcpListener() {
            @Override
            public void onGranted() {
                System.out.println("init base log activity succeed");
            }

            @Override
            public void onDenied(List<String> permissions) {
               /* if(permissions!=null){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{}, REQUEST_PERMISSION);
                }*/
                System.out.println("Error:Please accept the permissions require");
            }
        });
        onCreate();
        //getCssFile();
        createFileDir();//创建应用主文件夹
        locationService=((NiceExpApplication)getApplication()).locationService;
        locationService.registerListener(myListener);
        locationService.start();
        LocationClientOption option = new LocationClientOption();

        option.setIsNeedAddress(true);
        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true

        option.setNeedNewVersionRgc(true);
        //可选，设置是否需要最新版本的地址信息。默认不需要，即参数为false

        mLocationClient.setLocOption(option);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
        mLocationClient.start();
        init();
        initAdapter();
        loadimg();//加载顶部栏皮肤
        GridLayoutManager layoutManager = new GridLayoutManager(this, 5, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(5, 10, true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(popupMenuAdapter);

        initSetAdapter();
        ScaleAnimation scaleAnimation=new ScaleAnimation(0.0f,1.0f,0.0f,1.0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(500);
        viewPager.startAnimation(scaleAnimation);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               drawerLayout.openDrawer(Gravity.END);
               popupmenu.setVisibility(View.GONE);
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout2.openDrawer(Gravity.START);
                popupmenu.setVisibility(View.GONE);
            }
        });
        //小说网址
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                myAdapterRight.setCurrentItem(i);
                                                myAdapterRight.notifyDataSetChanged();
                                                switch (i) {
                                                    case 0:
                                                        Webview2.loadUrl("https://www.wtzw.com/");
                                                        break;
                                                    case 1:
                                                        Webview2.loadUrl("http://www.qwsy.com/");
                                                        break;
                                                    case 2:
                                                        Webview2.loadUrl("http://www.fmx.cn/");
                                                        break;
                                                    case 3:
                                                        Webview2.loadUrl("https://b.faloo.com/");
                                                        break;
                                                    case 4:
                                                        Webview2.loadUrl("http://www.ihuaben.com/");
                                                        break;
                                                    case 5:
                                                        Webview2.loadUrl("http://www.1001p.com/1");
                                                        break;
                                                }
                                                drawerLayout.closeDrawers();
                                            }
                                        });

        //网页网址
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                myAdapterLeft.setCurrentItem(i);
                myAdapterLeft.notifyDataSetChanged();
                switch (i){
                    case 0:webView.loadUrl("https://www.baidu.com");
                        break;
                    case 1:webView.loadUrl("https://www.sogou.com");
                        break;
                    case 2:webView.loadUrl("https://www.qq.com");
                        break;
                    case 3:webView.loadUrl("https://www.oneplus.com");
                        break;
                }
                drawerLayout2.closeDrawers();
            }
        });
    }

    private void createFileDir(){
        File file=new File(AppPath);
        if(!file.exists()){
            file.mkdir();
        }
    }
    private void getCssFile(){
        InputStream is=getResources().openRawResource(R.raw.night);
        byte[] buff = new byte[0];
        try {
            buff=new byte[is.available()];
            is.read(buff);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //css2=Base64.encodeToString(buff,Base64.NO_WRAP);
    }
    private void initSetAdapter(){
        recyclerView.setAdapter(popupMenuAdapter);
        listView.setAdapter(myAdapterRight);
        listView2.setAdapter(myAdapterLeft);
        viewPager.setAdapter(myFragmentViewPagerAdapter);
    }
    private void initAdapter(){
        myAdapterRight=new MyDrawerAdapter();//实例化右边侧滑菜单菜单适配器
        myAdapterLeft=new MyDrawerAdapterLeft();//实例化左边侧滑菜单菜单适配器
        popupMenuAdapter=new PopupMenuAdapter();//实例化弹出菜单适配器
        myFragmentViewPagerAdapter=new MyFragmentViewPagerAdapter(getSupportFragmentManager());//实例化Fragment适配器
        skinChangeAdapter=new SkinChangeAdapter();////实例化皮肤适配器
    }
    private void init(){
        scrollView=(MyScrollView) findViewById(R.id.sv);
        refreshLayout=(PullToRefreshView) findViewById(R.id.pulltorefresh);
        radioGroup=(RadioGroup) findViewById(R.id.rg);
        viewPager=(MyViewPager) findViewById(R.id.vp);
        btn1=(RadioButton) findViewById(R.id.btn_1);
        btn2=(RadioButton)findViewById(R.id.btn_2);
        btn3=(RadioButton)findViewById(R.id.btn_3);
        btn4=(RadioButton)findViewById(R.id.btn_4);
        listView=(ListView) findViewById(R.id.lv_option);
        imageView=(ImageView) findViewById(R.id.img_option);
        drawerLayout=(DrawerLayout) findViewById(R.id.dl);
        textView=(TextView) findViewById(R.id.tv_title);
        tv_location=(TextView)findViewById(R.id.location);
        tv_weather=(TextView)findViewById(R.id.weather);
        tv_temp=(TextView)findViewById(R.id.temp);
        drawerLayout2=(DrawerLayout) findViewById(R.id.dl_left);
        listView2=(ListView) findViewById(R.id.lv_option_left);
        imageView2=(ImageView) findViewById(R.id.img_option_left);
        title_bar=findViewById(R.id.title_bar);
        wechat_rl=findViewById(R.id.wechat_rl);
        qq_rl=findViewById(R.id.qq_rl);
        bottom_bar=findViewById(R.id.bottom_bar);
        share_page=findViewById(R.id.share_pop);
        recyclerView=findViewById(R.id.rv_popup_menu);
        popupmenu=findViewById(R.id.popupmenu);
        viewPager.setOffscreenPageLimit(4);
        btn1.setChecked(true);
        wechat_rl.setOnClickListener(this);
        qq_rl.setOnClickListener(this);
        viewPager.addOnPageChangeListener(this);
        radioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        ScaleAnimation animation=new ScaleAnimation(0.5f,1.0f,0.5f,1.0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation.setDuration(500);
        switch (i){
            case R.id.btn_1:
                viewPager.setCurrentItem(0);
                btn1.startAnimation(animation);
                break;
            case R.id.btn_2:
                viewPager.setCurrentItem(1);
                btn2.startAnimation(animation);
                break;
            case R.id.btn_3:
                viewPager.setCurrentItem(2);
                btn3.startAnimation(animation);
                refreshLayout.setEnabled(true);
                break;
            case R.id.btn_4:
                viewPager.setCurrentItem(3);
                btn4.startAnimation(animation);
                refreshLayout.setEnabled(true);
                break;
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
    }

    @Override
    public void onPageSelected(int i) {
    }

    @Override
    public void onPageScrollStateChanged(int i) {

        if (i == 2) {
            switch (viewPager.getCurrentItem()) {
                case 0:
                    btn1.setChecked(true);
                    break;
                case 1:
                    btn2.setChecked(true);
                    break;
                case 2:
                    btn3.setChecked(true);
                    break;
                case 3:
                    btn4.setChecked(true);
                    break;

            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(popupmenu.getVisibility()==View.VISIBLE){
                popupmenu.setVisibility(View.GONE);
            }else if(webView.canGoBack()) {
                webView.goBack();
            }else if(System.currentTimeMillis()-exitTime>2000){
                exitTime=System.currentTimeMillis();
                Toast.makeText(getApplicationContext(),"再按一次退出程序",Toast.LENGTH_SHORT).show();
            }else {
                handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    finish();
                    }
                },0);
            }
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        url_hint.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        initurl();
                    }

                return false;
            }
        });
        return super.dispatchTouchEvent(ev);
    }
    private void initurl(){
        SharedPreferences sharedPreferences = getSharedPreferences("locationdata", Context.MODE_PRIVATE);
        lastLocation = sharedPreferences.getInt("history", 0);
        lasturl=sharedPreferences.getString("lasturl",null);
        Log.i("轻量数据库", String.valueOf(lastLocation));
        Log.i("轻量数据库", lasturl);
        if(!lasturl.equals("")) {
            webView.loadUrl(lasturl);
          /*  webView.scrollBy(0,lastLocation);
            Log.i("检测",String.valueOf(lastLocation));*/
        } else {
            webView.loadUrl("https://www.qq.com");
        }
        url_hint.setVisibility(View.GONE);
    }
//网址分享
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.wechat_rl:
                ClipboardManager clipboardManager=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData=ClipData.newPlainText("label",getCurrentUrl()[0]);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getApplicationContext(),"已复制到剪贴板",Toast.LENGTH_SHORT).show();;
                Intent intent1=new Intent();
                intent1.setClassName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);

                break;
            case R.id.qq_rl:
                ClipboardManager clipboardManager2=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData2=ClipData.newPlainText("label",getCurrentUrl()[0]);
                clipboardManager2.setPrimaryClip(clipData2);
                Toast.makeText(getApplicationContext(),"已复制到剪贴板",Toast.LENGTH_SHORT).show();;
                Intent intent2=new Intent();
                intent2.setClassName("com.tencent.mobileqq","com.tencent.mobileqq.activity.SplashActivity");
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
                break;
        }
    }

    class PopupMenuAdapter extends RecyclerView.Adapter<PopupMenuAdapter.MyViewHolder> {
        List<PageHistory> list=new ArrayList<>();
        public PopupMenuAdapter() {
            list.add(new PageHistory(R.drawable.history, "历史收藏"));
            list.add(new PageHistory(R.drawable.addfav, "加入收藏"));
            list.add(new PageHistory(R.drawable.skin, "更换皮肤"));
            list.add(new PageHistory(R.drawable.setting, "设置"));
            list.add(new PageHistory(R.drawable.night, getResources().getString(R.string.nightMode)));
            list.add(new PageHistory(R.drawable.fullscreen, "开启全屏"));
            list.add(new PageHistory(R.drawable.share, "分享"));
            list.add(new PageHistory(R.drawable.refresh, "刷新"));
            list.add(new PageHistory(R.drawable.clearcache, "清除"));
            list.add(new PageHistory(R.drawable.exit, "退出"));

        }
        public PopupMenuAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.popup_menu_item, null);
            final MyViewHolder myViewHolder = new PopupMenuAdapter.MyViewHolder(view);
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (myViewHolder.getLayoutPosition()) {
                        case 0:
                            popupmenu.setVisibility(View.GONE);
                            Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                            startActivity(intent);
                            break;
                        case 1:
                            URLFAVSQLiteAdapter adapter=new URLFAVSQLiteAdapter(getApplicationContext());
                            PageHistory pageHistory=new PageHistory();
                            pageHistory.setTitle(getCurrentUrl()[1]);
                            pageHistory.setUrl(getCurrentUrl()[0]);
                            adapter.insertHistory(pageHistory);
                            popupmenu.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this,"该网站已收藏",Toast.LENGTH_SHORT).show();
                            break;
                        case 2:popupmenu.setVisibility(View.GONE);
                            startActivity(new Intent(MainActivity.this,SkinsActivity.class));
                            break;
                        case 3:
                            popupmenu.setVisibility(View.GONE);
                            startActivity(new Intent(MainActivity.this,SettingActivity.class));
                            finish();
                            break;
                        case 4:
                            //夜间模式实现
                            popupmenu.setVisibility(View.GONE);
                            int currentMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                            if (currentMode == Configuration.UI_MODE_NIGHT_NO) {
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                                recreate();
                            } else {
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                                recreate();
                            }
                            break;
                            //全屏实现
                        case 5:
                            if (title_bar.getVisibility() == View.VISIBLE) {
                                title_bar.setVisibility(View.GONE);
                                bottom_bar.setVisibility(View.GONE);
                                option_button.setVisibility(View.GONE);
                                url_bar.setVisibility(View.GONE);
                                MainActivity.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                                popupmenu.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "长按屏幕退出全屏", Toast.LENGTH_SHORT).show();
                                webView.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {
                                        if (title_bar.getVisibility() == View.GONE) {
                                            TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                                            animation.setDuration(500);
                                            TranslateAnimation animation2 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                                            animation2.setDuration(500);
                                            title_bar.startAnimation(animation);
                                            url_bar.startAnimation(animation);
                                            option_button.startAnimation(animation2);
                                            title_bar.setVisibility(View.VISIBLE);
                                            option_button.setVisibility(View.VISIBLE);
                                            bottom_bar.setVisibility(View.VISIBLE);
                                            url_bar.setVisibility(View.VISIBLE);
                                            MainActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                                            return true;
                                        }
                                        return false;
                                    }
                                });
                            }
                            break;
                        case 6:
                            popupmenu.setVisibility(View.GONE);
                            Animation animation=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.popup_menu_anim);
                            share_page.startAnimation(animation);
                            share_page.setVisibility(View.VISIBLE);
                            break;

                        case 7:
                            popupmenu.setVisibility(View.GONE);
                            webView.reload();
                            break;
                        case 8:
                            popupmenu.setVisibility(View.GONE);
                            webView.clearCache(true);
                            webView.clearHistory();
                            webView.clearFormData();
                            BitmapFileutil.deletFiles();
                            Toast.makeText(MainActivity.this,"缓存已清空",Toast.LENGTH_SHORT).show();
                            break;
                        case 9:
                            finish();
                            break;
                    }
                }
            });
            return myViewHolder;
        }


        @Override
        public void onBindViewHolder(@NonNull final PopupMenuAdapter.MyViewHolder myViewHolder, int i) {
            myViewHolder.imageView.setImageResource(list.get(i).getLogo());
            myViewHolder.textView.setText(list.get(i).getTitle());
        }


        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private ImageView imageView;
            private TextView textView;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView=itemView.findViewById(R.id.popup_logo);
                textView=itemView.findViewById(R.id.popup_title);


            }
        }
    }
    private String[] getCurrentUrl() {
        String s = "";
        String s1 = "";
        WebBackForwardList backForwardList = webView.copyBackForwardList();
        if (backForwardList != null && backForwardList.getSize() != 0) {
            WebHistoryItem historyItem = backForwardList.getCurrentItem();
            s = historyItem.getUrl();
            s1 = historyItem.getTitle();
        }
        String[] content = {s, s1};
        return content;
    }
    private void loadimg() {
        SharedPreferences sharedPreferences = getSharedPreferences("img", MODE_PRIVATE);
        skinposition = sharedPreferences.getInt("img", 0);
        if (skinposition >= 0) {
            title_bar.setBackgroundResource(skinChangeAdapter.skins2.get(skinposition).getLogo());
        }
    }
    public void onCreate() {
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                for(int i=0;i<permissions.length;i++){
                    if(grantResults[i]== PackageManager.PERMISSION_GRANTED){
                        System.out.println("Permissions -->"+"Permissions Granted" +permissions[i]);
                    }else {
                       ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},1);
                        System.out.println("Permissions -->"+"Permissions Denied" +permissions[i]);
                    }
                }
                break;
            case 2:for(int i=0;i<permissions.length;i++){
                if(grantResults[i]== PackageManager.PERMISSION_GRANTED){
                    System.out.println("Permissions -->"+"Permissions Granted" +permissions[i]);
                }else {
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
                    System.out.println("Permissions -->"+"Permissions Denied" +permissions[i]);
                }
            }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
