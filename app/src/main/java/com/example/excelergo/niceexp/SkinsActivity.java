package com.example.excelergo.niceexp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import utils.GoBackAction;

public class SkinsActivity extends AppCompatActivity {
private RecyclerView recyclerView;
private SkinChangeAdapter adapter;
private RelativeLayout rl_skin;
int skinposition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skins);
        recyclerView=findViewById(R.id.rv_skins);
        rl_skin=findViewById(R.id.rl_skin);
        adapter=new SkinChangeAdapter();
        RecyclerView.LayoutManager manager=new LinearLayoutManager(getApplicationContext());
        ((LinearLayoutManager) manager).setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setGoBackAction(new GoBackAction() {
            @Override
            public void goBack(int i) {
                rl_skin.setBackgroundResource(adapter.skins.get(i).getLogo());
                SharedPreferences sharedPreferences=getSharedPreferences("img",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putInt("img",i);
                editor.commit();
                    finish();
            }
        });
            loadimg();
    }
    private void loadimg(){
        SharedPreferences sharedPreferences=getSharedPreferences("img",MODE_PRIVATE);
        skinposition=sharedPreferences.getInt("img",0);
        Log.i("数据库",String.valueOf(skinposition));
        if(skinposition>=0) {
            rl_skin.setBackgroundResource(adapter.skins2.get(skinposition).getLogo());
        }else {
            rl_skin.setBackgroundColor(getResources().getColor(R.color.state_bar_color));
        }

    }
}
