package com.example.excelergo.niceexp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity {
private ViewPager viewPager;
private MyViewPagerPoint pagerPoint;
private TextView textView;
private TextView tv_skip;
int imgs[]={R.drawable.pugongyin,R.drawable.plant,R.drawable.blue,R.drawable.sea};
String titles[]={"美好","生活","从此刻","开始"};
List<ImageView> imageViewList;
MyGuidePagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        Window window=getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initView();
        SharedPreferences sp=getSharedPreferences("activity_status",MODE_PRIVATE);
        int position=sp.getInt("status",0);
        if(position==1){
            Intent intent=new Intent(GuideActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        imageViewList=new ArrayList<>();
        for(int i=0;i<imgs.length;i++){
            ImageView imageView=new ImageView(getApplicationContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(imgs[i]);
            imageViewList.add(imageView);
        }
        adapter=new MyGuidePagerAdapter(imageViewList);
        pagerPoint.setPagerPoint(imgs.length);
        pagerPoint.setpPosition(0);
        textView.setText(titles[0]);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                    int lastPosition=i%imgs.length;
                    pagerPoint.setpPosition(lastPosition);
                    textView.setText(titles[i%titles.length]);
                    if(lastPosition==3){
                        tv_skip.setVisibility(View.VISIBLE);
                    }else {
                        tv_skip.setVisibility(View.GONE);
                    }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp=getSharedPreferences("activity_status",MODE_PRIVATE);
                SharedPreferences.Editor editor=sp.edit();
                editor.putInt("status",1);
                editor.apply();
               Intent intent=new Intent(GuideActivity.this,MainActivity.class);
               startActivity(intent);
               finish();

            }
        });
    }

    private void initView(){
        viewPager=findViewById(R.id.vp_guide);
        pagerPoint=findViewById(R.id.pPoint);
        textView=findViewById(R.id.vp_text);
        tv_skip=findViewById(R.id.skip);
    }
}
