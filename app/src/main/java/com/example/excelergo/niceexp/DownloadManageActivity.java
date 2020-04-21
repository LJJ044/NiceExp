package com.example.excelergo.niceexp;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import utils.GoBackAction;
import utils.OnFileDeleteCallBack;
import utils.OnFileManageCallBack;
import utils.OnFileSelectedCallBack;

public class DownloadManageActivity extends AppCompatActivity {
private RecyclerView rv_file;
private RadioButton goBack_img;
private TextView tv_state;
private List<String> fileName;
private MyFileDownloadAdapter fileDownloadAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_manage);
        rv_file=(RecyclerView)findViewById(R.id.rv_fileName);
        goBack_img=(RadioButton) findViewById(R.id.goBack_download);
        tv_state=(TextView)findViewById(R.id.state_check);
        fileName=FileDownloadutil.listAllFileNmae();
        if(fileName.size()==0){

            tv_state.setVisibility(View.VISIBLE);
        }
        fileDownloadAdapter=new MyFileDownloadAdapter(this);
        LinearLayoutManager manager=new LinearLayoutManager(DownloadManageActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_file.setLayoutManager(manager);
        rv_file.setAdapter(fileDownloadAdapter);
        goBack_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
       fileDownloadAdapter.setFileSelectedCallBack(new OnFileSelectedCallBack() {
           @Override
           public void onOpenFile(String fileName) {
               String fileLocation=FileDownloadutil.FilePath+fileName;
               File file=new File(fileLocation);
               Uri uri=null;
               if(Build.VERSION.SDK_INT<=Build.VERSION_CODES.M){
                   uri=Uri.fromFile(file);
               }else {
                   uri=FileProvider.getUriForFile(getApplicationContext(),"com.example.excelergo.niceexp.provider",file);
               }
               String type=FileDownloadutil.getMimeType(file);
               Intent intent=new Intent();
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION );
               intent.setAction(Intent.ACTION_VIEW);
               intent.setDataAndType(uri,type);
               startActivity(intent);
           }
       });
       fileDownloadAdapter.setFileManageCallBack(new OnFileManageCallBack() {
           @Override
           public void onOpenFilelocation(String fileLocation) {
               String fileLoaction=FileDownloadutil.FilePath+fileLocation;
               File file=new File(fileLoaction);
               Uri uri=null;
               if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
                   uri=Uri.fromFile(file);
               }else {
                   uri=FileProvider.getUriForFile(DownloadManageActivity.this,"com.example.excelergo.niceexp.provider",file);
               }
               Intent intent=new Intent();
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION |Intent.FLAG_GRANT_WRITE_URI_PERMISSION );
               intent.setAction(Intent.ACTION_VIEW);
               intent.setDataAndType(uri,"file");
               startActivity(intent);

           }
       });
     fileDownloadAdapter.setGoBackAction(new GoBackAction() {
         @Override
         public void goBack(int i) {
             fileDownloadAdapter.notifyDataSetChanged();
         }
     });
    }
}
