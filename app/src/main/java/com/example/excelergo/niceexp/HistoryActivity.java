package com.example.excelergo.niceexp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private ImageView imageView_delete;
    private URLSQLiteAdapter adapter;
    private URLFAVSQLiteAdapter adapter2;
    private List<PageHistory> histories;
    private MyHistoryAdapter myHistoryAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private RadioButton radioButton;
    private String[] titles={"历史记录","收藏"};
    private List<Fragment> list;
    private MyFragmentPagerAdapter pagerAdapter;
    private RelativeLayout rl_history;
    private SkinChangeAdapter skinChangeAdapter;
    int skinposition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        rl_history=findViewById(R.id.rl_history);
        imageView_delete=(ImageView)findViewById(R.id.history_delete);
        tabLayout=(TabLayout) findViewById(R.id.tablay);
        viewPager=(ViewPager) findViewById(R.id.vp);
        radioButton=(RadioButton) findViewById(R.id.rb_goBack);
        skinChangeAdapter=new SkinChangeAdapter();
        myHistoryAdapter=new MyHistoryAdapter(getApplicationContext());
        loadimg();//设置标题栏背景
        list=new ArrayList<>();
        list.add(new HistoryFragment());
        list.add(new CollectionFragment());
        pagerAdapter=new MyFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        adapter=new URLSQLiteAdapter(getApplicationContext());
        adapter2=new URLFAVSQLiteAdapter(getApplicationContext());
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                for(int i=0;i<tabLayout.getTabCount();i++){
                    if(tab==tabLayout.getTabAt(i)){
                        viewPager.setCurrentItem(i);
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                tabLayout.getTabAt(i).select();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imageView_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(HistoryActivity.this)
                        .setTitle("确定清空吗?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(viewPager.getCurrentItem()==0) {
                                    adapter.deleteAllhistory();
                                    adapter.querAllHistory();
                                    histories=adapter.querAllHistory();
                                    HistoryFragment.recyclerView.setVisibility(View.GONE);
                                }else if(viewPager.getCurrentItem()==1){
                                    adapter2.deleteAllhistory();
                                    adapter2.querAllHistory();
                                    histories=adapter2.querAllHistory();
                                    CollectionFragment.recyclerView.setVisibility(View.GONE);
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).show();
            }
        });
    }
    class MyFragmentPagerAdapter extends FragmentPagerAdapter{

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return list.get(i);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
    private void loadimg(){
        SharedPreferences sharedPreferences=getSharedPreferences("img",MODE_PRIVATE);
        skinposition=sharedPreferences.getInt("img",0);
        if(skinposition>=0) {
            rl_history.setBackgroundResource(skinChangeAdapter.skins2.get(skinposition).getLogo());
        }else {
            rl_history.setBackgroundColor(getResources().getColor(R.color.state_bar_color));
        }

    }
}
