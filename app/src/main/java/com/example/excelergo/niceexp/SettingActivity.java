package com.example.excelergo.niceexp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import utils.GoBackAction;

public class SettingActivity extends AppCompatActivity {
private RecyclerView rv_setting;
public MySettingRecyclerAdapter adapter;
private RadioButton goBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        rv_setting=(RecyclerView) findViewById(R.id.rv_setting);
        goBack=(RadioButton) findViewById(R.id.img_goBack);
        LinearLayoutManager manager=new LinearLayoutManager(SettingActivity.this);
        manager.setOrientation(LinearLayout.VERTICAL);
        rv_setting.setLayoutManager(manager);
        adapter=new MySettingRecyclerAdapter();
        rv_setting.setAdapter(adapter);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingActivity.this,"已变更设置",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(SettingActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        adapter.setGoBackAction(new GoBackAction() {
            @Override
            public void goBack(int i) {
                startActivity(new Intent(SettingActivity.this,DownloadManageActivity.class));
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Toast.makeText(SettingActivity.this,"已变更设置",Toast.LENGTH_SHORT).show();
        if(keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){
            startActivity(new Intent(SettingActivity.this,MainActivity.class));
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
