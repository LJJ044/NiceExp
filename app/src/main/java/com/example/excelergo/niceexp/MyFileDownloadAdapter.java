package com.example.excelergo.niceexp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

import utils.GoBackAction;
import utils.OnFileDeleteCallBack;
import utils.OnFileManageCallBack;
import utils.OnFileSelectedCallBack;

public class MyFileDownloadAdapter extends RecyclerView.Adapter<MyFileDownloadAdapter.MyViewHolder> {
    private List<String> fileNames;
    private OnFileSelectedCallBack fileSelectedCallBack;
    private OnFileManageCallBack fileManageCallBack;
    private GoBackAction goBackAction;
    Context context;

    public MyFileDownloadAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyFileDownloadAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_download_manage_item,viewGroup,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
                myViewHolder.textView.setText(fileNames.get(i));
                myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(fileSelectedCallBack!=null){
                            fileSelectedCallBack.onOpenFile(fileNames.get(i));
                        }
                    }
                });
                myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        PopupMenu menu=new PopupMenu(context,view);
                        menu.getMenuInflater().inflate(R.menu.menu,menu.getMenu());
                        menu.show();
                        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()){
                                    case R.id.check:
                                        if(fileSelectedCallBack!=null){
                                            fileSelectedCallBack.onOpenFile(fileNames.get(i));
                                        }
                                        break;
                                    case R.id.delete:
                                        FileDownloadutil.deleteSpecificFile(fileNames.get(i));
                                        if(goBackAction!=null){
                                            goBackAction.goBack(33);
                                        }

                                        break;
                                    case R.id.open:
                                        if(fileManageCallBack!=null){
                                            fileManageCallBack.onOpenFilelocation(fileNames.get(i));
                                        }
                                        break;
                                }
                                return false;
                            }
                        });
                        return false;
                    }
                });
    }


    @Override
    public int getItemCount() {
        fileNames=FileDownloadutil.listAllFileNmae();
        return fileNames.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.fileName);

        }
    }

    public void setFileSelectedCallBack(OnFileSelectedCallBack fileSelectedCallBack) {
        this.fileSelectedCallBack = fileSelectedCallBack;
    }

    public void setFileManageCallBack(OnFileManageCallBack fileManageCallBack) {
        this.fileManageCallBack = fileManageCallBack;
    }

    public void setGoBackAction(GoBackAction goBackAction) {
        this.goBackAction = goBackAction;
    }
}
