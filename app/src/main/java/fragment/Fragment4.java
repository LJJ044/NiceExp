package fragment;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.PullToRefreshView;
import com.example.excelergo.niceexp.MainActivity;
import com.example.excelergo.niceexp.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import activity.HistoryActivity;
import activity.LoginActivity;
import utils.ImgeClipUtil;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.example.excelergo.niceexp.MainActivity.refreshLayout;
import static com.example.excelergo.niceexp.MainActivity.tv_temp;
import static com.example.excelergo.niceexp.MainActivity.tv_weather;

public class Fragment4 extends Fragment implements View.OnClickListener {
    private ImageView login_img, touxiang;
    public static TextView tv_user, tv_location4;
    private TextView openg, openc;
    private RelativeLayout relativeLayout, mainLayout;
    private EditText editText;
    private LinearLayout popup_choose;
    private Bitmap head;
    public static String city2;
    String temp, weather;
    JSONObject jsonObject1;
    private String headFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/myinfo/userInfo/";
    public Fragment4() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment4, container, false);
        init(view);
        initListener();
        loadUser();//加载用户
        loadTouxiang();//加载头像
        readsign();//读取签名内容
        loadWeatherData();//加载天气数据

        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()== MotionEvent.ACTION_DOWN){
                    refreshLayout.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            if(MainActivity.locationService.isStart()!=true){
                                MainActivity.locationService.start();
                                MainActivity.mLocationClient.start();
                            }
                            loadWeatherData();
                            refreshLayout.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    refreshLayout.setRefreshing(false);
                                }
                            },1000);
                        }
                    });
                }
                return false;
            }
        });
        return view;
    }
    private void loadUser(){
        SharedPreferences sp=getContext().getSharedPreferences("user",MODE_PRIVATE);
        String userName=sp.getString("userName",null);
        if(userName!=null) {
            tv_user.setText("用户名：" + userName);
        }
    }
    @Override
    public void onClick(View view) {
        Uri uri;
        File camerafile = new File(headFilePath + "head.jpg");
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            uri = Uri.fromFile(camerafile);
        } else {
            //ContentValues values=new ContentValues(1);
            //values.put(MediaStore.Images.Media.DATA,camerafile.getPath());
            //uri=getActivity().getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
            uri = FileProvider.getUriForFile(getActivity(), "com.example.excelergo.niceexp.provider", camerafile);
        }
        switch (view.getId()) {
            case R.id.main_page:
                editText.clearFocus();
                editText.setBackgroundColor(getContext().getResources().getColor(R.color.transparent));
                if (popup_choose.getVisibility() == View.VISIBLE) {
                    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.popup_menu_anim2);
                    popup_choose.startAnimation(animation);
                    popup_choose.setVisibility(View.GONE);
                }
                if (!editText.getText().toString().equals("")) {
                    saveSign();//保存签名内容到本地
                }
                break;
            case R.id.img_touxiang:
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.popup_menu_anim);
                popup_choose.startAnimation(animation);
                popup_choose.setVisibility(View.VISIBLE);
                break;
            case R.id.head_img:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.recent:
                startActivity(new Intent(getActivity(), HistoryActivity.class));
                break;
            //打开相册
            case R.id.open_gallery:
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, 1);
                popup_choose.setVisibility(View.GONE);
                break;
            //打开相机
            case R.id.open_camera:
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                    int hasCameraPermission = getActivity().checkCallingOrSelfPermission(Manifest.permission.CAMERA);
                    if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
                    } else {
                        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent2.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(intent2, 2);// 采用ForResult打开
                        popup_choose.setVisibility(View.GONE);
                        break;
                    }
                }


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = null;
        File camerafile = new File(headFilePath + "head.jpg");
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            uri = Uri.fromFile(camerafile);
        } else {
            uri = FileProvider.getUriForFile(getActivity(), "com.example.excelergo.niceexp.provider", camerafile);
        }
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());// 裁剪图片
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    cropPhoto(uri);// 裁剪图片
                }
                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");
                    if (head != null) {
                        setPicToView(head);// 保存在SD卡中
                        touxiang.setImageBitmap(ImgeClipUtil.ClipSquareBitmap(head, 200, head.getWidth()));// 用ImageView显示出来

                    }
                    break;
                }
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 调用系统的裁剪功能
     *
     * @param uri
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        //授予Activity权限
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        //设置数据类型
        intent.setDataAndType(uri, "image/*");
        //裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高(图片质量)
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        //发送数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    //将Bitmap保存到本地
    private void setPicToView(Bitmap bitmap) {
        try {
            File file1 = new File(headFilePath);
            if (!file1.exists()) {
                file1.mkdir();
            }
            File file2 = new File(headFilePath + "head.JPG");
            if(!file2.exists()) {
                FileOutputStream fos = new FileOutputStream(file2);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init(View view) {
        login_img = view.findViewById(R.id.head_img);
        tv_user = view.findViewById(R.id.userName);
        tv_location4 = view.findViewById(R.id.tv_location);
        touxiang = view.findViewById(R.id.img_touxiang);
        openg = view.findViewById(R.id.open_gallery);
        openc = view.findViewById(R.id.open_camera);
        editText = view.findViewById(R.id.sign_content);
        relativeLayout = view.findViewById(R.id.recent);
        mainLayout = view.findViewById(R.id.main_page);
        popup_choose = view.findViewById(R.id.popup_choose);
    }

    private void initListener() {
        mainLayout.setOnClickListener(this);
        touxiang.setOnClickListener(this);
        login_img.setOnClickListener(this);
        relativeLayout.setOnClickListener(this);
        openg.setOnClickListener(this);
        openc.setOnClickListener(this);
    }

    private void loadTouxiang() {
        Bitmap bitmap = BitmapFactory.decodeFile(headFilePath + "head.jpg");
        if (bitmap != null) {
            touxiang.setImageBitmap(ImgeClipUtil.ClipSquareBitmap(bitmap, 200, bitmap.getWidth()));
        }
    }

    private void readsign() {
        try {
            FileInputStream fis = new FileInputStream(headFilePath + "sign.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            StringBuilder sb=new StringBuilder();
            String str;
            if((str=br.readLine())!=null) {
                sb.append(str);
            }
            editText.setText(sb.toString());
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveSign() {
        String sign = editText.getText().toString();
        File file = new File(headFilePath);
        if (!file.exists()) {
            file.mkdir();
        }
        File file2 = new File(headFilePath + "sign.txt");
        try {
            FileOutputStream fos = new FileOutputStream(file2);
            fos.write(sign.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadWeatherData() {
                String head1 = "http://apis.juhe.cn/simpleWeather/query?city=";
                String head3 = "&key=460f4b8be1eeda3f990cde2604ad5279";
                final String weatherStr=head1 + city2 + head3;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(weatherStr);
                            HttpURLConnection connection=(HttpURLConnection) url.openConnection();
                            connection.setConnectTimeout(5000);
                            connection.setReadTimeout(5000);
                            connection.setRequestMethod("GET");
                            InputStream is=connection.getInputStream();
                            handleData(is);
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(weather!=null&&temp!=null) {
                                    tv_weather.setText(weather);
                                    tv_temp.setText(temp+"℃");
                                    handler.sendEmptyMessage(1);
                                }

                            }
                        });
                    }

                }).start();



    }
    private void handleData(InputStream inputStream){
        BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
        String str="";
        StringBuilder sb=new StringBuilder();
        try {
            while ((str=br.readLine())!=null) {
                sb.append(str);
            }
                String content=sb.toString();
                Log.i("天气",content);
                JSONObject object = new JSONObject(content);
                jsonObject1 = object.getJSONObject("result");
                JSONObject object2 = jsonObject1.getJSONObject("realtime");
                temp = object2.getString("temperature");
                weather = object2.getString("info");

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
/*    private void handleData(String jsonString){
        try {
            JSONObject object = new JSONObject(jsonString);
            jsonObject1 = object.getJSONObject("result");
            JSONObject object2 = jsonObject1.getJSONObject("realtime");
            temp = object2.getString("temperature");
            weather = object2.getString("info");
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if(jsonObject1==null) {
                        Toast.makeText(getActivity(), "天气请求次数已过", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
}